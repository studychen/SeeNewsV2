package com.studychen.seenews;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.studychen.seenews.util.ThemeTool;
import com.readystatesoftware.systembartint.SystemBarTintManager;


/**
 * Created by zjq on 15/4/2.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        // 创建状态栏的管理实例
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // 激活状态栏设置
        tintManager.setStatusBarTintEnabled(true);
        // 激活导航栏设置
        tintManager.setNavigationBarTintEnabled(true);
        // 设置一个颜色给系统栏
        if (ThemeTool.isDark) {
            tintManager.setTintColor(getResources().getColor(R.color.colorPrimaryDarkDarkTheme));
        } else {
            tintManager.setTintColor(getResources().getColor(R.color.primary_dark));
        }
        ThemeTool.changeTheme(this);
    }

    /**
     * 通过Id得到view的实例
     *
     * @param viewId
     * @param <T>
     * @return
     */
    protected <T> T findView(int viewId) {
        return (T) findViewById(viewId);
    }

    /**
     * toast消息
     *
     * @param msg
     */
    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 弹出对话框
     *
     * @param msg
     */
    protected void showDialog(String msg) {

    }

    /**
     * 关闭对话框
     */
    protected void dismissDialog() {

    }

    /**
     * 通过类名启动activity
     *
     * @param clazz
     */
    protected void openActivity(Class<?> clazz) {
        openActivity(clazz, null);
    }

    /**
     * 通过类名启动activity
     *
     * @param context
     * @param clazz
     */
    protected void openActivity(Context context, Class<?> clazz) {
        Intent intent = new Intent(context, clazz);
        openActivity(intent);
    }

    /**
     * 通过类名带参启动Activity
     *
     * @param clazz
     * @param bundle
     */
    protected void openActivity(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        openActivity(intent);
    }

    /**
     * 启动Activity
     *
     * @param intent
     */
    protected void openActivity(Intent intent) {
        startActivity(intent);
    }

    /**
     * 通过action名启动activity
     *
     * @param action
     */
    protected void openActivity(String action) {
        openActivity(action, null);
    }

    /**
     * 通过action名带参启动activity
     *
     * @param action
     * @param bundle
     */
    protected void openActivity(String action, Bundle bundle) {
        Intent intent = new Intent(action);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        openActivity(intent);
    }
}
