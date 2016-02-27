package com.studychen.seenews.util;

import android.content.Context;

/**
 * Created by tomchen on 2/26/16.
 */
public class GetTimeAgo {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;

    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    /**
     * 按照毫秒来存储
     *
     * @param time
     * @return
     */
    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return "未知时间";
        }

        final long diff = now - time;

        if (diff < MINUTE_MILLIS) {
            return "刚刚";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "1分钟前";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + "分钟前";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "1小时前";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + "小时前";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "昨天";
        } else {
            return diff / DAY_MILLIS + "天前";
        }
    }
}
