package com.studychen.seenews.util;

import android.app.Activity;

import com.studychen.seenews.R;

/**
 * 改变主题工具类
 * 全局变量可以利用 类静态变量 或 preference
 */
public class ThemeTool {

    public static boolean isDark = false;

    public static void changeTheme(Activity activity) {
        if (isDark) {
            activity.setTheme(R.style.AppThemeDark);
        }
    }
}
