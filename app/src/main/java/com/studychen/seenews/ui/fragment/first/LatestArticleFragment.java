package com.studychen.seenews.ui.fragment.first;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nispok.snackbar.Snackbar;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.studychen.seenews.R;
import com.studychen.seenews.adapter.LatestArticleAdapter;
import com.studychen.seenews.model.SimpleArticleItem;
import com.studychen.seenews.ui.activity.first.DetailActivity;
import com.studychen.seenews.util.ApiUrl;
import com.studychen.seenews.util.Constant;
import com.studychen.seenews.util.OnItemClickLitener;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 最新新闻
 * 上面是轮播图片
 * 下面是列表新闻
 * Created by tomchen on 1/10/16.
 */
public class LatestArticleFragment extends Fragment {

    public static final String ARTICLE_ID = "id";
    public static final String COLUMN_TYPE = "type";
    private static final String ARTICLE_LATEST_PARAM = "param";
    private static final int UPTATE_VIEWPAGER = 0;
    private static final int TYPE_INT = 0;

    //latest的column是0
    private static final int mColumn = 0;
    //    //轮播的最热新闻图片
//    @InjectView(R.id.vp_hottest)
//    ViewPager vpHottest;
//    //轮播图片下面的小圆点
//    @InjectView(R.id.ll_hottest_indicator)
//    LinearLayout llHottestIndicator;
//
//    //学院广播信息
//    @InjectView(R.id.tv_college_broadcast)
//    TextView tvCollegeBroadcast;
    //新闻列表

    @InjectView(R.id.rcv_article_latest)
    RecyclerView mRecyclerView;
    @InjectView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private int totalItemCount;
    private int lastVisibleItem;
    //获取 fragment 依赖的 Activity，方便使用 Context
    private Activity mActivity;

    private LatestArticleAdapter mAdapter;


    //新闻列表数据
    private List<SimpleArticleItem> mArticleList = new ArrayList<SimpleArticleItem>();

    private boolean loading = false;
    private boolean bottom = false;

    //设置当前 第几个图片 被选中
    private int currentIndex = 0;
    private ImageView[] mBottomImages;//底部只是当前页面的小圆点
    private Timer timer = new Timer(); //为了方便取消定时轮播，将 Timer 设为全局

//    //定时轮播图片，需要在主线程里面修改 UI
//    private Handler mHandler = new Handler() {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case UPTATE_VIEWPAGER:
//                    if (msg.arg1 != 0) {
//                        vpHottest.setCurrentItem(msg.arg1);
//                    } else {
//                        //false 当从末页调到首页是，不显示翻页动画效果，
//                        vpHottest.setCurrentItem(msg.arg1, false);
//                    }
//                    break;
//            }
//        }
//    };

    public static LatestArticleFragment newInstance(String param) {
        LatestArticleFragment fragment = new LatestArticleFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_first_latest, container, false);
        ButterKnife.inject(this, view);
        mActivity = getActivity();
        Log.i(Constant.LOG, "获得的Activity " + mActivity);
        mArticleList = new ArrayList<SimpleArticleItem>();
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));//这里用线性显示 类似于listview
        //最开始 ViewPager 没有数据
//        setUpViewPager( null );

        Log.i(Constant.LOG, "in onActivityCreated");

        mAdapter = new LatestArticleAdapter(mActivity, mArticleList);

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            /**
             * Callback method to be invoked when the RecyclerView has been scrolled. This will be
             * called after the scroll has completed.
             * <p/>
             * This callback will also be called if visible item range changes after a layout
             * calculation. In that case, dx and dy will be 0.
             *
             * @param recyclerView The RecyclerView which scrolled.
             * @param dx           The amount of horizontal scroll.
             * @param dy           The amount of vertical scroll.
             */
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                super.onScrolled(recyclerView, dx, dy);

                Log.i(Constant.LOG, "in onScrolled(recyclerView, dx, dy);");

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                totalItemCount = layoutManager.getItemCount();
                Log.i(Constant.LOG, "totalItemCount: " + totalItemCount);

                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                Log.i(Constant.LOG, "lastVisibleItem: " + lastVisibleItem);

                if (lastVisibleItem != totalItemCount - 1) {
                    bottom = false;
                }
                if (!bottom && !loading && totalItemCount < (lastVisibleItem + Constant.VISIBLE_THRESHOLD)) {
                    new LatestArticleTask().execute(mAdapter.getBottomArticleId());
                    loading = true;
                }
            }
        });

        mAdapter.setOnItemClickLitener(new OnItemClickLitener() {
            @Override
            public void onItemClick(int position) {
                SimpleArticleItem articleItem = mArticleList.get(position + Constant.COUNT_ROTATION - 1);
                Intent intent = new Intent(mActivity, DetailActivity.class);
                intent.putExtra(COLUMN_TYPE, TYPE_INT);
                intent.putExtra(ARTICLE_ID, articleItem.getId());
                startActivity(intent);
            }
        });


        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new MoreArticleTask().execute(mAdapter.getTopOriginArticleId());
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                new MoreArticleTask().execute(mAdapter.getTopOriginArticleId());
            }
        });
//        swipeRefreshLayout.setRefreshing(true);
//        new LatestArticleTask().execute(-1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


    public List<SimpleArticleItem> getRotationItem() {
        String url = ApiUrl.rotationUrl();

        OkHttpClient client = new OkHttpClient();

        Request request
                = new Request.Builder()
                .url(url)
                .build();

        try {
            Response responses = client.newCall(request).execute();
            String jsonData = responses.body().string();
            Log.i(Constant.LOG, jsonData);


            // 新浪云网站故障，资源耗尽
            if (jsonData.contains(Constant.SINA_ERROR_INFO)) {
                return null;
            } else {
                Gson gson = new GsonBuilder().create();

                Log.i(Constant.LOG, jsonData);

                Type listType = new TypeToken<List<SimpleArticleItem>>() {
                }.getType();
                List<SimpleArticleItem> articles = gson.fromJson(jsonData, listType);
                Log.i(Constant.LOG, articles + "");
                return articles;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<SimpleArticleItem>();

    }


//    private void setUpViewPager(final List<String> strings) {
////        RotationImageAdapter imageAdapter = new RotationImageAdapter(mActivity, strings);
////        vpHottest.setAdapter(imageAdapter);
//
//        //下面是设置动画切换的样式
//        vpHottest.setPageTransformer(true, new RotateUpTransformer());
//
//        //创建底部指示位置的导航栏
//        mBottomImages = new ImageView[strings.size()];
//
//        for (int i = 0; i < mBottomImages.length; i++) {
//            ImageView imageView = new ImageView(mActivity);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10, 10);
//            params.setMargins(5, 0, 5, 0);
//            imageView.setLayoutParams(params);
//            if (i == 0) {
//                imageView.setBackgroundResource(R.drawable.indicator_select);
//            } else {
//                imageView.setBackgroundResource(R.drawable.indicator_not_select);
//            }
//
//            mBottomImages[i] = imageView;
//            //把指示作用的原点图片加入底部的视图中
//            llHottestIndicator.addView(mBottomImages[i]);
//
//        }
//
//        vpHottest.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            //图片左右滑动时候，将当前页的圆点图片设为选中状态
//            @Override
//            public void onPageSelected(int position) {
//                // 一定几个图片，几个圆点，但注意是从0开始的
//                int total = mBottomImages.length;
//                for (int j = 0; j < total; j++) {
//                    if (j == position) {
//                        mBottomImages[j].setBackgroundResource(R.drawable.indicator_select);
//                    } else {
//                        mBottomImages[j].setBackgroundResource(R.drawable.indicator_not_select);
//                    }
//                }
//
//                //设置全局变量，currentIndex为选中图标的 index
//                currentIndex = position;
//            }
//
//            @Override
//            public void onPageScrolled(int i, float v, int i1) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//                //实现切换到末尾后返回到第一张
//                switch (state) {
//                    // 手势滑动
//                    case ViewPager.SCROLL_STATE_DRAGGING:
//                        break;
//
//                    // 界面切换中
//                    case ViewPager.SCROLL_STATE_SETTLING:
//                        break;
//
//                    case ViewPager.SCROLL_STATE_IDLE:// 滑动结束，即切换完毕或者加载完毕
//                        // 当前为最后一张，此时从右向左滑，则切换到第一张
//                        if (vpHottest.getCurrentItem() == vpHottest.getAdapter()
//                                .getCount() - 1) {
//                            vpHottest.setCurrentItem(0, false);
//                        }
//                        // 当前为第一张，此时从左向右滑，则切换到最后一张
//                        else if (vpHottest.getCurrentItem() == 0) {
//                            vpHottest.setCurrentItem(vpHottest.getAdapter()
//                                    .getCount() - 1, false);
//                        }
//                        break;
//
//                    default:
//                        break;
//                }
//            }
//        });
//
//
//        //设置自动轮播图片，5s后执行，周期是5s
//
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                Message message = new Message();
//                message.what = UPTATE_VIEWPAGER;
//                if (currentIndex == strings.size() - 1) {
//                    currentIndex = -1;
//                }
//                message.arg1 = currentIndex + 1;
//                mHandler.sendMessage(message);
//            }
//        }, 6000, 6000);
//    }
//

    /**
     * @param type     第几个栏目
     * @param moreThan 大于该id的新闻数组
     * @return
     */
    public List<SimpleArticleItem> getMoreById(int type, int moreThan) {
        String api = ApiUrl.moreThanUrl();
        String url = String.format(api, type, moreThan);

        Log.i(Constant.LOG, "得到更多新闻 url " + url);

        OkHttpClient client = new OkHttpClient();

        Request request
                = new Request.Builder()
                .url(url)
                .build();

        try {
            Response responses = client.newCall(request).execute();
            String jsonData = responses.body().string();
            Log.i(Constant.LOG, jsonData);


            // 新浪云网站故障，资源耗尽
            if (jsonData.contains(Constant.SINA_ERROR_INFO)) {
                return null;
            } else {
                Gson gson = new GsonBuilder().create();

                Log.i(Constant.LOG, "json数据" + jsonData);

                Type listType = new TypeToken<List<SimpleArticleItem>>() {
                }.getType();
                List<SimpleArticleItem> articles = gson.fromJson(jsonData, listType);
                Log.i(Constant.LOG, "json解析的Articles" + articles);
                return articles;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i(Constant.LOG, "JSON没获得数据");
        return new ArrayList<SimpleArticleItem>();
    }

    /**
     * @param type   第几个栏目
     * @param offset 偏移 id
     * @return
     */
    public List<SimpleArticleItem> getArticleList(int type, int offset) {
        String api = ApiUrl.columnUrl();
        String url = String.format(api, type, offset);

        OkHttpClient client = new OkHttpClient();

        Request request
                = new Request.Builder()
                .url(url)
                .build();

        try {
            Response responses = client.newCall(request).execute();
            String jsonData = responses.body().string();
            Log.i(Constant.LOG, jsonData);


            // 新浪云网站故障，资源耗尽
            if (jsonData.contains(Constant.SINA_ERROR_INFO)) {
                return null;
            } else {
                Gson gson = new GsonBuilder().create();

                Log.i(Constant.LOG, "json数据" + jsonData);

                Type listType = new TypeToken<List<SimpleArticleItem>>() {
                }.getType();
                List<SimpleArticleItem> articles = gson.fromJson(jsonData, listType);
                Log.i(Constant.LOG, "json解析的Articles" + articles);
                return articles;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i(Constant.LOG, "JSON没获得数据");
        return new ArrayList<SimpleArticleItem>();
    }

    //Integer 是输入参数
    // 得到比某个id大的新闻数组
    class MoreArticleTask extends AsyncTask<Integer, Void, List<SimpleArticleItem>> {
        @Override
        protected List<SimpleArticleItem> doInBackground(Integer... params) {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            List<SimpleArticleItem> data = new ArrayList<SimpleArticleItem>();

            //只有第一次需要加载头部的轮播图片
            //下拉刷新时候不加轮播图片
            if (mArticleList.size() == 0) {
                data.addAll(getRotationItem());
            }

            data.addAll(getMoreById(mColumn, params[0]));
            return data;

        }

        @Override
        protected void onPostExecute(List<SimpleArticleItem> simpleArticleItems) {
            super.onPostExecute(simpleArticleItems);

            swipeRefreshLayout.setRefreshing(false);

            //没有新的数据，提示消息
            if (simpleArticleItems == null || simpleArticleItems.size() == 0) {
                Snackbar.with(mActivity.getApplicationContext()) // context
                        .text(mActivity.getResources().getString(R.string.list_more_data)) // text to display
                        .duration(Snackbar.SnackbarDuration.LENGTH_SHORT) // make it shorter
                        .show(mActivity); // activity where it is displayed
            } else {
                mArticleList.addAll(simpleArticleItems);
                mAdapter.notifyDataSetChanged();
            }


        }

    }

    //Integer 是输入参数
    class LatestArticleTask extends AsyncTask<Integer, Void, List<SimpleArticleItem>> {

        /**
         * Runs on the UI thread before {@link #doInBackground}.
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //增加底部的一个null数据，表示ProgressBar
            if (mArticleList != null && mArticleList.size() > 0) {
                mArticleList.add(null);
                // notifyItemInserted(int position)，这个方法是在第position位置
                // 被插入了一条数据的时候可以使用这个方法刷新，
                // 注意这个方法调用后会有插入的动画，这个动画可以使用默认的，也可以自己定义。
                Log.i(Constant.LOG, "增加底部footer 圆形ProgressBar");

                mAdapter.notifyItemInserted(mArticleList.size() - 1);
            }
        }

        @Override
        protected List<SimpleArticleItem> doInBackground(Integer... params) {
            Log.i(Constant.LOG, "in doInBackground");

            List<SimpleArticleItem> data = new ArrayList<SimpleArticleItem>();

            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //只有第一次需要加载头部的轮播图片
            //下拉刷新时候不加轮播图片
            if (mArticleList.size() == 0) {
                data.addAll(getRotationItem());
            }

            data.addAll(getArticleList(mColumn, params[0]));
            return data;
        }

        @Override
        protected void onPostExecute(final List<SimpleArticleItem> moreArticles) {
            super.onPostExecute(moreArticles);
            if (mArticleList.size() == 0) {
                mArticleList.addAll(moreArticles);
                mAdapter.notifyDataSetChanged();
            } else {
                //删除 footer
                mArticleList.remove(mArticleList.size() - 1);

                Log.i(Constant.LOG, "下拉增加数据 " + moreArticles);

                //只有到达最底部才加载
                //防止上拉到了倒数两三个也加载
                if (!bottom && lastVisibleItem == totalItemCount - 1 && moreArticles.size() == 0) {
                    Snackbar.with(mActivity.getApplicationContext()) // context
                            .text(mActivity.getResources().getString(R.string.list_no_data)) // text to display
                            .duration(Snackbar.SnackbarDuration.LENGTH_SHORT) // make it shorter
                            .show(mActivity); // activity where it is displayed
                    bottom = true;
                }

                mArticleList.addAll(moreArticles);
                mAdapter.notifyDataSetChanged();

                loading = false;
//            mArticleList.addAll(moreArticles);
            }

        }
    }

}
