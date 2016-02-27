package com.studychen.seenews.model;

/**
 * 首页轮播图片 javabean
 * @author tomchen
 *
 */
public class RotationItem {

	int id;
	// 首页只有一张图片
	String imageUrl;
	String title;
	// type 是数字 1表示新闻通知 2本科教学 见 ColumnType 
	int type;

	 
	public RotationItem(int id, String imageUrl, String title, int type) {
		this.id = id;
		this.imageUrl = imageUrl;
		this.title = title;
		this.type = type;
	}

	
	@Override
	public String toString() {
		return "RotationItem [aid=" + id + ", imageUrl=" + imageUrl + ", title=" + title + ", type=" + type + "]";
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}


}
