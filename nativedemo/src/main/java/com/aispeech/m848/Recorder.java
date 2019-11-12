package com.aispeech.m848;

import android.media.MediaRecorder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.aispeech.AIAudioRecord;
import com.aispeech.ailog.AILog;
import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.nodes.RecorderExNode;

import java.util.Arrays;

public class Recorder {

    private static final String TAG = "Recorder";

    private static final int RECORD_INTERVAL = 100;

    private AIAudioRecord mRecorder = null;
    private byte mReadBuffer[];

    private boolean isStopped = false;
    private boolean isStartCalled = false;

    private HandlerThread mHandlerThread = new HandlerThread("recorder");
    private RecorderHandler mRecorderHandler;

    private static final int MSG_START_RECORDING = 0;
    private static final int MSG_STOP_RECORDING = 1;

    private static Recorder mInstance;

    public synchronized static Recorder getInstance() {
        if (mInstance == null) {
            mInstance = new Recorder();
        }

        return mInstance;
    }

    private Recorder() {
        mHandlerThread.start();
        mRecorderHandler = new RecorderHandler(mHandlerThread.getLooper());
    }

    private void prepareConfig() {
        int mReadBufferSize = 16000 * 6 * 2 * 100 / 1000;
        if (mReadBuffer == null || mReadBuffer.length != mReadBufferSize) {
            mReadBuffer = new byte[mReadBufferSize];
        }
    }

    public void start() {
        isStartCalled = true;
        startRecording();
    }

    public void stop() {
        if (isStartCalled) {
            stopRecording();
            isStartCalled = false;
        }
    }

    public void release() {
        stop();
        mHandlerThread.quit();
    }

    public boolean isStarted() {
        return isStartCalled;
    }

    private void startRecording() {
        if (isStopped) {
            mRecorderHandler.sendEmptyMessage(MSG_STOP_RECORDING);
            isStopped = false;
            return;
        }

        if (null == mRecorder) {
            prepareConfig();
            mRecorder = new AIAudioRecord();
            mRecorder._native_setup(MediaRecorder.AudioSource.VOICE_RECOGNITION, 16000, 6);
            mRecorder._native_start();
        }

        int size = mRecorder._native_read_in_byte_array(mReadBuffer, 0, mReadBuffer.length);
        if (size > 0) {
            byte[] publishBuffer = Arrays.copyOfRange(mReadBuffer, 0, size);
            if (DDS.getInstance().getInitStatus() == DDS.INIT_COMPLETE_FULL) {
                AILog.i(TAG, "recorded size -> " + publishBuffer.length);
                RecorderExNode.feedPcm(publishBuffer);
            }
//                TestEngine.getInstance().feed(publishBuffer);
            mRecorderHandler.sendEmptyMessage(MSG_START_RECORDING);
        } else {
            mRecorderHandler.sendEmptyMessageDelayed(MSG_START_RECORDING, 1000);
        }
    }

    private void releaseRecorder() {
        if (null != mRecorder) {
            mRecorder._native_stop();
            mRecorder = null;
        }
    }

    private void stopRecording() {
        isStopped = true;
    }

    private class RecorderHandler extends Handler {

        RecorderHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_START_RECORDING:
                    startRecording();
                    break;
                case MSG_STOP_RECORDING:
                    releaseRecorder();
                    break;
                default:
                    break;
            }
        }
    }
}
