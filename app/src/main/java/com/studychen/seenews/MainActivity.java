package com.studychen.seenews;

import android.app.UiModeManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.studychen.seenews.util.NightModeHelper;
import com.studychen.seenews.util.PrefUtils;
import com.studychen.seenews.ui.fragment.first.ArticleFragmentContainer;
import com.studychen.seenews.ui.fragment.second.TestNightFragment;
import com.studychen.seenews.ui.fragment.store.ArticleNewsFragment;


import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String savedTab = "savedTab";
    private static final String Log_FILTER = "param";
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.fragment_container)
    FrameLayout fragmentContainer;
    @InjectView(R.id.rb_tab_common)
    RadioButton rbTabCommon;
    @InjectView(R.id.rb_tab_sale)
    RadioButton rbTabSale;
    @InjectView(R.id.rb_tab_visit)
    RadioButton rbTabVisit;
    @InjectView(R.id.nav_view)
    NavigationView navView;
    @InjectView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @InjectView(R.id.main_radios)
    RadioGroup mainRadios;

    private boolean isNightMode = true;
    private ActionBarDrawerToggle toggle;//代替监听器
    private UiModeManager uiManager;

    private int mThemeId;

    private NightModeHelper mNightModeHelper;

    private FragmentPagerAdapter mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            if (position == R.id.rb_tab_common) {
                return ArticleFragmentContainer.newInstance("FirstFragment");
            } else if (position == R.id.rb_tab_sale) {
                return TestNightFragment.newInstance("SecondFragment");
            } else {
                return ArticleNewsFragment.newInstance("StoreFragment");
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    //这儿是3个大的 fragment
    @OnCheckedChanged({R.id.rb_tab_common, R.id.rb_tab_sale, R.id.rb_tab_visit})
    public void onChecked(RadioButton rb) {
        Boolean isChecked = rb.isChecked();
        //检查是否选中
        if (isChecked) {
            int viewId = rb.getId();
            //instantiateItem从FragmentManager中查找Fragment，找不到就getItem新建一个
            Fragment fragment =
                    (Fragment) mFragmentPagerAdapter.instantiateItem(fragmentContainer, viewId);
            mFragmentPagerAdapter.setPrimaryItem(fragmentContainer, viewId, fragment);
            mFragmentPagerAdapter.finishUpdate(fragmentContainer);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        navView.setNavigationItemSelectedListener(this);


        //onSaveInstanceState会保存已点击 tab 的 id
        if (savedInstanceState == null) {
            //默认将第一个RadioButton设为选中
            Log.i(Log_FILTER, "savedInstanceState  null");
            rbTabCommon.performClick();
        } else {
            Log.i(Log_FILTER, "savedInstanceState not null");

            RadioButton radioButton = (RadioButton) findViewById(savedInstanceState.getInt(savedTab));
            Log.i(Log_FILTER, radioButton.getText() + "");

            radioButton.performClick();
        }

        Log.i(Log_FILTER, "in main onCreate");

        initToolbar();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    /**
     * 设置ToolBar
     */
    protected void initToolbar() {

        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.drawable.ic_menu);

        //setTile 要在下面这句话上面，不然会失效
        setSupportActionBar(toolbar);


        //监听DrawerLayout
        //将抽屉事件和 toolbar联系起来，这是 material design 的设计
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.setDrawerListener(toggle);
    }

    //设置搜索框
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:
                showSingeChoiceDialog();
                return true;
            case R.id.men_action_change_mode:
                PrefUtils.setDarkMode(!PrefUtils.isDarkMode());
                MainActivity.this.recreate();//重新创建当前Activity实例
                Toast.makeText(this, "yenight", Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 搜索跳转
     */
    private void showSingeChoiceDialog() {
        int colorId = R.color.accent;
        if (PrefUtils.isDarkMode()) {
            colorId = R.color.colorAccentDarkTheme;
        }
        
        new MaterialDialog.Builder(this).title(R.string.action_search_title)
                .items(R.array.search_content_main)
                .itemColorRes(colorId)
                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which,
                                               CharSequence text) {
                        return true;
                    }

                })
                .positiveText(R.string.search_positive_btn_main)
                .show();
    }

    /**
     * 回退键弹窗
     */
    @Override
    public void onBackPressed() {
        new MaterialDialog.Builder(this).iconRes(
                R.mipmap.ic_launcher).limitIconToDefaultSize() // limits the displayed icon size to 48dp
                .title(R.string.exit_app_title)
                .content(R.string.exit_app_hint)
                .positiveText(R.string.exit_app_positive)
                .negativeText(R.string.exit_app_negative)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        finish();
                        //退出应用程序
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                    }
                })
                .show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(savedTab, mainRadios.getCheckedRadioButtonId());
        RadioButton radioButton = (RadioButton) findViewById(mainRadios.getCheckedRadioButtonId());

        Log.i(Log_FILTER, "in main onSaveInstanceState");
        //删除下面这行，不然容易发生重影
//        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestart() {
        Log.i(Log_FILTER, "in main onRestart");
        super.onRestart();
    }

    @Override
    protected void onStart() {
        Log.i(Log_FILTER, "in main onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.i(Log_FILTER, "in main onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i(Log_FILTER, "in main onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i(Log_FILTER, "in main onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i(Log_FILTER, "in main onDestroy");
        super.onDestroy();
        ButterKnife.reset(this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
//        if (menuItem.isChecked()) {
//            menuItem.setChecked(false);
//        } else {
//            menuItem.setChecked(true);
//        }

        int id = menuItem.getItemId();
        return true;
    }

}
