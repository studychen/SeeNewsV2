package com.studychen.seenews.ui.fragment.first;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.studychen.seenews.R;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zjq on 2015/6/30.
 */
public class ArticleFragment extends Fragment {
    private static final String SALE_PARAM = "param";
    @InjectView(R.id.tablayout_fragment_article)
    TabLayout tablayout;//Tablayout
    @InjectView(R.id.vp_fragment_article)
    ViewPager vp;//ViewPager

    private String mParam;

    private Activity mAct;//托管的Activity
    private final static String LATEST = "最新";
    private final static String MASTER = "研究生";
    private List<Fragment> fragments = new ArrayList<Fragment>();//放置Fragmetn
    private String[] titles = new String[]{LATEST, MASTER};//

    public static ArticleFragment newInstance(String param) {
        ArticleFragment fragment = new ArticleFragment();
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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, null);
        mAct = getActivity();
        //注入
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化Tablayout
        setTablayout();
        //初始化ViewPager
        setViewPager();
    }

    /**
     * 设置Tablayout
     */
    private void setTablayout() {
        tablayout.addTab(tablayout.newTab().setText(LATEST), true);
        tablayout.addTab(tablayout.newTab().setText(MASTER));
    }

    /**
     * 设置ViewPager
     */
    private void setViewPager() {
        //添加数据
        fragments.add(LatestArticleFragment.newInstance(""));
        fragments.add(MasterArticleFragment.newInstance(""));
        //设置适配器
        FragmentSaleAdapter adapter = new FragmentSaleAdapter(getChildFragmentManager());
        vp.setAdapter(adapter);
        //绑定tab
        tablayout.setupWithViewPager(vp);
        tablayout.setTabsFromPagerAdapter(adapter);
    }

    /**
     * 适配器
     */
    class FragmentSaleAdapter extends FragmentStatePagerAdapter {

        public FragmentSaleAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
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
     * 重置注入
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //重置ButterKnife
        ButterKnife.reset(this);
    }

}
