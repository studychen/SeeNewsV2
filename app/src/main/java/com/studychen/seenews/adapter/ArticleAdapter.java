package com.studychen.seenews.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.studychen.seenews.model.ItemArticle;
import com.facebook.drawee.view.SimpleDraweeView;
import com.studychen.seenews.R;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 新闻列表的适配器
 * 01-14 头部是 ViewPager，下面是列表新闻
 * Created by tomchen on 1/11/16.
 */
public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    //头部固定为 张图片
    private static final int NUM_IMAGE = 4;

    //Handler 用到的参数值
    private static final int UPTATE_VIEWPAGER = 0;

    //新闻列表
    private List<ItemArticle> articleList;

    //设置当前 第几个图片 被选中
    private int currentIndex = 0;

    //context
    private Context context;

    private LayoutInflater mLayoutInflater;

    private ImageView[] mCircleImages;//底部只是当前页面的小圆点


    public ArticleAdapter(Context context, List<ItemArticle> articleList) {
        this.context = context;

        //头部viewpager图片固定是7张，剩下的是列表的数据
        this.articleList = articleList;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //理论上应该把最可能返回的 TYPE 放在前面
        View view = null;

        if (viewType == TYPE_ITEM) {
            view = mLayoutInflater.inflate(
                    R.layout.viewholder_article_item, parent, false);
            return new ItemArticleViewHolder(view);
        }
        //头部返回 ViewPager 实现的轮播图片
        if (viewType == TYPE_HEADER) {
            view = mLayoutInflater.inflate(
                    R.layout.viewholder_article_header, parent, false);
            return new HeaderArticleViewHolder(view);
        }

        return null;
//        //可以抛出异常，没有对应的View类型
//        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemArticleViewHolder) {
            //转型
            ItemArticleViewHolder newHolder = (ItemArticleViewHolder) holder;
            //注意RecyclerView第0项是 ViewPager 占据了0 1 2 3图片
            //那么下面的列表展示是 RecyclerView 的第1项，从第4项开始
            ItemArticle article = articleList.get(position + NUM_IMAGE - 1);
            newHolder.rcvArticlePhoto.setImageURI(Uri.parse(article.getImageUrl()));
            newHolder.rcvArticleTitle.setText(article.getTitle());
            newHolder.rcvArticleDate.setText(article.getPublishDate());
            newHolder.rcvArticleSource.setText(article.getSource());
            //注意这个阅读次数是 int 类型，需要转化为 String 类型
            newHolder.rcvArticleReadtimes.setText(article.getReadTimes() + "次");
            newHolder.rcvArticlePreview.setText(article.getBody());
        } else if (holder instanceof HeaderArticleViewHolder) {
            HeaderArticleViewHolder newHolder = (HeaderArticleViewHolder) holder;

            List<ItemArticle> headers = articleList.subList(0, NUM_IMAGE );
            HeaderImageAdapter imageAdapter = new HeaderImageAdapter(context, headers);

            setUpViewPager(newHolder.vpHottest, newHolder.llHottestIndicator, headers);

        }
    }


    private void setUpViewPager(final ViewPager vp, LinearLayout llBottom, final List<ItemArticle> headerArticles) {
        HeaderImageAdapter imageAdapter = new HeaderImageAdapter(context, headerArticles);
        //??这儿有些疑惑，Adapter 里面嵌套设置 Adapter 是否优雅？
        vp.setAdapter(imageAdapter);

        final Handler mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case UPTATE_VIEWPAGER:
                        if (msg.arg1 != 0) {
                            vp.setCurrentItem(msg.arg1);
                        } else {
                            //false 当从末页调到首页是，不显示翻页动画效果，
                            vp.setCurrentItem(msg.arg1, false);
                        }
                        break;
                }
            }
        };

        //下面是设置动画切换的样式
        vp.setPageTransformer(true, new RotateUpTransformer());

        //创建底部指示位置的导航栏
        final ImageView[] mCircleImages = new ImageView[headerArticles.size()];

        for (int i = 0; i < mCircleImages.length; i++) {
            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10, 10);
            params.setMargins(5, 0, 5, 0);
            imageView.setLayoutParams(params);
            if (i == 0) {
                imageView.setBackgroundResource(R.drawable.indicator_select);
            } else {
                imageView.setBackgroundResource(R.drawable.indicator_not_select);
            }

            mCircleImages[i] = imageView;
            //把指示作用的原点图片加入底部的视图中
            llBottom.addView(mCircleImages[i]);

        }

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //图片左右滑动时候，将当前页的圆点图片设为选中状态
            @Override
            public void onPageSelected(int position) {
                // 一定几个图片，几个圆点，但注意是从0开始的
                int total = mCircleImages.length;
                for (int j = 0; j < total; j++) {
                    if (j == position) {
                        mCircleImages[j].setBackgroundResource(R.drawable.indicator_select);
                    } else {
                        mCircleImages[j].setBackgroundResource(R.drawable.indicator_not_select);
                    }
                }

                //设置全局变量，currentIndex为选中图标的 index
                currentIndex = position;
            }

            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

                //实现切换到末尾后返回到第一张
                switch (state) {
                    // 手势滑动
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        break;

                    // 界面切换中
                    case ViewPager.SCROLL_STATE_SETTLING:
                        break;

                    case ViewPager.SCROLL_STATE_IDLE:// 滑动结束，即切换完毕或者加载完毕
                        // 当前为最后一张，此时从右向左滑，则切换到第一张
                        if (vp.getCurrentItem() == vp.getAdapter()
                                .getCount() - 1) {
                            vp.setCurrentItem(0, false);
                        }
                        // 当前为第一张，此时从左向右滑，则切换到最后一张
                        else if (vp.getCurrentItem() == 0) {
                            vp.setCurrentItem(vp.getAdapter()
                                    .getCount() - 1, false);
                        }
                        break;

                    default:
                        break;
                }
            }
        });


        //设置自动轮播图片，5s后执行，周期是5s

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = UPTATE_VIEWPAGER;
                if (currentIndex == headerArticles.size() - 1) {
                    currentIndex = -1;
                }
                message.arg1 = currentIndex + 1;
                mHandler.sendMessage(message);
            }
        }, 6000, 6000);
    }

    @Override
    public int getItemCount() {
        //因为多了一个头部，所以是+1,但是头部 ViewPager 占了7个
        //所以实际是少了6个
        return articleList.size() + 1 - NUM_IMAGE;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;
        else
            return TYPE_ITEM;
    }


    class HeaderArticleViewHolder extends RecyclerView.ViewHolder {

        //轮播的最热新闻图片
        @InjectView(R.id.vp_hottest)
        ViewPager vpHottest;
        //轮播图片下面的小圆点
        @InjectView(R.id.ll_hottest_indicator)
        LinearLayout llHottestIndicator;

        //学院广播信息
        @InjectView(R.id.tv_college_broadcast)
        TextView tvCollegeBroadcast;

        public HeaderArticleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    class ItemArticleViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.rcv_article_photo)
        SimpleDraweeView rcvArticlePhoto;
        @InjectView(R.id.rcv_article_title)
        TextView rcvArticleTitle;
        @InjectView(R.id.rcv_article_date)
        TextView rcvArticleDate;
        @InjectView(R.id.rcv_article_source)
        TextView rcvArticleSource;
        @InjectView(R.id.rcv_article_readtimes)
        TextView rcvArticleReadtimes;
        @InjectView(R.id.rcv_article_preview)
        TextView rcvArticlePreview;

        public ItemArticleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }


}
