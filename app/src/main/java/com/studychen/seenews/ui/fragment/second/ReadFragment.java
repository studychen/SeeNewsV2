package com.studychen.seenews.ui.fragment.second;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.studychen.seenews.R;
import com.studychen.seenews.model.ItemArticle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 *
 */
public class ReadFragment extends Fragment {
    public static final String ARTICLE_BASE_URL = "http://seenews.applinzi.com/articleWithSql?action=query&aid=";
    private static final String SALE_PARAM = "param";
    private static final String SINA_ERROR_INFO = "您所访问的网站发生故障";
    @InjectView(R.id.rcv_article_title)
    TextView rcvArticleTitle;
    @InjectView(R.id.rcv_article_date)
    TextView rcvArticleDate;
    @InjectView(R.id.rcv_article_source)
    TextView rcvArticleSource;
    @InjectView(R.id.rcv_article_readtimes)
    TextView rcvArticleReadtimes;
    @InjectView(R.id.article_body)
    WebView articleBody;
    private String mParam;
    private ArticleDetailTask mTask;

    public static Fragment newInstance(String param) {
        ReadFragment fragment = new ReadFragment();
        Bundle args = new Bundle();
        args.putString(SALE_PARAM, param);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getString(SALE_PARAM);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTask = new ArticleDetailTask();
        mTask.execute(7941);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two, null);
        Log.i(SALE_PARAM, "in SaleFragment");
        ButterKnife.inject(this, view);
        return view;
    }


    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null)
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    //String 是输入参数
    class ArticleDetailTask extends AsyncTask<Integer, Void, ItemArticle> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ItemArticle doInBackground(Integer... params) {

            String url = ARTICLE_BASE_URL + params[0];

            OkHttpClient client = new OkHttpClient();

            Request request
                    = new Request.Builder()
                    .url(url)
                    .build();
            Response responses = null;

            try {
                responses = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String jsonData = null;
            try {
                jsonData = responses.body().string();

                // 新浪云网站故障，资源耗尽
                if (jsonData.contains(SINA_ERROR_INFO)) {
                    return null;
                } else {
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    ItemArticle article = gson.fromJson(jsonData, ItemArticle.class);
                    return article;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(ItemArticle data) {
            super.onPostExecute(data);
            if (data != null) {
                rcvArticleTitle.setText(data.getTitle());
                rcvArticleDate.setText(data.getPublishDate());
                rcvArticleSource.setText("来源于：" + data.getSource());
                rcvArticleReadtimes.setText(data.getReadTimes() + "");
                articleBody.getSettings().setJavaScriptEnabled(true);
                articleBody.loadDataWithBaseURL(null, data.getBody(), "text/html", "UTF-8", null);
            }
        }
    }

}
