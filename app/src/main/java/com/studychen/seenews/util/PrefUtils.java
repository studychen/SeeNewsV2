package com.studychen.seenews.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.studychen.seenews.app.SeeNewsApp;

/**
 * SharedPreferences工具类
 * 保存 夜间模式
 * Created by tomchen on 2/3/16.
 */
public class PrefUtils {

    //日间或者夜间模式
    private static final String PRE_THEME_MODE = "dark_mode";

    //省流量模式 这儿和R.string.save_net_mode相同
    private static final String PRE_SAVE_NET_MODE = "save_net_mode";

    private static final String PRE_NAME = "com.studychen.seenews";

    private static SharedPreferences getSharedPreferences() {
        return SeeNewsApp.getContext()
                .getSharedPreferences(PRE_NAME, Context.MODE_PRIVATE);
    }

    public static boolean isDarkMode() {
        return getSharedPreferences().getBoolean(PRE_THEME_MODE, false);
    }

    /**
     * 夜间模式
     *
     * @param isDarkMode true为夜间模式
     */
    public static void setDarkMode(boolean isDarkMode) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(PRE_THEME_MODE, isDarkMode);
        editor.commit();
    }

    /**
     * 省流量模式
     *
     * @return
     */
    public static boolean isSaveNetMode() {
        return getSharedPreferences().getBoolean(PRE_SAVE_NET_MODE, false);
    }

    /**
     * @param isSaveNetMode true为省流量模式，不加载图片
     */
    public static void setSaveNetMode(boolean isSaveNetMode) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(PRE_SAVE_NET_MODE, isSaveNetMode);
        editor.commit();
    }

    /**
     * 删除 SharedPreferences 的某个 key
     *
     * @param key
     */
    public static void removeFromPrefs(String key) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.remove(key);
        editor.commit();
    }
}