package com.aispeech.h5demo.test.vad;

import android.util.Log;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;
import com.aispeech.h5demo.test.BaseTestActivity;
import com.aispeech.h5demo.test.ListItem;

public class VadTestActivity extends BaseTestActivity {
    private final String TAG = "VadTestActivity";
    @Override
    protected void initData() {
        mData.add(new ListItem<>("设置VAD后端停顿时间", null));
        mData.add(new ListItem<>("获取VAD后端停顿时间", null));
        mData.add(new ListItem<>("设置VAD前端静音检测超时时间", null));
        mData.add(new ListItem<>("获取VAD前端静音检测超时时间", null));
    }

    @Override
    protected void onItemClick(ListItem item) {
        switch (item.getTitle()) {
            case "设置VAD后端停顿时间":
                setVadPauseTime();
                break;
            case "获取VAD后端停顿时间":
                getVadPauseTime();
                break;
            case "设置VAD前端静音检测超时时间":
                setVadTimeout();
                break;
            case "获取VAD前端静音检测超时时间":
                getVadTimeout();
                break;

        }
    }

    private void setVadPauseTime() {

        //设置VAD后端停顿时间
        try {
            DDS.getInstance().getAgent().getASREngine().setVadPauseTime(1000);
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void getVadPauseTime() {

        //获取VAD后端停顿时间
        try {
            long vadPauseTime = DDS.getInstance().getAgent().getASREngine().getVadPauseTime();
            Log.d(TAG,"vadPauseTime:"+vadPauseTime);
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void setVadTimeout() {
        //设置VAD前端静音检测超时时间
        try {
            DDS.getInstance().getAgent().getASREngine().setVadTimeout(1000);
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void getVadTimeout() {

        //获取VAD前端静音检测超时时间
        try {
            long vadPauseTime = DDS.getInstance().getAgent().getASREngine().getVadTimeout();
            Log.d(TAG,"vadPauseTime:"+vadPauseTime);
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }




}
