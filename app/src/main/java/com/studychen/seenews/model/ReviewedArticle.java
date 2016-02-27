package com.studychen.seenews.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by tomchen on 2/26/16.
 */

//App 阅读新闻的 访问历史
@Table(name = "reviewed")
public class ReviewedArticle extends Model {
    @Column(name = "type")
    public int type;

    @Column(name = "aid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public int aid;


    @Column(name = "photo_key")
    public String photoKey;

    @Column(name = "title")
    public String title;

    //阅读时间
    @Column(name = "click_time")
    public long clickTime;


    public ReviewedArticle() {
        super();
    }


    @Override
    public String toString() {
        return "ReviewedArticle{" +
                "type=" + type +
                ", aid=" + aid +
                ", photoKey='" + photoKey + '\'' +
                ", title='" + title + '\'' +
                ", clickTime=" + clickTime +
                '}';
    }
}
