package com.studychen.seenews.db;

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

    @Column(name = "aid")
    public int id;

    @Column(name = "title")
    public String title;

    @Column(name = "date")
    public String date;

    @Column(name = "read")
    public int read;

    public ReviewedArticle() {
        super();
    }

    @Override
    public String toString() {
        return "ReviewedArticle{" +
                "type=" + type +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", read=" + read +
                '}';
    }
}
