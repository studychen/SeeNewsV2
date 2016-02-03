package com.studychen.seenews.model;

/**
 * Created by tomchen on 1/10/16.
 * 新闻类，这是在 RecycleView 使用的新闻 javabean
 * 还有一个新闻详情javabean
 */
public class ItemArticle {
    private int index;
    private String imageUrl;
    private String title;
    private String publishDate;
    private int readTimes;
    private String source;
    private String body;

    public ItemArticle(int index, String imageUrl) {
        this.index = index;
        this.imageUrl = imageUrl;
    }

    public ItemArticle(int index, String imageUrl, String title, String publishDate, int readTimes, String source, String preview) {
        this.index = index;
        this.imageUrl = imageUrl;
        this.title = title;
        this.publishDate = publishDate;
        this.readTimes = readTimes;
        this.source = source;
        this.body = preview;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getReadTimes() {
        return readTimes;
    }

    public void setReadTimes(int readTimes) {
        this.readTimes = readTimes;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "ItemArticle{" +
                "index=" + index +
                ", imageUrl='" + imageUrl + '\'' +
                ", title='" + title + '\'' +
                ", publishDate='" + publishDate + '\'' +
                ", readTimes=" + readTimes +
                ", source='" + source + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
