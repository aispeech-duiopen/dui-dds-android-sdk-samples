package com.aispeech.h5demo.observer;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.aispeech.h5demo.DuiApplication;
import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dsk.duiwidget.CommandObserver;

import org.json.JSONObject;

/**
 * 客户端CommandObserver, 用于处理客户端动作的执行以及快捷唤醒中的命令响应.
 * 例如在平台配置客户端动作： command://call?phone=$phone$&name=#name#,
 * 那么在CommandObserver的onCall方法中会回调topic为"call", data为
 */
public class DuiCommandObserver implements CommandObserver {
    private String TAG = "DuiCommandObserver";
    private static final String COMMAND_CALL = "sys.action.call";
    private static final String COMMAND_SELECT = "sys.action.call.select";
    private String mSelectedPhone = null;

    public DuiCommandObserver() {
    }

    // 注册当前更新消息
    public void regist() {
        DDS.getInstance().getAgent().subscribe(new String[]{COMMAND_CALL, COMMAND_SELECT},
                this);
    }

    // 注销当前更新消息
    public void unregist() {
        DDS.getInstance().getAgent().unSubscribe(this);
    }

    @Override
    public void onCall(String command, String data) {
        Log.e(TAG, "command: " + command + "  data: " + data);
        try {
            if (COMMAND_CALL.equals(command)) {
                String number = new JSONObject(data).optString("phone");
                if (number == null) {
                    phoneDial(mSelectedPhone);
                    mSelectedPhone = null;
                } else {
                    phoneDial(number);
                }
            } else if (COMMAND_SELECT.equals(command)) {
                mSelectedPhone = new JSONObject(data).optString("phone");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 拨打电话
    private void phoneDial(String number) {
        if (number == null) {
            return;
        }
        Log.e(TAG, "phoneDial:" + number);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("tel:" + number));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        DuiApplication.getContext().startActivity(intent);
    }

}
