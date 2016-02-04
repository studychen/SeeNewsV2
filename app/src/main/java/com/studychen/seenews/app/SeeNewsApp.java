package com.studychen.seenews.app;

import android.app.Application;
import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * 全局应用程序上下文
 * 方便 Preference 或 Sqlite 获取 Context
 */

public class SeeNewsApp extends Application {


    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;
        //Fresco是facebook的android图片处理库
        Fresco.initialize(getApplicationContext());

        //初始化ActiveAndroid 方便操作Sqlite
        ActiveAndroid.initialize(this);
    }

    /**
     * clear 类库
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        Fresco.shutDown();
        ActiveAndroid.dispose();
    }
}
