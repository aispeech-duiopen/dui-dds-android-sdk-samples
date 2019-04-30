package com.aispeech.nativedemo.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.aispeech.ailog.AILog;
import com.aispeech.dui.dds.DDS;
import com.aispeech.nativedemo.R;
import com.aispeech.nativedemo.bean.MessageBean;
import com.aispeech.nativedemo.widget.pageview.adapter.BasePageAdapter;
import com.aispeech.nativedemo.widget.pageview.adapter.OnPageDataListener;


public class ListWidgetAdapter extends BasePageAdapter<MessageBean, ListWidgetAdapter.MainHolder> {
    public static final String TAG = "ListWidgetAdapter";
    public static final int MAX_VALUE = 28800;
    private boolean isShowDeleteIcon;
    private int mLayoutId;
    private int mPosition;

    public ListWidgetAdapter(Context context, @LayoutRes int itemLayoutId, int position) {
        super(context);
        mLayoutId = itemLayoutId;
        mPosition = position;
    }

    @Override
    public int getItemCount() {
        if (mIsLooping) {
            return MAX_VALUE;
        }
        return super.getItemCount();
    }

    @Override
    protected int getLayoutId(int viewType) {
        return mLayoutId;
    }

    @Override
    protected MainHolder getViewHolder(View itemView, int viewType) {
        return new MainHolder(itemView);
    }

    @Override
    protected void convert(MainHolder holder, final int position, MessageBean originItem) {
        int adjustedPosition = position;
        if (mOrientation == OnPageDataListener.HORIZONTAL && mRow * mColumn > 1) {
            adjustedPosition = getAdjustedPosition(position, mRow * mColumn);
        }
        MessageBean item = mTargetData.get(adjustedPosition);
        if (adjustedPosition != -1) {
            if (item == null) {
                holder.itemView.setOnLongClickListener(null);
                holder.itemView.setOnClickListener(null);
                holder.title.setText("");
                holder.subtitle.setText("");
                holder.index.setText("");
            } else {
                holder.title.setText(item.getTitle());
                holder.subtitle.setText(item.getSubTitle());
                holder.index.setText(String.valueOf(position % (mRow * mColumn) + 1));
                holder.itemView.setOnLongClickListener(new OnItemLongClickListener(adjustedPosition));
                holder.itemView.setOnClickListener(new OnItemClickListener(adjustedPosition));
            }
        }
    }

    class MainHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView subtitle;
        private TextView index;

        public MainHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
            index = itemView.findViewById(R.id.t_index);
        }
    }

    class OnItemLongClickListener implements View.OnLongClickListener {
        private int mPosition;

        public OnItemLongClickListener(int position) {
            mPosition = position;
        }

        @Override
        public boolean onLongClick(View v) {
            isShowDeleteIcon = !isShowDeleteIcon;
            notifyDataSetChanged();
            return true;
        }
    }

    class OnItemClickListener implements View.OnClickListener {
        private int mTargetPos;

        public OnItemClickListener(int adjustedPos) {
            mTargetPos = adjustedPos;
        }

        @Override
        public void onClick(View v) {
            AILog.i(TAG, "onClick:%!," + mTargetPos + " clicked! list position in dialog list: " + mPosition + ", dialog list size: " + DialogAdapter.mList.size());
            if (!DialogAdapter.mState.equals("avatar.silence") && mPosition == DialogAdapter.mList.size() - 1) {
                int targetItem = mTargetPos % 5 + 1;
                DDS.getInstance().getAgent().publishSticky("list.item.select", "{\"index\":" + targetItem + "}");
            }
        }
    }

}
