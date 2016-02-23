package com.studychen.seenews.ui.fragment.first;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.studychen.seenews.R;
import com.studychen.seenews.adapter.OriginArticleAdapter;
import com.studychen.seenews.model.SimpleArticleItem;
import com.studychen.seenews.util.ApiUrl;
import com.studychen.seenews.util.ColumnType;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 普通 展示新闻列表
 * <p/>
 * 实现上拉加载更多
 * Created by tomchen on 1/10/16.
 */
public class OriginalArticleFragment extends Fragment {


    private static final String POSITION = "column";
    private static final String LOG = "PAGER_LOG";
    private static final String SINA_ERROR_INFO = "您所访问的网站发生故障";
    // 控制还剩几项新闻的时候加载更多
    private static final int VISIBLE_THRESHOLD = 3;
    @InjectView(R.id.rcv_article_origin)
    RecyclerView mRecyclerView;
    //存储的参数,是栏目的 id 不连续
    private int mColumn;
    //获取 fragment 依赖的 Activity，方便使用 Context
    private Activity mActivity;
    private Handler mHandler;
    private OriginArticleAdapter mAdapter;
    private List<SimpleArticleItem> mArticleList;
    private boolean loading;

    public static OriginalArticleFragment newInstance(int param) {
        OriginalArticleFragment fragment = new OriginalArticleFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION, param);
        Log.i(LOG, "in newInstance put position " + param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int mPosition = getArguments().getInt(POSITION);
        mColumn = ColumnType.getType(mPosition);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_first_original, container, false);
        ButterKnife.inject(this, view);
        mActivity = getActivity();
        mHandler = new Handler();
        mArticleList = new ArrayList<SimpleArticleItem>();
        Log.i(LOG, "in onCreateView");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.i(LOG, "in onActivityCreated");

        //让 RecyclerView 每一项的高度相同
//        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

        mAdapter = new OriginArticleAdapter(mActivity, mArticleList, mRecyclerView);

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

                Log.i(LOG, "in onScrolled(recyclerView, dx, dy);");

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                int totalItemCount = layoutManager.getItemCount();
                Log.i(LOG, "totalItemCount: " + totalItemCount);

                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                Log.i(LOG, "lastVisibleItem: " + lastVisibleItem);

                if (!loading && totalItemCount < (lastVisibleItem + VISIBLE_THRESHOLD)) {
                    new ArticleTask().execute(mAdapter.getBottomArticleId());
                    loading = true;
                }
            }
        });

        new ArticleTask().execute(-1);

//        mAdapter.setOnLoadMoreListener(new OriginArticleAdapter.OnLoadMoreListener() {
//            /**
//             * 加载更多
//             */
//            @Override
//            public void onLoadMore() {
//
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        //   remove progress item
//                        mArticleList.remove(mArticleList.size() - 1);
//                        //notifyItemRemoved(int position)
//                        // 第position个被删除的时候刷新，同样会有动画。
//                        mAdapter.notifyItemRemoved(mArticleList.size());
//                        List<SimpleArticleItem> moreArticles = getArticleList(mColumn,
//                                mArticleList.get(mArticleList.size() - 1).getId());
//
//                        for (int i = 0; i < moreArticles.size(); i++) {
//                            mArticleList.add(moreArticles.get(i));
//                            mAdapter.notifyItemInserted(mArticleList.size());
//                        }
//                        mAdapter.setLoaded();
//                    }
//                }, 1000);
//            }
//        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    /**
     * @param type   第几个栏目
     * @param offset 偏移 id
     * @return
     */
    public List<SimpleArticleItem> getArticleList(int type, int offset) {
        String api = ApiUrl.columnUrl();
        String url = String.format(api, type, offset);
        Log.i(LOG, url);

        OkHttpClient client = new OkHttpClient();

        Request request
                = new Request.Builder()
                .url(url)
                .build();

        try {
            Response responses = client.newCall(request).execute();
            String jsonData = responses.body().string();
            Log.i(LOG, jsonData);


            // 新浪云网站故障，资源耗尽
            if (jsonData.contains(SINA_ERROR_INFO)) {
                return null;
            } else {
                Gson gson = new GsonBuilder().create();

                Log.i(LOG, jsonData);

                Type listType = new TypeToken<List<SimpleArticleItem>>() {
                }.getType();
                List<SimpleArticleItem> articles = gson.fromJson(jsonData, listType);
                Log.i(LOG, articles + "");
                return articles;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<SimpleArticleItem>();

    }

    class ArticleTask extends AsyncTask<Integer, Void, List<SimpleArticleItem>> {

        /**
         * Runs on the UI thread before {@link #doInBackground}.
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mArticleList != null && mArticleList.size() > 0) {
                mArticleList.add(null);
                // notifyItemInserted(int position)，这个方法是在第position位置
                // 被插入了一条数据的时候可以使用这个方法刷新，
                // 注意这个方法调用后会有插入的动画，这个动画可以使用默认的，也可以自己定义。
                Log.i(LOG, "in mArticleList.add(null)");

                mAdapter.notifyItemInserted(mArticleList.size() - 1);
            }
        }

        /**
         * @param params 偏移量 id
         * @return
         */
        @Override
        protected List<SimpleArticleItem> doInBackground(Integer... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return getArticleList(mColumn, params[0]);
        }

        @Override
        protected void onPostExecute(final List<SimpleArticleItem> moreArticles) {
            // 新增新闻数据
            super.onPostExecute(moreArticles);

            if (mArticleList.size() == 0) {
                mArticleList.addAll(moreArticles);
                mAdapter.notifyDataSetChanged();
            } else {
                //删除 footer
                mArticleList.remove(mArticleList.size() - 1);
                mArticleList.addAll(moreArticles);
                mAdapter.notifyDataSetChanged();
//                for (int i = 0; i < moreArticles.size(); i++) {
//                    mArticleList.add(moreArticles.get(i));
//                    mAdapter.notifyItemInserted(mArticleList.size());
//                }
                loading = false;
//            mArticleList.addAll(moreArticles);
            }


        }

    }

}
