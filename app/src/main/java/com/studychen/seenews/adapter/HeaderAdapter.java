package com.studychen.seenews.adapter;

import android.app.Activity;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.studychen.seenews.model.ItemArticle;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomchen on 1/16/16.
 */
public class HeaderAdapter extends PagerAdapter {
    private static final String LOG = "NEWS_LOG";

    private Activity context;
    private List<ItemArticle> articles;
    private List<SimpleDraweeView> images = new ArrayList<SimpleDraweeView>();


    public HeaderAdapter(Activity context, List<ItemArticle> articles) {
        this.context = context;
        if (articles == null || articles.size() == 0) {
            this.articles = new ArrayList<>();
        } else {
            this.articles = articles;
        }

        for (int i = 0; i < articles.size(); i++) {
            SimpleDraweeView image = new SimpleDraweeView(context);
            Uri uri = Uri.parse(articles.get(i).getImageUrl());
            image.setImageURI(uri);
            images.add(image);
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(images.get(position));
        return images.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(images.get(position));
    }

    @Override
    public int getCount() {
        return articles.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        Log.i(LOG, "in isViewFromObject view: " + view + " object: "
                + object + " equal: " + (view == object));
        return view == object;
    }
}
