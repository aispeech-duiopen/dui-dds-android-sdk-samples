package com.aispeech.h5demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aispeech.ailog.AILog;
import com.aispeech.h5demo.observer.DuiCommandObserver;
import com.aispeech.h5demo.observer.DuiMessageObserver;
import com.aispeech.h5demo.observer.DuiNativeApiObserver;
import com.aispeech.h5demo.observer.DuiUpdateObserver;
import com.aispeech.h5demo.webview.HybridWebViewClient;
import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements DuiUpdateObserver.UpdateCallback, DuiMessageObserver.MessageCallback {
    private final String TAG = "MainActivity";
    private WebView mWebview;
    private RelativeLayout mWebContainer;
    private MyReceiver mInitReceiver;// 初始化监听广播
    private TextView mInputTv;// 下面的状态展示textview
    private boolean mIsActivityShowing = false;// 当前页面是否可见
    private boolean mLoadedTotally = false;// 页面加载进度
    private DuiMessageObserver mMessageObserver = new DuiMessageObserver();// 消息监听器
    private DuiUpdateObserver mUpdateObserver = new DuiUpdateObserver();// dds更新监听器
    private DuiCommandObserver mCommandObserver = new DuiCommandObserver();// 命令监听器
    private DuiNativeApiObserver mNativeApiObserver = new DuiNativeApiObserver();// 本地方法回调监听器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 添加一个初始成功的广播监听器
        IntentFilter filter = new IntentFilter();
        filter.addAction("ddsdemo.intent.action.init_complete");
        mInitReceiver = new MyReceiver();
        registerReceiver(mInitReceiver, filter);

        initView();
        // 开启PhoneCallService服务,上传本地通讯录
        startService(new Intent(MainActivity.this, PhoneCallService.class));
    }

    // 初始化组件
    private void initView() {
        mInputTv = (TextView) this.findViewById(R.id.input_tv);
        mInputTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    DDS.getInstance().getAgent().avatarClick();
                } catch (DDSNotInitCompleteException e) {
                    e.printStackTrace();
                }
            }
        });

        mWebContainer = (RelativeLayout) this.findViewById(R.id.main_web_container);
        setWebView();
        mWebContainer.addView(mWebview, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams
                .MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void onStart() {
        mIsActivityShowing = true;
        mUpdateObserver.regist(this);
        mCommandObserver.regist();
        mNativeApiObserver.regist();
        super.onStart();
    }

    @Override
    protected void onStop() {
        AILog.d(TAG, "onStop() " + this.hashCode());
        mIsActivityShowing = false;
        super.onStop();
    }

    @Override
    protected void onResume() {
        mMessageObserver.regist(this);
        refreshTv("等待唤醒...");
        enableWakeup();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mMessageObserver.unregist();
        refreshTv("等待唤醒...");
        disableWakeup();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        MainActivity.this.finish();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        unregisterReceiver(mInitReceiver);
        mWebContainer.removeAllViews();
        mWebview.removeAllViews();
        mWebview.destroy();
        stopService();
        mUpdateObserver.unregist();
        mCommandObserver.unregist();
        mNativeApiObserver.unregist();
    }

    // 更新 tv状态
    private void refreshTv(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mInputTv.setText(text);
            }
        });
    }

    // 停止service, 释放dds组件
    private void stopService() {
        Intent intent = new Intent(MainActivity.this, DDSService.class);
        stopService(intent);
    }

    // 设置网页
    private void setWebView() {
        mWebview = new WebView(getApplicationContext());
        mWebview.setWebViewClient(new HybridWebViewClient(this));
        mWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                Log.d(TAG, "view " + view + " progress " + newProgress + " mLoadedTotally " + mLoadedTotally);
                if (newProgress == 100 && !mLoadedTotally) {
                    mLoadedTotally = true;
                    sendHiMessage();
                }
            }
        });
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.setBackgroundColor(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mWebview.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }
        try {
            loadUI(DDS.getInstance().getAgent().getValidH5Path());
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    // 加载h5
    void loadUI(String h5UiPath) {
        Log.d(TAG, "loadUI " + h5UiPath);
        String url = h5UiPath;
        mLoadedTotally = false;
        mWebview.loadUrl(url);
    }

    // 打开唤醒，调用后才能语音唤醒
    void enableWakeup() {
        try {
            DDS.getInstance().getAgent().getWakeupEngine().enableWakeup();
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    // 关闭唤醒, 调用后将无法语音唤醒
    void disableWakeup() {
        try {
            DDS.getInstance().getAgent().stopDialog();
            DDS.getInstance().getAgent().getWakeupEngine().disableWakeup();
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    // dds初始化成功之后,展示一个打招呼消息,告诉用户可以开始使用
    public void sendHiMessage() {
        Log.d(TAG, "sendHiMessage");
        String[] wakeupWords = new String[0];
        String minorWakeupWord = null;
        try {
            wakeupWords = DDS.getInstance().getAgent().getWakeupEngine().getWakeupWords();
            minorWakeupWord = DDS.getInstance().getAgent().getWakeupEngine().getMinorWakeupWord();
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
        String hiStr = "";
        if (wakeupWords != null && minorWakeupWord != null) {
            hiStr = getString(R.string.hi_str2, wakeupWords[0], minorWakeupWord);
        } else if (wakeupWords != null && wakeupWords.length == 2) {
            hiStr = getString(R.string.hi_str2, wakeupWords[0], wakeupWords[1]);
        } else if (wakeupWords != null && wakeupWords.length > 0) {
            hiStr = getString(R.string.hi_str, wakeupWords[0]);
        }
        JSONObject output = new JSONObject();
        try {
            output.put("text", hiStr);
            DDS.getInstance().getAgent().getBusClient().publish("context.output.text", output.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String name = intent.getAction();
            if (name.equals("ddsdemo.intent.action.init_complete")) {
                try {
                    enableWakeup();
                    refreshTv("等待唤醒...");
                    loadUI(DDS.getInstance().getAgent().getValidH5Path());
                } catch (DDSNotInitCompleteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // DuiMessageObserver中当前状态的回调
    @Override
    public void onState(String state) {
        switch (state) {
            case "avatar.silence":
                refreshTv("等待唤醒...");
                break;
            case "avatar.listening":
                refreshTv("监听中...");
                break;
            case "avatar.understanding":
                refreshTv("理解中...");
                break;
            case "avatar.speaking":
                refreshTv("播放语音中...");
                break;
        }
    }

    // DuiUpdateObserver的更新dds回调
    @Override
    public void onUpdate(final int type, String result) {
        if (!mIsActivityShowing) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (type == DuiUpdateObserver.START) {
                    mInputTv.setEnabled(false);
                } else if (type == DuiUpdateObserver.FINISH) {
                    mInputTv.setEnabled(true);
                }
            }
        });
        refreshTv(result);
    }

}
