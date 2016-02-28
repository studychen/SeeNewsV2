package com.studychen.seenews.ui.activity.menu;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.orhanobut.logger.Logger;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.studychen.seenews.BaseActivity;
import com.studychen.seenews.R;
import com.studychen.seenews.adapter.HistoryArticleAdapter;
import com.studychen.seenews.model.ReviewedArticle;
import com.studychen.seenews.util.Constant;
import com.studychen.seenews.util.OnDeleteClickLitener;
import com.studychen.seenews.util.OnItemLongClickLitener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HistoryActivity extends BaseActivity {

    @InjectView(R.id.rcv_article_history)
    RecyclerView mRecyclerView;
    @InjectView(R.id.toolbar_history)
    Toolbar mToolbar;
    @InjectView(R.id.progress_wheel)
    ProgressWheel progressWheel;
    @InjectView(R.id.progress_layout)
    LinearLayout progressLayout;


    //新闻列表数据
    private List<ReviewedArticle> mArticleList;

    private HistoryArticleAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_history);
        ButterKnife.inject(this);
        initToolbar();

        progressWheel.spin();
        mArticleList = new ArrayList<ReviewedArticle>();
        mAdapter = new HistoryArticleAdapter(this, mArticleList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter.setOnItemLongClickLitener(new OnItemLongClickLitener() {
            @Override
            public void onItemLongClick(int position) {
                delteDbRecord(mArticleList.remove(position).aid);
                mAdapter.notifyItemRemoved(position);
            }
        });

        mAdapter.setOnDeleteClickLitener(new OnDeleteClickLitener() {
            @Override
            public void onDeleteClick(int position) {
                delteDbRecord(mArticleList.remove(position).aid);
                mAdapter.notifyItemRemoved(position);
            }
        });


        new ReviewedArticleTask().execute(-1);
        Logger.d("in HistoryActivity onCreate");
    }

    @Override
    protected void onRestart() {
        Logger.d("in HistoryActivity onRestart");
        super.onRestart();
    }

    @Override
    protected void onStart() {
        Logger.d("in HistoryActivity onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Logger.d("in HistoryActivity onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Logger.d("in HistoryActivity onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Logger.d("in HistoryActivity onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Logger.d("in HistoryActivity onDestroy");
        super.onDestroy();
        ButterKnife.reset(this);
    }

    /**
     * 删除数据库记录
     *
     * @return
     */
    private void delteDbRecord(int aid) {
        new Delete().from(ReviewedArticle.class).where("aid = ?", aid).execute();
    }

    private List<ReviewedArticle> getAll() {
        //按照访问时间倒序排列
        return new Select()
                .from(ReviewedArticle.class)
                .orderBy("click_time DESC")
                .execute();
    }

    /**
     * 初始化Toolbar
     */
    private void initToolbar() {
        mToolbar.setTitle(getResources().getString(R.string.review_history));
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_left_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * 选项菜单
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

    //Integer 是输入参数
    class ReviewedArticleTask extends AsyncTask<Integer, Void, List<ReviewedArticle>> {


        @Override
        protected List<ReviewedArticle> doInBackground(Integer... params) {
            Logger.d("in doInBackground 得到历史新闻" + getAll());
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return getAll();
        }

        @Override
        protected void onPostExecute(final List<ReviewedArticle> moreArticles) {
            super.onPostExecute(moreArticles);
            progressLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);

            mArticleList.addAll(moreArticles);

            mAdapter.notifyDataSetChanged();
        }

    }
}
