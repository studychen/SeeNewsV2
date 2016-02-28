package com.studychen.seenews.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.facebook.drawee.view.SimpleDraweeView;
import com.studychen.seenews.R;
import com.studychen.seenews.model.ReviewedArticle;
import com.studychen.seenews.util.Constant;
import com.studychen.seenews.util.GetTimeAgo;
import com.studychen.seenews.util.OnDeleteClickLitener;
import com.studychen.seenews.util.OnItemClickLitener;
import com.studychen.seenews.util.OnItemLongClickLitener;

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
    private OnItemLongClickLitener mOnItemLongClickLitener;//点击 RecyclerView 中的 Item
    private OnDeleteClickLitener mOnDeleteClickLitener;//点击 RecyclerView 中的 Item

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
                R.layout.item_article_history, parent, false);

        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        ReviewedArticle article = articleList.get(position);

        HistoryViewHolder newHolder = (HistoryViewHolder) holder;

        newHolder.rcvArticlePhoto.setImageURI(Uri.parse(article.photoKey));

        newHolder.rcvArticleTitle.setText(article.title);

        newHolder.rcvClickTime.setText(GetTimeAgo.getTimeAgo(article.clickTime));

        newHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);


        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            newHolder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(pos);
                }
            });
        }

        // 如果设置了长按的回调方法
        if (mOnItemLongClickLitener != null) {
            newHolder.swipeLayout.getSurfaceView().setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemLongClickLitener.onItemLongClick(pos);
                    return false;
                }

            });
        }

        if (mOnDeleteClickLitener != null) {
            newHolder.cvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnDeleteClickLitener.onDeleteClick(pos);
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


    public void setOnItemLongClickLitener(OnItemLongClickLitener mOnItemLongClickLitener) {
        this.mOnItemLongClickLitener = mOnItemLongClickLitener;
    }

    public void setOnDeleteClickLitener(OnDeleteClickLitener mOnDeleteClickLitener) {
        this.mOnDeleteClickLitener = mOnDeleteClickLitener;
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {


        @InjectView(R.id.rcv_article_title)
        TextView rcvArticleTitle;
        @InjectView(R.id.rcv_article_photo)
        SimpleDraweeView rcvArticlePhoto;
        @InjectView(R.id.rcv_click_time)
        TextView rcvClickTime;
        @InjectView(R.id.swipe_layout)
        SwipeLayout swipeLayout;
        @InjectView(R.id.cv_Delete)
        CardView cvDelete;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

}
