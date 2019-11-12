package com.aispeech.nativedemo.test;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.aispeech.nativedemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Fei
 * @date 2018/6/8
 */
public class ListAdapter extends BaseAdapter {
    private List<ListItem> mData = new ArrayList<>();

    public ListAdapter(List<ListItem> data) {
        mData.addAll(data);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ListViewHolder holder;

        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.test_item, viewGroup, false);
            holder = new ListViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ListViewHolder) view.getTag();
        }

        ListItem item = mData.get(i);
        holder.title.setText(item.getTitle());
        return view;
    }

    static class ListViewHolder {
        TextView title;

        ListViewHolder(View itemView) {
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}
