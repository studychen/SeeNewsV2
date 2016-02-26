package com.studychen.seenews.ui.activity.first;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.studychen.seenews.R;
import com.studychen.seenews.db.ReviewedArticle;
import com.studychen.seenews.model.ArticleItem;
import com.studychen.seenews.ui.fragment.first.LatestArticleFragment;
import com.studychen.seenews.util.ApiUrl;
import com.studychen.seenews.util.Constant;

import java.io.IOException;
import java.lang.reflect.Type;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by tomchen on 2/23/16.
 */
public class DetailActivity extends AppCompatActivity {
    @InjectView(R.id.article_body)
    WebView articleBody;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @InjectView(R.id.article_image)
    SimpleDraweeView articleImage;
    @InjectView(R.id.detail_title)
    TextView detailTitle;
    @InjectView(R.id.detail_date)
    TextView detailDate;
    @InjectView(R.id.detail_source)
    TextView detailSource;
    @InjectView(R.id.detail_read)
    TextView detailRead;
    @InjectView(R.id.detail_article)
    LinearLayout detailArticle;

    private int columnType;
    private int articleID;
    private String title;
    private String date;
    private int read;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        ButterKnife.inject(this);

        initToolbar();

        columnType = getIntent().getIntExtra(LatestArticleFragment.COLUMN_TYPE, 0);
        articleID = getIntent().getIntExtra(LatestArticleFragment.ARTICLE_ID, 7948);
        title = getIntent().getStringExtra(LatestArticleFragment.ARTICLE_TITLE);
        date = getIntent().getStringExtra(LatestArticleFragment.ARTICLE_DATE);
        read = getIntent().getIntExtra(LatestArticleFragment.ARTICLE_READ, 452);

        new LatestArticleTask().execute(columnType, articleID);
    }


    private void initToolbar() {

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_left_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * 设置宽度
     * 当LinearLayout 的内容小于屏幕高度时候
     * 也能上拉实现图片和ToolBar动画效果
     */
    private void setMinHeight() {


        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenHeight = displaymetrics.heightPixels;

        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (this.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }

        detailArticle.setMinimumHeight(screenHeight - actionBarHeight);
    }


    /**
     * 根据 type 和 id 获取新闻详情
     *
     * @param type
     * @param articleID
     * @return
     */
    public ArticleItem getArticleDetail(int type, int articleID) {
        String api = ApiUrl.articleUrl();
        String url = String.format(api, type, articleID);

        Log.i(Constant.LOG, "新闻详情页面 url" + url);

        Log.i(Constant.LOG, url);

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

                Type listType = new TypeToken<ArticleItem>() {
                }.getType();
                ArticleItem article = gson.fromJson(jsonData, listType);
                Log.i(Constant.LOG, article + "");
                return article;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArticleItem();

    }

    /**
     * 选项菜单
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //返回键 back的箭头
            case android.R.id.home:
                this.finish();
                return true;
        }
        return false;
    }


    //String 是输入参数
    class LatestArticleTask extends AsyncTask<Integer, Void, ArticleItem> {


        @Override
        protected ArticleItem doInBackground(Integer... params) {
            ReviewedArticle articleHistory = new ReviewedArticle();
            articleHistory.type = params[0];
            articleHistory.id = params[1];
            articleHistory.title = title;
            articleHistory.date = date;
            articleHistory.read = read;
            articleHistory.save();
            return getArticleDetail(params[0], params[1]);
        }

        /**
         * Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.
         */
        @Override
        protected void onPostExecute(ArticleItem simpleArticleItem) {
            super.onPostExecute(simpleArticleItem);

            detailTitle.setText(simpleArticleItem.getTitle());
            detailDate.setText(simpleArticleItem.getPublishDate());
            detailSource.setText("来源：" + simpleArticleItem.getSource());
            detailRead.setText(simpleArticleItem.getReadTimes() + "浏览");

            collapsingToolbar.setTitle(simpleArticleItem.getTitle());

            String[] imageUrls = simpleArticleItem.getImageUrls();

            //当图片小于3张时候 选取第1张图片
            if (imageUrls.length > 0) {
                articleImage.setImageURI(Uri.parse(Constant.BUCKET_HOST_NAME
                        + imageUrls[0]));
            } else {
                articleImage.setImageURI(Uri.parse(ApiUrl.randomImageUrl(simpleArticleItem.getId())));
            }

            articleBody.getSettings().setJavaScriptEnabled(true);


            articleBody.loadDataWithBaseURL("", "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=2.0, minimum-scale=1.0, user-scalable=no\" />" + "<style>img{display: inline;height: auto;max-width: 100%;}</style>" + simpleArticleItem.getBody(), "text/html", "UTF-8", "");
//            setMinHeight();
        }
    }
}
