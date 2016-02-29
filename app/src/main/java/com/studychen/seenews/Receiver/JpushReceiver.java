package com.studychen.seenews.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.studychen.seenews.ui.activity.first.DetailActivity;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by tomchen on 2/29/16.
 */
public class JpushReceiver extends BroadcastReceiver {

    public static final String IS_FROM_JPUSH = "isFromJpush";

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();

        if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            //发送的默认推送消息，做些统计或者其他工作

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            //用户点击后的行为，自定义页面
            //打开自定义的Activity
            Intent i = new Intent(context, DetailActivity.class);

            i.putExtra(IS_FROM_JPUSH, true);
            i.putExtras(bundle);

            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(i);
        }


    }
}
