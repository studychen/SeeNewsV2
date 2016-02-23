package com.studychen.seenews.ui.fragment.first;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.studychen.seenews.R;
import com.studychen.seenews.util.ColumnName;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zjq on 2015/6/30.
 */
public class ArticleFragmentContainer extends Fragment {
    private static final String PARAM = "param";

    private static final String LOG = "PAGER_LOG";

    @InjectView(R.id.tablayout_article)
    TabLayout tablayout;//Tablayout
    @InjectView(R.id.viewpager_article)
    ViewPager vp;//ViewPager
    private String mParam;
    private Activity mAct;//托管的Activity
    private String[] mTitles = new String[]{ColumnName.LATEST, ColumnName.NOTIFIC,
            ColumnName.BACHELOR, ColumnName.MASTER,
            ColumnName.ACADEMIC, ColumnName.JOB};//

    public static ArticleFragmentContainer newInstance(String param) {
        ArticleFragmentContainer fragment = new ArticleFragmentContainer();
        Bundle args = new Bundle();
        args.putString(PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getString(PARAM);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_first_container, null);
        mAct = getActivity();
        //注入
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化Tablayout
//        setTablayout();
        //初始化ViewPager
        setViewPager();
    }

//    /**
//     * 设置Tablayout
//     */
//    private void setTablayout() {
//        tablayout.addTab(tablayout.newTab().setText(LATEST), true);
//        tablayout.addTab(tablayout.newTab().setText(MASTER));
//    }

    /**
     * 设置ViewPager
     */
    private void setViewPager() {
        //设置适配器
        ArticleFragmentAdapter adapter = new ArticleFragmentAdapter(getChildFragmentManager());
        vp.setAdapter(adapter);
        //绑定tab
        tablayout.setupWithViewPager(vp);
        tablayout.setTabsFromPagerAdapter(adapter);
    }

    /**
     * Fragment切换时隐藏控件
     *
     * @param menuVisible
     */
    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null) {
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 处理类库ButterKnife
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    /**
     * 适配器
     */
    class ArticleFragmentAdapter extends FragmentStatePagerAdapter {

        public ArticleFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //首页和其他页不同
            if (position == 0) {
                return LatestArticleFragment.newInstance("");
            } else {
                Log.i(LOG, "in getItem " + position);
                return OriginalArticleFragment.newInstance(position);
            }
        }

        @Override
        public int getCount() {
            return 6;
        }

        /**
         * 标签卡上方的标题
         *
         * @param position
         * @return
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }

}
