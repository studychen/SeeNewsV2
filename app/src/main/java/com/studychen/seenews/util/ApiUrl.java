package com.studychen.seenews.util;

import java.util.Random;

/**
 * Created by tomchen on 2/19/16.
 * 手机端内容的 url
 * 分为测试和新浪云
 */
public class ApiUrl {
    // 新浪云 ip，外网使用
    public static final String saeIP = "http://javanews.applinzi.com/";
    // 本地局域网 ip，测试使用
    public static final String localIP = "http://192.168.199.133:8080/seenewsv2/";
    private static final Random random = new Random(47);
    private static final boolean isCloud = false;

    public static String columnUrl() {
        String suffix = "columnWithSql?column=%d&offset=%d";
        if (isCloud)
            return saeIP + suffix;
        else
            return localIP + suffix;
    }

    public static String articleUrl() {
        String suffix = "articleWithSql?column=%d&id=%d";
        if (isCloud)
            return saeIP + suffix;
        else
            return localIP + suffix;
    }

    /**
     * 首页的轮播图片
     *
     * @return
     */
    public static String rotationUrl() {
        String suffix = "rotationWithSql";
        if (isCloud)
            return saeIP + suffix;
        else
            return localIP + suffix;
    }

    /**
     * 首页的轮播图片
     *
     * @return
     */
    public static String randomImageUrl() {

        return Constant.RANDOM_IMAGE + random.nextInt(Constant.COUNT_IMAGE);
    }


}
