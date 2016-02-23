package com.studychen.seenews.util;

/**
 * Created by tomchen on 2/4/16.
 */
public class ColumnType {

    public static final int LATEST = 0;// 最新消息
    public static final int NOTIFIC = 1;// 校园通知
    public static final int BACHELOR = 2;// 本科教学 学士
    public static final int MASTER = 3;// 研究生 硕士
    public static final int ACADEMIC = 5;// 学术交流
    // 选取了电院新闻的部分栏目
    public static final int JOB = 8;// 就业招聘

    /*
    根据positon，得到对应的栏目
    注意 position 是连续的，栏目不是连续
     */
    public static int getType(int position) {
        int[] types = {LATEST, NOTIFIC, BACHELOR, MASTER, ACADEMIC, JOB};
        return types[position];
    }
}
