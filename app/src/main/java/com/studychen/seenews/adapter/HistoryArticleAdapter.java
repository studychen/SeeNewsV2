package com.studychen.seenews.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.studychen.seenews.R;
import com.studychen.seenews.db.ReviewedArticle;
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
 * 只有列表新闻
 * 没有头部的的轮播图片
 */

public class HistoryArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    //新闻列表
    private List<ReviewedArticle> articleList;
    //context
    private Context context;

    private LayoutInflater mLayoutInflater;

    private OnItemClickLitener mOnItemClickLitener;//点击 RecyclerView 中的 Item

    public HistoryArticleAdapter(Context context, List<ReviewedArticle> articleList) {
        this.context = context;
        if (articleList == null) {
            this.articleList = new ArrayList<ReviewedArticle>();
        } else {
            this.articleList = articleList;
        }
        mLayoutInflater = LayoutInflater.from(context);
    }

    /**
     * 根据不同的类别返回不同的ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mLayoutInflater.inflate(
                R.layout.item_article_normal, parent, false);

        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        ReviewedArticle article = articleList.get(position);

        HistoryViewHolder newHolder = (HistoryViewHolder) holder;
        newHolder.rcvArticleTitle.setText(article.title);
        newHolder.rcvArticleDate.setText(article.date + "发布");

        //注意这个阅读次数是 int 类型，需要转化为 String 类型
        newHolder.rcvArticleReadtimes.setText(article.read + "已阅");

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
    }


    @Override
    public int getItemCount() {

        if (articleList != null) {
            return articleList.size();
        } else {
            return 0;
        }
    }


    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {


        @InjectView(R.id.rcv_article_title)
        TextView rcvArticleTitle;
        @InjectView(R.id.rcv_article_date)
        TextView rcvArticleDate;
        @InjectView(R.id.rcv_article_readtimes)
        TextView rcvArticleReadtimes;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

}
