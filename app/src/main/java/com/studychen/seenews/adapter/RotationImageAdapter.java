package com.studychen.seenews.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.studychen.seenews.model.ItemArticle;
import com.facebook.drawee.view.SimpleDraweeView;
import com.studychen.seenews.model.SimpleArticleItem;
import com.studychen.seenews.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * 头部轮播图片的适配类
 * Created by tomchen on 2015/8/28.
 */
public class RotationImageAdapter extends PagerAdapter {

    private Context context;
    private List<SimpleArticleItem> articles;
    private List<SimpleDraweeView> sdvs = new ArrayList<SimpleDraweeView>();

    public RotationImageAdapter(Context context, List<SimpleArticleItem> articles) {
        this.context = context;
        if (articles == null || articles.size() == 0) {
            this.articles = new ArrayList<>();
        } else {
            this.articles = articles;
        }

        for (int i = 0; i < articles.size(); i++) {
            SimpleDraweeView sdv = new SimpleDraweeView(context);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            sdv.setLayoutParams(layoutParams);
            sdv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Uri uri = Uri.parse(Constant.BUCKET_HOST_NAME + articles.get(i).getImageUrls()[0]);
            sdv.setImageURI(uri);
            sdvs.add(sdv);
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(sdvs.get(position));
        return sdvs.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(sdvs.get(position));
    }


    @Override
    public int getCount() {
        return articles.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }
}
