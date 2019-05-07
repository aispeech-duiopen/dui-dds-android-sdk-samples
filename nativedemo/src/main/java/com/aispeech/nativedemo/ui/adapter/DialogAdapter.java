package com.aispeech.nativedemo.ui.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.aispeech.ailog.AILog;
import com.aispeech.dui.dds.DDS;
import com.aispeech.nativedemo.R;
import com.aispeech.nativedemo.bean.MessageBean;
import com.aispeech.nativedemo.widget.pageview.view.PageRecyclerView;
import com.aispeech.nativedemo.widget.pageview.view.PageView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.LinkedList;

import static com.aispeech.nativedemo.DuiApplication.getContext;

public class DialogAdapter extends RecyclerView.Adapter {

    private static final String TAG = "DialogAdapter";
    public static LinkedList<MessageBean> mList;
    public static String mState;

    public DialogAdapter(LinkedList<MessageBean> list) {
        mList = list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AILog.i(TAG, "onCreateViewHolder" + viewType);
        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case MessageBean.TYPE_INPUT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_input, parent, false);
                holder = new IntputViewHolder(view);
                break;
            case MessageBean.TYPE_OUTPUT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_output, parent, false);
                holder = new OutputViewHolder(view);
                break;
            case MessageBean.TYPE_WIDGET_CONTENT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_widget_content, parent, false);
                holder = new WidgetContentViewHolder(view);
                break;

            case MessageBean.TYPE_WIDGET_LIST:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_widget_list, parent, false);
                holder = new WidgetListViewHolder(view);
                break;

            case MessageBean.TYPE_WIDGET_WEB:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_widget_web, parent, false);
                holder = new WebViewHolder(view);
                break;

            case MessageBean.TYPE_WIDGET_MEDIA:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_widget_web, parent, false);
                holder = new WebViewHolder(view);
                break;
        }
        return holder;
    }

    private void bindPageView(final PageView pageview, final MessageBean message, final int position) {
        AILog.i(TAG, "bindPageView" + position + ", listwidget page: " + message.getCurrentPage() + ", pageview: " + pageview);
        ArrayList<MessageBean> items = message.getMessageBeanList();
        pageview.setPageRow(message.getItemsPerPage());
        ListWidgetAdapter adapter = new ListWidgetAdapter(getContext(), R.layout.item_horizontal_grid2, position);
        pageview.setAdapter(adapter);
        pageview.updateAll(items);

        Runnable setCurrentItemRunnable = new Runnable() {
            @Override
            public void run() {
                pageview.setCurrentItem(message.getCurrentPage() - 1, false);
            }
        };

        Runnable addPageChangeListenerRunnable = new Runnable() {
            @Override
            public void run() {
                pageview.addOnPageChangeListener(new ListWidgetPageChangeListener(position));
            }
        };
        pageview.postDelayed(setCurrentItemRunnable, 200);
        pageview.postDelayed(addPageChangeListenerRunnable, 1000);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MessageBean message = mList.get(position);
        int itemViewType = mList.get(position).getType();
        switch (itemViewType) {
            case MessageBean.TYPE_INPUT:
                ((IntputViewHolder) holder).content.setText(message.getText());
                break;
            case MessageBean.TYPE_OUTPUT:
                ((OutputViewHolder) holder).content.setText(message.getText());
                break;
            case MessageBean.TYPE_WIDGET_CONTENT:
                ((WidgetContentViewHolder) holder).title.setText(message.getTitle());
                ((WidgetContentViewHolder) holder).subTitle.setText(message.getSubTitle());
                Log.d("xxx", " xxxxx : " + message.getImgUrl());
                ((WidgetContentViewHolder) holder).imageView.setImageURI(Uri.parse(message.getImgUrl()));
                break;
            case MessageBean.TYPE_WIDGET_LIST:
                holder.setIsRecyclable(false);
                bindPageView(((WidgetListViewHolder) holder).viewPager, message, position);
                break;
            case MessageBean.TYPE_WIDGET_WEB:
                ((WebViewHolder) holder).webView.loadUrl(message.getUrl());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    @Override
    public int getItemViewType(int position) {
        Log.d(TAG, "getItemViewType " + position);
        return mList.get(position).getType();
    }

    class OutputViewHolder extends RecyclerView.ViewHolder {
        private TextView content;

        public OutputViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
        }
    }

    class IntputViewHolder extends RecyclerView.ViewHolder {
        private TextView content;

        public IntputViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
        }
    }

    class WidgetContentViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView subTitle;
        private SimpleDraweeView imageView;

        public WidgetContentViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            subTitle = itemView.findViewById(R.id.sub_title);
            imageView = itemView.findViewById(R.id.image);
        }
    }

    class WidgetListViewHolder extends RecyclerView.ViewHolder {
        private PageView viewPager;

        public WidgetListViewHolder(View itemView) {
            super(itemView);
            viewPager = itemView.findViewById(R.id.pageView);
        }
    }

    class WebViewHolder extends RecyclerView.ViewHolder {
        private WebView webView;

        public WebViewHolder(View itemview) {
            super(itemview);
            webView = itemview.findViewById(R.id.mywebview);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, final String url) {
                }
            });
        }
    }

    public class ListWidgetPageChangeListener implements PageRecyclerView.OnPageChangeListener {

        private int mPosition;

        ListWidgetPageChangeListener(int position) {
            mPosition = position;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            MessageBean message = mList.get(mPosition);
            message.setCurrentPage(position + 1);
            if (!mState.equals("avatar.silence") && mPosition == mList.size() - 1) {
                int targetPage = position + 1;
                DDS.getInstance().getAgent().publishSticky("list.page.switch", "{\"pageNumber\":" + targetPage + "}");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}
