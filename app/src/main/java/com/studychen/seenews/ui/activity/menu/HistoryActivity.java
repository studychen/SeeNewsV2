package com.studychen.seenews.ui.activity.menu;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.activeandroid.query.Select;
import com.nispok.snackbar.Snackbar;
import com.studychen.seenews.BaseActivity;
import com.studychen.seenews.R;
import com.studychen.seenews.adapter.HistoryArticleAdapter;
import com.studychen.seenews.adapter.LatestArticleAdapter;
import com.studychen.seenews.db.ReviewedArticle;
import com.studychen.seenews.model.SimpleArticleItem;
import com.studychen.seenews.util.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HistoryActivity extends BaseActivity {

    private static final String LOG_FILTER = "HistoryActivity";

    @InjectView(R.id.rcv_article_history)
    RecyclerView mRecyclerView;


    //新闻列表数据
    private List<ReviewedArticle> mArticleList;

    private HistoryArticleAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_history);
        ButterKnife.inject(this);

        mArticleList = new ArrayList<ReviewedArticle>();
        mAdapter = new HistoryArticleAdapter(this, mArticleList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        new ReviewedArticleTask().execute(-1);
        Log.i(LOG_FILTER, "in HistoryActivity onCreate");
    }

    @Override
    protected void onRestart() {
        Log.i(LOG_FILTER, "in HistoryActivity onRestart");
        super.onRestart();
    }

    @Override
    protected void onStart() {
        Log.i(LOG_FILTER, "in HistoryActivity onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.i(LOG_FILTER, "in HistoryActivity onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i(LOG_FILTER, "in HistoryActivity onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i(LOG_FILTER, "in HistoryActivity onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i(LOG_FILTER, "in HistoryActiviy onDestroy");
        super.onDestroy();
        ButterKnife.reset(this);
    }

    private List<ReviewedArticle> getAll() {
        return new Select()
                .from(ReviewedArticle.class)
                .orderBy("aid DESC")
                .execute();
    }

    //Integer 是输入参数
    class ReviewedArticleTask extends AsyncTask<Integer, Void, List<ReviewedArticle>> {


        @Override
        protected List<ReviewedArticle> doInBackground(Integer... params) {
            Log.i(Constant.LOG, "in doInBackground 得到历史新闻" + getAll());
            return getAll();
        }

        @Override
        protected void onPostExecute(final List<ReviewedArticle> moreArticles) {
            super.onPostExecute(moreArticles);
            mArticleList.addAll(moreArticles);

            Log.i(Constant.LOG, "in doInBackground 得到历史新闻" + mArticleList);

            mAdapter.notifyDataSetChanged();
        }

    }
}
