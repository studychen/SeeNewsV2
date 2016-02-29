package com.studychen.seenews.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.orhanobut.logger.Logger;
import com.studychen.seenews.R;
import com.studychen.seenews.app.SeeNewsApp;
import com.studychen.seenews.util.NetworkUtil;
import com.studychen.seenews.util.PrefUtils;

/**
 * Created by tomchen on 2/28/16.
 */
public class SmartSimpleDraweeView extends SimpleDraweeView {


    public SmartSimpleDraweeView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public SmartSimpleDraweeView(Context context) {
        super(context);
    }

    public SmartSimpleDraweeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmartSimpleDraweeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setImageURI(final Uri uri) {

        Logger.d("isSaveNetMode " + PrefUtils.isSaveNetMode());

        this.getHierarchy()
                .setActualImageScaleType(ScalingUtils.ScaleType.FOCUS_CROP);

        //wifi 情况下，都加载图片
        if (NetworkUtil.isWifiAvailable()) {
            super.setImageURI(uri);
            return;
        }

        //不是省流量模式，采用原来的方法
        if (!PrefUtils.isSaveNetMode()) {
            super.setImageURI(uri);
        } else {
            if (!NetworkUtil.isWifiAvailable() && NetworkUtil.isMobileNetAvailable()) {
                Drawable placeImage;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    placeImage = getResources().getDrawable(R.drawable.click_load_image, SeeNewsApp.getContext().getTheme());
                } else {
                    placeImage = getResources().getDrawable(R.drawable.click_load_image);
                }


                //省流量，设置图片为点击加载
                final GenericDraweeHierarchyBuilder builder =
                        new GenericDraweeHierarchyBuilder(getResources());

                GenericDraweeHierarchy hierarchy = builder
                        .setPlaceholderImage(placeImage)
                        .setActualImageScaleType(ScalingUtils.ScaleType.FOCUS_CROP)
                        .build();
                this.setHierarchy(hierarchy);

                this.setOnClickListener(new View.OnClickListener() {

                    /**
                     * Called when a view has been clicked.
                     *
                     * @param v The view that was clicked.
                     */
                    @Override
                    public void onClick(View v) {
                        SmartSimpleDraweeView.this.getHierarchy()
                                .setProgressBarImage(new ProgressBarDrawable());

                        SmartSimpleDraweeView.this.getHierarchy()
                                .setActualImageScaleType(ScalingUtils.ScaleType.FOCUS_CROP);

                        SmartSimpleDraweeView.super.setImageURI(uri);
                    }
                });
            }

        }
    }
}
