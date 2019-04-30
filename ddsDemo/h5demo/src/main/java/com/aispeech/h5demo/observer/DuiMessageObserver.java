package com.aispeech.h5demo.observer;

import android.util.Log;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.agent.MessageObserver;

/**
 * 客户端MessageObserver, 用于处理客户端动作的消息响应.
 */
public class DuiMessageObserver implements MessageObserver {
    private final String Tag = "DuiMessageObserver";

    public interface MessageCallback {
        void onState(String state);
    }

    private MessageCallback mMessageCallback;
    private String[] mSubscribeKeys = new String[]{"sys.dialog.state",};

    // 注册当前更新消息
    public void regist(MessageCallback messageCallback) {
        mMessageCallback = messageCallback;
        DDS.getInstance().getAgent().subscribe(mSubscribeKeys, this);
    }

    // 注销当前更新消息
    public void unregist() {
        DDS.getInstance().getAgent().unSubscribe(this);
    }


    @Override
    public void onMessage(String message, String data) {
        Log.d(Tag, "message : " + message + " data : " + data);
        switch (message) {
            case "sys.dialog.state":
                if (mMessageCallback != null) {
                    mMessageCallback.onState(data);
                }
                break;
        }
    }

}
