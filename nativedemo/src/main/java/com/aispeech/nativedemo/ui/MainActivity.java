package com.aispeech.nativedemo.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.aispeech.ailog.AILog;
import com.aispeech.dui.dds.DDS;
import com.aispeech.nativedemo.DDSService;
import com.aispeech.nativedemo.PhoneCallService;
import com.aispeech.nativedemo.R;
import com.aispeech.nativedemo.bean.MessageBean;
import com.aispeech.nativedemo.observer.DuiCommandObserver;
import com.aispeech.nativedemo.observer.DuiMessageObserver;
import com.aispeech.nativedemo.observer.DuiNativeApiObserver;
import com.aispeech.nativedemo.observer.DuiUpdateObserver;
import com.aispeech.nativedemo.test.MainTestActivity;
import com.aispeech.nativedemo.ui.adapter.DialogAdapter;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements DuiUpdateObserver.UpdateCallback, DuiMessageObserver.MessageCallback {
    public static final String TAG = "MainActivity";

    private Handler mHandler = new Handler();
    private TextView mInputTv;// 下面的状态展示textview
    private RecyclerView mRecyclerView;// 列表展示控件
    private DialogAdapter mDialogAdapter;  // 各种UI控件的实现在DialogAdapter类里
    private boolean mIsActivityShowing = false;// 当前页面是否可见
    private MyReceiver mInitReceiver;// 初始化监听广播
    private LinkedList<MessageBean> mMessageList = new LinkedList<>();// 当前消息容器
    private DuiMessageObserver mMessageObserver = new DuiMessageObserver();// 消息监听器
    private DuiCommandObserver mCommandObserver = new DuiCommandObserver();// 命令监听器
    private DuiNativeApiObserver mNativeApiObserver = new DuiNativeApiObserver();// 本地方法回调监听器
    private DuiUpdateObserver mUpdateObserver = new DuiUpdateObserver();// dds更新监听器

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
    private void initView(){
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

        this.findViewById(R.id.test_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainTestActivity.class);
                intent.putExtra("title", "DDS 测试");
                startActivity(intent);
            }
        });

        mRecyclerView = (RecyclerView) this.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDialogAdapter = new DialogAdapter(mMessageList);
        mRecyclerView.setAdapter(mDialogAdapter);
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
        // 注册消息监听器
        mMessageObserver.regist(this, mMessageList);

        sendHiMessage();
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
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mInitReceiver);
        mUpdateObserver.unregist();
        mCommandObserver.unregist();
        mNativeApiObserver.unregist();
        stopService();
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
        stopService(new Intent(MainActivity.this, DDSService.class));
        stopService(new Intent(MainActivity.this, PhoneCallService.class));
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
        String[] wakeupWords = new String[0];
        String minorWakeupWord = null;
        try {
            // 获取主唤醒词
            wakeupWords = DDS.getInstance().getAgent().getWakeupEngine().getWakeupWords();
            // 获取副唤醒词
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
        Log.e("dengzi", "histr = " + hiStr);
        if (!TextUtils.isEmpty(hiStr)) {
            MessageBean bean = new MessageBean();
            bean.setText(hiStr);
            bean.setType(MessageBean.TYPE_OUTPUT);
            mMessageList.add(bean);
            mDialogAdapter.notifyItemInserted(mMessageList.size());
            mRecyclerView.smoothScrollToPosition(mMessageList.size());
        }
    }

    // 更新ui列表展示
    public void notifyItemInserted() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDialogAdapter.notifyDataSetChanged();
                mRecyclerView.smoothScrollToPosition(mMessageList.size());
            }
        });
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

    // DuiMessageObserver中当前消息的回调
    @Override
    public void onMessage() {
        notifyItemInserted();
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
        DialogAdapter.mState = state;
    }

    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String name = intent.getAction();
            if (!TextUtils.isEmpty(name) && name.equals("ddsdemo.intent.action.init_complete")) {
                enableWakeup();
                refreshTv("等待唤醒...");
                // 此处等待200ms,等待wakeup节点完成初始成功
                // 我们已知此问题,待下一版本我们会将此等待去除,
                // 开发者使用时就认为此处不需要等待即可,dds在后续的升级中会解决此问题
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(200);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sendHiMessage();
                            }
                        });
                    }
                }).start();
            }
        }
    }

}
