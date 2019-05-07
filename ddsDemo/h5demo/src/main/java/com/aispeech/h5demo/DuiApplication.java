package com.aispeech.h5demo;

import android.app.Application;
import android.content.Context;

/**
 * Created by yrl on 17-8-28.
 */
public class DuiApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext() {
        if (mContext == null) {
            throw new RuntimeException("Unknown Error");
        }
        return mContext;
    }
}
