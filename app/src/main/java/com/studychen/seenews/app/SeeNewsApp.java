package com.studychen.seenews.app;

import android.app.Application;
import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.orhanobut.logger.Logger;
import com.studychen.seenews.util.Constant;

import cn.jpush.android.api.JPushInterface;

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

        Logger.init(Constant.LOGGER_TAG);

        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
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
