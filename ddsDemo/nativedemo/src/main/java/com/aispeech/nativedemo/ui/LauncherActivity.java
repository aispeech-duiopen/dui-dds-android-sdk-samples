package com.aispeech.nativedemo.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.aispeech.ailog.AILog;
import com.aispeech.dui.dds.DDS;
import com.aispeech.nativedemo.DDSService;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;
import com.aispeech.nativedemo.R;

public class LauncherActivity extends Activity {

    private static final String TAG = "LauncherActivity";
    private AlertDialog mDialog;
    private int mAuthCount = 0;// 授权次数,用来记录自动授权

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        startService(new Intent(this, DDSService.class));
        new Thread() {
            public void run() {
                checkDDSReady();
            }
        }.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 注册一个广播,接收service中发送的dds初始状态广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ddsdemo.intent.action.auth_success");// 认证成功的广播
        intentFilter.addAction("ddsdemo.intent.action.auth_failed");// 认证失败的广播
        registerReceiver(authReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 释放注册的广播
        unregisterReceiver(authReceiver);
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        finish();
    }

    // 检查dds是否初始成功
    public void checkDDSReady() {
        while (true) {
            if (DDS.getInstance().getInitStatus() == DDS.INIT_COMPLETE_FULL ||
                    DDS.getInstance().getInitStatus() == DDS.INIT_COMPLETE_NOT_FULL) {
                try {
                    if (DDS.getInstance().isAuthSuccess()) {
                        gotoMainActivity();
                        break;
                    } else {
                        // 自动授权
                        doAutoAuth();
                    }
                } catch (DDSNotInitCompleteException e) {
                    e.printStackTrace();
                }
                break;
            } else {
                AILog.w(TAG, "waiting  init complete finish...");
            }
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // 跳转到主页面
    private void gotoMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    // 显示授权弹框给用户
    private void showDoAuthDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(LauncherActivity.this);
                builder.setMessage("未授权");
                builder.setPositiveButton("做一次授权", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            DDS.getInstance().doAuth();
                        } catch (DDSNotInitCompleteException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        finish();
                    }
                });

                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                mDialog = builder.create();
                mDialog.show();
            }
        });
    }

    // 认证广播
    private BroadcastReceiver authReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(intent.getAction(), "ddsdemo.intent.action.auth_success")) {
                gotoMainActivity();
            } else if (TextUtils.equals(intent.getAction(), "ddsdemo.intent.action.auth_failed")) {
                doAutoAuth();
            }
        }
    };

    // 执行自动授权
    private void doAutoAuth(){
        // 自动执行授权5次,如果5次授权失败之后,给用户弹提示框
        if (mAuthCount < 5) {
            try {
                DDS.getInstance().doAuth();
                mAuthCount++;
            } catch (DDSNotInitCompleteException e) {
                e.printStackTrace();
            }
        } else {
            showDoAuthDialog();
        }
    }
}
