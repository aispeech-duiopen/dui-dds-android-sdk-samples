package com.aispeech.nativedemo.ui.adapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.aispeech.ailog.AILog;
import com.aispeech.dui.dds.DDS;
import com.aispeech.nativedemo.R;
import com.aispeech.nativedemo.bean.MessageBean;
import com.aispeech.nativedemo.bean.WeatherBean;
import com.aispeech.nativedemo.widget.pageview.view.PageRecyclerView;
import com.aispeech.nativedemo.widget.pageview.view.PageView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;

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
                holder = new InputViewHolder(view);
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
            case MessageBean.TYPE_WIDGET_WEATHER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather, parent, false);
                holder = new WeatherViewHolder(view);
                break;
                default:
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

        Runnable setCurrentItemRunnable = () -> pageview.setCurrentItem(message.getCurrentPage() - 1, false);

        Runnable addPageChangeListenerRunnable = () -> pageview.addOnPageChangeListener(new ListWidgetPageChangeListener(position));
        pageview.postDelayed(setCurrentItemRunnable, 200);
        pageview.postDelayed(addPageChangeListenerRunnable, 1000);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MessageBean message = mList.get(position);
        int itemViewType = mList.get(position).getType();
        switch (itemViewType) {
            case MessageBean.TYPE_INPUT:
                ((InputViewHolder) holder).content.setText(message.getText());
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
            case MessageBean.TYPE_WIDGET_WEATHER:
                WeatherBean weatherBean = message.getWeatherBean();
                WeatherBean.ForecastChooseBean today = weatherBean.getForecastChoose().get(0);
                ((WeatherViewHolder) holder).location.setText(weatherBean.getCityName());
                ((WeatherViewHolder) holder).todayTianqi.setText(today.getConditionDay());
                ((WeatherViewHolder) holder).todayFeng.setText(today.getWindDirDay() + "，" + today.getWindLevelDay() + "级");
                ((WeatherViewHolder) holder).todayWendu.setText(today.getTempNight() + "~" + today.getTempDay() + "℃");
                ((WeatherViewHolder) holder).todayIcon.setImageResource(getIconByString(today.getConditionDay()));
                /*
                最多显示未来五天的天气
                 */
                for (int i = 0; i < (weatherBean.getForecast().size() - 1 > 5 ? 5: weatherBean.getForecast().size() - 1); i++) {
                    WeatherBean.ForecastBean bean = weatherBean.getForecast().get(i + 1);
                    ((WeatherViewHolder) holder).dates[i].setText(bean.getPredictDate().substring(5));
                    ((WeatherViewHolder) holder).icons[i].setImageResource(getIconByString(bean.getConditionDay()));
                    ((WeatherViewHolder) holder).temps[i].setText(bean.getTempNight() + "~" + bean.getTempDay() + "℃");
                }
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

    class InputViewHolder extends RecyclerView.ViewHolder {
        private TextView content;

        public InputViewHolder(View itemView) {
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

    class WeatherViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.location)
        TextView location;
        @BindView(R.id.todayTianqi)
        TextView todayTianqi;
        @BindView(R.id.todayFeng)
        TextView todayFeng;
        @BindView(R.id.todayWendu)
        TextView todayWendu;
        @BindView(R.id.todayIcon)
        ImageView todayIcon;
        @BindView(R.id.date1)
        TextView date1;
        @BindView(R.id.icon1)
        ImageView icon1;
        @BindView(R.id.temp1)
        TextView temp1;
        @BindView(R.id.date2)
        TextView date2;
        @BindView(R.id.icon2)
        ImageView icon2;
        @BindView(R.id.temp2)
        TextView temp2;
        @BindView(R.id.date3)
        TextView date3;
        @BindView(R.id.icon3)
        ImageView icon3;
        @BindView(R.id.temp3)
        TextView temp3;
        @BindView(R.id.date4)
        TextView date4;
        @BindView(R.id.icon4)
        ImageView icon4;
        @BindView(R.id.temp4)
        TextView temp4;
        @BindView(R.id.date5)
        TextView date5;
        @BindView(R.id.icon5)
        ImageView icon5;
        @BindView(R.id.temp5)
        TextView temp5;

        TextView[] dates;
        ImageView[] icons;
        TextView[] temps;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            dates = new TextView[5];
            dates[0] = date1;
            dates[1] = date2;
            dates[2] = date3;
            dates[3] = date4;
            dates[4] = date5;

            icons = new ImageView[5];
            icons[0] = icon1;
            icons[1] = icon2;
            icons[2] = icon3;
            icons[3] = icon4;
            icons[4] = icon5;

            temps = new TextView[5];
            temps[0] = temp1;
            temps[1] = temp2;
            temps[2] = temp3;
            temps[3] = temp4;
            temps[4] = temp5;
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
            listener.onSelected(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private OnPageChangeListener listener;

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.listener = listener;
    }

    public interface OnPageChangeListener {
        void onSelected(int position);
    }

    private int getIconByString(String weather) {
        Log.d(TAG, "weather: " + weather);
        switch (weather) {
            case "晴":
                return R.drawable.vector_drawable_qingtian_day;
            case "阴":
                return R.drawable.vector_drawable_yingtian;
            case "多云":
                return R.drawable.vector_drawable_qing_duoyun_day;
            case "小雨":
                return R.drawable.vector_drawable_xiaoyu;
            case "中雨":
                return R.drawable.vector_drawable_zhongyu;
            case "大雨":
                return R.drawable.vector_drawable_dayu;
            case "暴雨":
                return R.drawable.vector_drawable_baoyu;
            case "小雪":
                return R.drawable.vector_drawable_xiaoxue;
            case "中雪":
                return R.drawable.vector_drawable_zhongxue;
            case "大雪":
                return R.drawable.vector_drawable_daxue;
            case "暴雪":
                return R.drawable.vector_drawable_baoxue;
            case "雷阵雨":
                return R.drawable.vector_drawable_leizhenyu;
            case "雾霾":
                return R.drawable.vector_drawable_wumai;
            case "雾":
                return R.drawable.vector_drawable_wu;
            default:
                if (weather.contains("雨")) {
                    return R.drawable.vector_drawable_duoyun_yu_day;
                } else if (weather.contains("云")) {
                    return R.drawable.vector_drawable_qing_duoyun_day;
                } else {
                    return R.drawable.vector_drawable_qing_duoyun_day;
                }
        }
    }

}
