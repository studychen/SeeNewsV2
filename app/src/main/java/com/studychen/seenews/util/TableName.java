package com.studychen.seenews.util;

public class TableName {
    public static final String LATEST = "latest";// 最新消息
    public static final String NOTIFIC = "notific";// 校园通知
    public static final String BACHELOR = "bachelor";// 本科教学 学士
    public static final String MASTER = "master";// 研究生 硕士
    public static final String ACADEMIC = "academic";// 学术交流
    public static final String JOB = "job";// 就业招聘

    /**
     *
     * 这儿的设计能不能更优雅
     * 0是最新消息表，1是校园通知表
     *
     * @param type 获取对应的表名称
     * @return
     */
    public static String getTableByType(int type) {
        switch (type) {
            case ColumnType.LATEST:
                return LATEST;
            case ColumnType.NOTIFIC:
                return NOTIFIC;
            case ColumnType.BACHELOR:
                return BACHELOR;
            case ColumnType.MASTER:
                return MASTER;
            case ColumnType.ACADEMIC:
                return ACADEMIC;
            case ColumnType.JOB:
                return JOB;
            default:
                return LATEST;
        }
    }
}
