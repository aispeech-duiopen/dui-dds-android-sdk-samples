package com.aispeech.h5demo.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.aispeech.h5demo.MainActivity;
import com.aispeech.h5demo.R;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseTestActivity extends AppCompatActivity {
    protected String TAG = this.getClass().getSimpleName();
    protected ListView mListView;
    private TextView mTitleTv;
    protected List<ListItem> mData = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initData();
        initView();
    }

    protected void initView() {
        String title = getIntent().getStringExtra("title");
        mTitleTv = findViewById(R.id.title_tv);
        mTitleTv.setText(title);
        mListView = findViewById(R.id.lv);
        mListView.setAdapter(new ListAdapter(mData));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListItem item = mData.get(position);
                BaseTestActivity.this.onItemClick(item);
            }
        });
    }

    protected abstract void initData();

    protected int getLayoutId() {
        return R.layout.test_main;
    }

    protected abstract void onItemClick(ListItem item);

    protected void toMain() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
