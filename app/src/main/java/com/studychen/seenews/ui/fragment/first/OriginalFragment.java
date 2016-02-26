package com.studychen.seenews.ui.fragment.first;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.studychen.seenews.R;
import com.studychen.seenews.adapter.HeaderAdapter;
import com.studychen.seenews.model.ItemArticle;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by tomchen on 1/10/16.
 */
public class OriginalFragment extends Fragment {

    private static final String ARTICLE_LATEST_PARAM = "param";

    private static final String log = "PAGER_LOG";

    private static final int UPTATE_VIEWPAGER = 0;

    //轮播的最热新闻图片
    @InjectView(R.id.vp_hottest)
    ViewPager vpHottest;
    //轮播图片下面的小圆点
    @InjectView(R.id.ll_hottest_indicator)
    LinearLayout llHottestIndicator;


    //存储的参数
    private String mParam;

    //获取 fragment 依赖的 Activity，方便使用 Context
    private Activity mAct;

    //新闻列表数据
    private List<ItemArticle> itemArticleList = new ArrayList<ItemArticle>();

    //设置当前 第几个图片 被选中
    private int autoCurrIndex = 0;
    private ImageView[] mBottomImages;//底部只是当前页面的小圆点
    private Timer timer = new Timer(); //为了方便取消定时轮播，将 Timer 设为全局

    //定时轮播图片，需要在主线程里面修改 UI
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPTATE_VIEWPAGER:
                    if (msg.arg1 != 0) {
                        vpHottest.setCurrentItem(msg.arg1);
                    } else {
                        //false 当从末页调到首页是，不显示翻页动画效果，
                        vpHottest.setCurrentItem(msg.arg1, false);
                    }
                    break;
            }
        }
    };

    public static OriginalFragment newInstance(String param) {
        OriginalFragment fragment = new OriginalFragment();
        Bundle args = new Bundle();
        args.putString(ARTICLE_LATEST_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mParam = savedInstanceState.getString(ARTICLE_LATEST_PARAM);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_first_other, container, false);
        mAct = getActivity();
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new ImageTask().execute();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


    private void setUpViewPager(final List<ItemArticle> headerArticles) {
        HeaderAdapter imageAdapter = new HeaderAdapter(mAct, headerArticles);
        vpHottest.setAdapter(imageAdapter);

        //下面是设置动画切换的样式
        vpHottest.setPageTransformer(true, new RotateUpTransformer());

        //创建底部指示位置的导航栏
        mBottomImages = new ImageView[headerArticles.size()];

        for (int i = 0; i < mBottomImages.length; i++) {
            ImageView imageView = new ImageView(mAct);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10, 10);
            params.setMargins(5, 0, 5, 0);
            imageView.setLayoutParams(params);
            if (i == 0) {
                imageView.setBackgroundResource(R.drawable.indicator_select);
            } else {
                imageView.setBackgroundResource(R.drawable.indicator_not_select);
            }

            mBottomImages[i] = imageView;
            //把指示作用的原点图片加入底部的视图中
            llHottestIndicator.addView(mBottomImages[i]);

        }

        vpHottest.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                                              //图片左右滑动时候，将当前页的圆点图片设为选中状态
                                              @Override
                                              public void onPageSelected(int position) {
                                                  // 一定几个图片，几个圆点，但注意是从0开始的
                                                  int total = mBottomImages.length;
                                                  for (int j = 0; j < total; j++) {
                                                      if (j == position) {
                                                          mBottomImages[j].setBackgroundResource(R.drawable.indicator_select);
                                                      } else {
                                                          mBottomImages[j].setBackgroundResource(R.drawable.indicator_not_select);
                                                      }
                                                  }

                                                  //设置全局变量，currentIndex为选中图标的 index
                                                  autoCurrIndex = position;
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
                                                          Log.i(log, "in SCROLL_STATE_SETTLING " + vpHottest.getCurrentItem());
                                                          break;

                                                      case ViewPager.SCROLL_STATE_IDLE:// 滑动结束，即切换完毕或者加载完毕
                                                          Log.i(log, "in SCROLL_STATE_IDLE " + vpHottest.getCurrentItem());
                                                          // 当前为最后一张，此时从右向左滑，则切换到第一张
                                                          if (vpHottest.getCurrentItem() == vpHottest.getAdapter()
                                                                  .getCount() - 1) {
                                                              vpHottest.setCurrentItem(0, false);
                                                          }
                                                          // 当前为第一张，此时从左向右滑，则切换到最后一张
                                                          else if (vpHottest.getCurrentItem() == 0) {
                                                              vpHottest.setCurrentItem(vpHottest.getAdapter()
                                                                      .getCount() - 1, false);
                                                          }
                                                          break;

                                                      default:
                                                          Log.i(log, "in default");
                                                          break;
                                                  }

                                              }
                                          }
        );


//        //      设置自动轮播图片，5s后执行，周期是5s
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                Message message = new Message();
//                message.what = UPTATE_VIEWPAGER;
//                if (autoCurrIndex == headerArticles.size() - 1) {
//                    autoCurrIndex = -1;
//                }
//                message.arg1 = autoCurrIndex + 1;
//                mHandler.sendMessage(message);
//            }
//        }, 2000, 2000);

    }


    class ImageTask extends AsyncTask<String, Void, List<ItemArticle>> {
        @Override
        protected List<ItemArticle> doInBackground(String... params) {
            List<ItemArticle> articles = new ArrayList<ItemArticle>();
            articles.add(
                    new ItemArticle(1123, "http://see.xidian.edu.cn/uploads/image/20151231/20151231105648_11790.jpg"));
            articles.add(
                    new ItemArticle(1123, "http://see.xidian.edu.cn/uploads/image/20151230/20151230152544_36663.jpg"));
            articles.add(
                    new ItemArticle(1123, "http://see.xidian.edu.cn/uploads/image/20151229/20151229204329_75030.jpg"));
            articles.add(
                    new ItemArticle(1123, "http://see.xidian.edu.cn/uploads/image/20151221/20151221151031_36136.jpg"));

            return articles;
        }

        @Override
        protected void onPostExecute(List<ItemArticle> articles) {
            //这儿的 是 url 的集合
            super.onPostExecute(articles);
            setUpViewPager(articles);

        }
    }

}
