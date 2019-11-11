package com.aispeech.h5demo.test.vprint;

import android.util.Log;

import com.aispeech.dui.dds.agent.vprint.VprintEngine;
import com.aispeech.dui.dds.agent.vprint.VprintListener;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;
import com.aispeech.h5demo.test.BaseTestActivity;
import com.aispeech.h5demo.test.ListItem;
import com.aispeech.libbase.export.bean.VprintIntent;

/**
 * config.addConfig(DDSConfig.K_VPRINT_ENABLE, "true");// 是否开启声纹功能
 * config.addConfig(DDSConfig.K_USE_VPRINT_IN_WAKEUP, "true");// 是否开启声纹功能并在唤醒中使用声纹判断
 * config.addConfig(DDSConfig.K_VPRINT_BIN, "/sdcard/vprint.bin");// 声纹资源绝对路径
 */
public class VprintTestActivity extends BaseTestActivity {
    private int mOutChannelNum = 2;// 双路
    private int mTrainNum = 3;// 重启次数
    private String mWord = "ni hao xiao chi";

    @Override
    protected void initData() {
        mData.add(new ListItem<>("单麦声纹", null));
        mData.add(new ListItem<>("双麦声纹", null));
        mData.add(new ListItem<>("声纹监听器", null));
        mData.add(new ListItem<>("注册声纹", null));
        mData.add(new ListItem<>("更新声纹", null));
        mData.add(new ListItem<>("追加声纹", null));
        mData.add(new ListItem<>("使用声纹", null));
        mData.add(new ListItem<>("停止声纹", null));
        mData.add(new ListItem<>("获取声纹模型", null));
        mData.add(new ListItem<>("删除一条声纹", null));
        mData.add(new ListItem<>("删除所有声纹", null));
    }

    @Override
    protected void onItemClick(ListItem item) {
        switch (item.getTitle()) {
            case "单麦声纹":
                setMode(1);
                break;
            case "双麦声纹":
                setMode(2);
                break;
            case "声纹监听器":
                startListener();
                break;
            case "注册声纹":
                regist();
                break;
            case "更新声纹":
                update();
                break;
            case "追加声纹":
                append();
                break;
            case "使用声纹":
                test();
                break;
            case "停止声纹":
                stop();
                break;
            case "获取声纹模型":
                getMode();
                break;
            case "删除一条声纹":
                unregister();
                break;
            case "删除所有声纹":
                unregisterAll();
                break;
        }
    }

    private void setMode(int mode) {
        mOutChannelNum = mode;
    }

    private VprintIntent.Builder getVprintBuilder(VprintIntent.Action action) {
        VprintIntent.Builder builder = new VprintIntent.Builder();
        builder.setAction(action);
        builder.setUserId("dengzi");
        builder.setVprintWord(mWord);
        builder.setTrainNum(mTrainNum);
        if (mOutChannelNum > 1) {
            builder.setOutChannelNum(mOutChannelNum);
        }
        return builder;
    }

    private void regist() {
        try {
            VprintIntent intent = getVprintBuilder(VprintIntent.Action.REGISTER).setSnrThresh(8.67f).create();

            VprintEngine.getInstance().start(intent);
            toMain();
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void update() {
        try {
            VprintIntent intent = getVprintBuilder(VprintIntent.Action.UPDATE).setSnrThresh(8.67f).create();

            VprintEngine.getInstance().start(intent);
            toMain();
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void append() {
        try {
            VprintIntent intent = getVprintBuilder(VprintIntent.Action.APPEND).setSnrThresh(8.67f).create();

            VprintEngine.getInstance().start(intent);
            toMain();
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void test() {
        try {
            VprintIntent.Builder builder = new VprintIntent.Builder();
            builder.setAction(VprintIntent.Action.TEST);
            if (mOutChannelNum > 1) {
                builder.setOutChannelNum(mOutChannelNum);
            }

            VprintIntent intent = builder.create();
            VprintEngine.getInstance().start(intent);
            toMain();
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void stop() {
        try {
            VprintEngine.getInstance().stop();
            toMain();
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void startListener() {
        try {
            VprintEngine.getInstance().setVprintListener(new VprintListener() {
                @Override
                public void onState(String state) {
                    Log.e(TAG, "onState = " + state);
                }

                @Override
                public void onResults(String result) {
                    Log.e(TAG, "onResults = " + result);
                }

                @Override
                public void onError(String error) {
                    Log.e(TAG, "onError = " + error);
                }
            });
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void getMode() {
        try {
            VprintEngine.getInstance().getMode();
            toMain();
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void unregister() {
        try {
            VprintIntent intent = getVprintBuilder(VprintIntent.Action.UNREGISTER).create();

            VprintEngine.getInstance().start(intent);
            toMain();
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void unregisterAll() {
        try {
            VprintIntent intent = new VprintIntent.Builder()
                    .setAction(VprintIntent.Action.UNREGISTER_ALL)
                    .create();
            VprintEngine.getInstance().start(intent);
            toMain();
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

}
