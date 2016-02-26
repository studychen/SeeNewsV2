package com.studychen.seenews.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.facebook.drawee.view.SimpleDraweeView;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.studychen.seenews.R;
import com.studychen.seenews.model.SimpleArticleItem;
import com.studychen.seenews.util.ApiUrl;
import com.studychen.seenews.util.Constant;
import com.studychen.seenews.util.OnItemClickLitener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 新闻列表的适配器
 * 最新栏目的适配器
 * 这个栏目上方有轮播图片
 * 上方是 ViewPager，下面是列表新闻
 * Created by tomchen on 1/11/16.
 */
public class LatestArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final static int TYPE_MULTI_IMAGES = 3; // 多个图片的文章
    public final static int TYPE_FOOTER = 4;//底部--往往是loading_more
    public final static int TYPE_NORMAL = 2; // 正常的一条文章
    private static final int TYPE_ROTATION = 1;
    //Handler 用到的参数值
    private static final int UPTATE_VIEWPAGER = 0;

    //新闻列表
    private List<SimpleArticleItem> articleList;

    //设置当前 第几个图片 被选中
    private int currentIndex = 0;

    //context
    private Context context;

    private LayoutInflater mLayoutInflater;

    private ImageView[] mCircleImages;//底部只是当前页面的小圆点

    private OnItemClickLitener mOnItemClickLitener;//点击 RecyclerView 中的 Item


    /**
     * 注意这儿的 articleList 和原来的articleList 是同一个引用
     * fragment 的文章list增加了数据
     * 这儿的list也增加数据
     *
     * @param context
     * @param articleList
     */
    public LatestArticleAdapter(Context context, List<SimpleArticleItem> articleList) {
        this.context = context;

        if (articleList == null) {
            this.articleList = new ArrayList<SimpleArticleItem>();
        } else {
            //头部viewpager图片固定是7张，剩下的是列表的数据
            this.articleList = articleList;
        }

        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //理论上应该把最可能返回的 TYPE 放在前面

        RecyclerView.ViewHolder vh;
        View view;
        switch (viewType) {

            //其他无法处理的情况使用viewholder_article_simple
            default:
            case TYPE_NORMAL:
                view = mLayoutInflater.inflate(
                        R.layout.item_article_normal, parent, false);
                vh = new ItemArticleViewHolder(view);
                return vh;
            case TYPE_FOOTER:
                Log.i(Constant.LOG, "in TYPE_FOOTER");
                view = mLayoutInflater.inflate(
                        R.layout.recyclerview_footer, parent, false);
                vh = new FooterViewHolder(view);
                return vh;
            case TYPE_MULTI_IMAGES:
                view = mLayoutInflater.inflate(
                        R.layout.item_article_multi_images, parent, false);
                vh = new MultiImagesViewHolder(view);
                return vh;
            case TYPE_ROTATION:
                view = mLayoutInflater.inflate(
                        R.layout.item_article_rotations, parent, false);
                vh = new RotationViewHolder(view);
                return vh;
        }

//        //可以抛出异常，没有对应的View类型
//        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");

    }

    //返回最底的文章id，为了下拉刷新从该id开始加载
    public int getBottomArticleId() {
        if (articleList == null || articleList.size() == 0)
            return -1;
        return articleList.get(articleList.size() - 1).getId();
    }

    //返回最底的文章id，为了下拉刷新从该id开始加载
    public int getTopOriginArticleId() {
        if (articleList == null || articleList.size() == 0)
            return -1;
        return articleList.get(Constant.COUNT_ROTATION).getId();
    }


    /**
     * 当Item 超出屏幕后，就会重新执行onBindViewHolder
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        //这时候 article是 null，先把 footer 处理了
        if (holder instanceof FooterViewHolder) {
            ((FooterViewHolder) holder).rcvLoadMore.spin();
            return;
        }

        //注意RecyclerView第0项是 ViewPager 占据了0 1 2 3 4 5 6 7图片
        //那么下面的列表展示是 RecyclerView 的第1项，从第7项开始
        SimpleArticleItem article = articleList.get(position + Constant.COUNT_ROTATION - 1);
        String[] imageUrls = article.getImageUrls();

        if (holder instanceof ItemArticleViewHolder) {

            //转型
            ItemArticleViewHolder newHolder = (ItemArticleViewHolder) holder;

            if (imageUrls.length == 0) {
                newHolder.rcvArticlePhoto.setImageURI(Uri.parse(ApiUrl.randomImageUrl(article.getId())));

            } else {
                newHolder.rcvArticlePhoto.setImageURI(Uri.parse(Constant.BUCKET_HOST_NAME + article.getImageUrls()[0]));

            }
            newHolder.rcvArticleTitle.setText(article.getTitle());
            newHolder.rcvArticleDate.setText(article.getPublishDate());
            //注意这个阅读次数是 int 类型，需要转化为 String 类型
            newHolder.rcvArticleReadtimes.setText(article.getReadTimes() + "阅");
            newHolder.rcvArticleSummary.setText(article.getSummary());

            // 如果设置了回调，则设置点击事件
            if (mOnItemClickLitener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getLayoutPosition();
                        mOnItemClickLitener.onItemClick(pos);
                    }
                });
            }

        } else if (holder instanceof MultiImagesViewHolder) {
            MultiImagesViewHolder newHolder = (MultiImagesViewHolder) holder;
            newHolder.articleTitle.setText(article.getTitle());
            newHolder.articlePic1.setImageURI(Uri.parse(Constant.BUCKET_HOST_NAME + imageUrls[0]));
            newHolder.articlePic2.setImageURI(Uri.parse(Constant.BUCKET_HOST_NAME + imageUrls[1]));
            newHolder.articlePic3.setImageURI(Uri.parse(Constant.BUCKET_HOST_NAME + imageUrls[2]));
            newHolder.countPics.setText("图片: " + imageUrls.length);
            newHolder.countRead.setText("浏览: " + article.getReadTimes());

            // 如果设置了回调，则设置点击事件
            if (mOnItemClickLitener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getLayoutPosition();
                        mOnItemClickLitener.onItemClick(pos);
                    }
                });
            }

        } else if (holder instanceof RotationViewHolder) {


            RotationViewHolder newHolder = (RotationViewHolder) holder;

            List<SimpleArticleItem> headers = articleList.subList(0, Constant.COUNT_ROTATION);
            newHolder.tvCollegeBroadcast.setText("今天是第19周 农历十一月二十 天气晴转多云 气温偏低 请注意防寒");
            setUpViewPager(newHolder.vpHottest, newHolder.llHottestIndicator, headers);
        }
    }

    /**
     * @param vp             轮播图片
     * @param llBottom       底部的小圆点
     * @param headerArticles 新闻，包括了图片url、标题等属性
     */
    private void setUpViewPager(final ViewPager vp, LinearLayout llBottom, final List<SimpleArticleItem> headerArticles) {
        RotationImageAdapter imageAdapter = new RotationImageAdapter(context, headerArticles);
        //??这儿有些疑惑，Adapter 里面嵌套设置 Adapter 是否优雅？
        vp.setAdapter(imageAdapter);

//        final Handler mHandler = new Handler() {
//            public void handleMessage(Message msg) {
//                switch (msg.what) {
//                    case UPTATE_VIEWPAGER:
//                        if (msg.arg1 != 0) {
//                            vp.setCurrentItem(msg.arg1);
//                        } else {
//                            //false 当从末页调到首页是，不显示翻页动画效果，
//                            vp.setCurrentItem(msg.arg1, false);
//                        }
//                        break;
//                }
//            }
//        };

        //下面是设置动画切换的样式
        vp.setPageTransformer(true, new RotateUpTransformer());

        //创建底部指示位置的导航栏
        final ImageView[] mCircleImages = new ImageView[headerArticles.size()];

        //先去除已有的View，所有的小圆点

        llBottom.removeAllViews();

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


//        //设置自动轮播图片，5s后执行，周期是5s
//
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                Message message = new Message();
//                message.what = UPTATE_VIEWPAGER;
//                if (currentIndex == headerArticles.size() - 1) {
//                    currentIndex = -1;
//                }
//                message.arg1 = currentIndex + 1;
//                mHandler.sendMessage(message);
//            }
//        }, 6000, 6000);
    }

    @Override
    public int getItemCount() {
        //因为多了一个头部，所以是+1,但是头部 ViewPager 占了7个
        //所以实际是少了6个
        if (articleList != null) {
            return articleList.size() + 1 - Constant.COUNT_ROTATION;
        } else {
            Log.i(Constant.LOG, "OriginArticleAdapter getItemCount return 0");
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_ROTATION;
        else {
            SimpleArticleItem article = articleList.get(position + Constant.COUNT_ROTATION - 1);
            if (article == null) {
                return TYPE_FOOTER;
            } else if (article.getImageUrls().length >= 3) {
                return TYPE_MULTI_IMAGES;
            } else {
                return TYPE_NORMAL;
            }
        }
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    class RotationViewHolder extends RecyclerView.ViewHolder {

        //轮播的最热新闻图片
        @InjectView(R.id.vp_hottest)
        ViewPager vpHottest;
        //轮播图片下面的小圆点
        @InjectView(R.id.ll_hottest_indicator)
        LinearLayout llHottestIndicator;

        //学院广播信息
        @InjectView(R.id.tv_college_broadcast)
        TextView tvCollegeBroadcast;

        public RotationViewHolder(View itemView) {
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
        @InjectView(R.id.rcv_article_readtimes)
        TextView rcvArticleReadtimes;
        @InjectView(R.id.rcv_article_summary)
        TextView rcvArticleSummary;

        public ItemArticleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    /**
     * 大于3 张图片使用的ViewHolder
     */
    class MultiImagesViewHolder extends RecyclerView.ViewHolder {


        @InjectView(R.id.article_title)
        TextView articleTitle;
        @InjectView(R.id.article_pic1)
        SimpleDraweeView articlePic1;
        @InjectView(R.id.article_pic2)
        SimpleDraweeView articlePic2;
        @InjectView(R.id.article_pic3)
        SimpleDraweeView articlePic3;
        @InjectView(R.id.count_pics)
        TextView countPics;
        @InjectView(R.id.count_read)
        TextView countRead;

        public MultiImagesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    /**
     * 底部加载更多
     */
    class FooterViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.rcv_load_more)
        ProgressWheel rcvLoadMore;

        public FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }


}
