package com.studychen.seenews.ui.fragment.store;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.HandlerThread;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.studychen.seenews.R;
import com.studychen.seenews.adapter.ArticleAdapter;
import com.studychen.seenews.model.ItemArticle;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/8/28.
 */
public class ArticleNewsFragment extends Fragment {
    private static final String STORE_PARAM = "param";
    @InjectView(R.id.rcv_article)
    RecyclerView rcvArticle;

    //头部新闻图片 URL
    private String mParam;

   class MyHandler extends HandlerThread {

       public MyHandler(String name) {
           super(name);
       }
   }
    //下面新闻图文数据
    private List<ItemArticle> itemArticleList = new ArrayList<ItemArticle>();

    //获取 fragment 依赖的 Activity，方便使用 Context
    private Activity mAct;


    public static Fragment newInstance(String param) {
        ArticleNewsFragment fragment = new ArticleNewsFragment();
        Bundle args = new Bundle();
        args.putString(STORE_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getString(STORE_PARAM);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article, null);
        Log.i(STORE_PARAM, "in StoreFragment");
        mAct = getActivity();
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rcvArticle.setLayoutManager(new LinearLayoutManager(mAct));//这里用线性显示 类似于listview
//        rcvArticle.setLayoutManager(new GridLayoutManager(mAct, 2));//这里用线性宫格显示 类似于grid view
//        rcvArticle.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));//这里用线性宫格显示 类似于瀑布流

        new LatestArticleTask().execute();

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

    class LatestArticleTask extends AsyncTask<String, Void, List<ItemArticle>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<ItemArticle> doInBackground(String... params) {
            ItemArticle storeInfo1 =
                    new ItemArticle(20123, "http://i2.sinaimg.cn/ent/j/2012-05-20/U5912P28T3D3634984F328DT20120520152700.JPG", "关于举办《经典音乐作品欣赏与人文审美》讲座的通知", "2015-01-09", 1129, "科学研究院",
                            "讲座主要内容：以中、西方音乐历史中经典音乐作品为基础，通过作曲家及作品创作背景、相关音乐文化史知识及音乐欣赏常识...");
            ItemArticle storeInfo2 =
                    new ItemArticle(20123, "http://i2.sinaimg.cn/ent/j/2012-05-20/U5912P28T3D3634984F328DT20120520152700.JPG", "关于举办《经典音乐作品欣赏与人文审美》讲座的通知", "2015-01-09", 1129, "科学研究院",
                            "讲座主要内容：以中、西方音乐历史中经典音乐作品为基础，通过作曲家及作品创作背景、相关音乐文化史知识及音乐欣赏常识...");
            ItemArticle storeInfo3 =
                    new ItemArticle(20123, "http://i2.sinaimg.cn/ent/j/2012-05-20/U5912P28T3D3634984F328DT20120520152700.JPG", "关于举办《经典音乐作品欣赏与人文审美》讲座的通知", "2015-01-09", 1129, "科学研究院",
                            "讲座主要内容：以中、西方音乐历史中经典音乐作品为基础，通过作曲家及作品创作背景、相关音乐文化史知识及音乐欣赏常识...");
            ItemArticle storeInfo4 =
                    new ItemArticle(20123, "http://i2.sinaimg.cn/ent/j/2012-05-20/U5912P28T3D3634984F328DT20120520152700.JPG", "关于举办《经典音乐作品欣赏与人文审美》讲座的通知", "2015-01-09", 1129, "科学研究院",
                            "讲座主要内容：以中、西方音乐历史中经典音乐作品为基础，通过作曲家及作品创作背景、相关音乐文化史知识及音乐欣赏常识...");
            ItemArticle storeInfo5 =
                    new ItemArticle(20123, "http://i2.sinaimg.cn/ent/j/2012-05-20/U5912P28T3D3634984F328DT20120520152700.JPG", "关于举办《经典音乐作品欣赏与人文审美》讲座的通知", "2015-01-09", 1129, "科学研究院",
                            "讲座主要内容：以中、西方音乐历史中经典音乐作品为基础，通过作曲家及作品创作背景、相关音乐文化史知识及音乐欣赏常识...");
            ItemArticle storeInfo6 =
                    new ItemArticle(20123, "http://i2.sinaimg.cn/ent/j/2012-05-20/U5912P28T3D3634984F328DT20120520152700.JPG", "关于举办《经典音乐作品欣赏与人文审美》讲座的通知", "2015-01-09", 1129, "科学研究院",
                            "讲座主要内容：以中、西方音乐历史中经典音乐作品为基础，通过作曲家及作品创作背景、相关音乐文化史知识及音乐欣赏常识...");
            itemArticleList.add(storeInfo1);
            itemArticleList.add(storeInfo2);
            itemArticleList.add(storeInfo3);
            itemArticleList.add(storeInfo4);
            itemArticleList.add(storeInfo5);
            itemArticleList.add(storeInfo6);
            return itemArticleList;
        }

        @Override
        protected void onPostExecute(List<ItemArticle> data) {
            super.onPostExecute(data);
            ArticleAdapter adapter = new ArticleAdapter(mAct, data);
            rcvArticle.setAdapter(adapter);
        }
    }

}
