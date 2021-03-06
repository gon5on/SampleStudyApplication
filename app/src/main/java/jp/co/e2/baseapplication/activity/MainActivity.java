package jp.co.e2.baseapplication.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.WindowManager;

import jp.co.e2.baseapplication.R;
import jp.co.e2.baseapplication.fragment.HttpFragment;
import jp.co.e2.baseapplication.fragment.DbFragment;
import jp.co.e2.baseapplication.fragment.RegenerateFragment;
import jp.co.e2.baseapplication.fragment.ViewFragment;

/**
 * メインアクテビティ
 */
public class MainActivity extends BaseActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    /**
     * ${inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //キーボードを最初は出さない
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setContentView(R.layout.activity_common_navigation);

        //ツールバーセット
        setToolbar();

        //ドロワーセット
        setDrawer();

        if (savedInstanceState == null) {
            //初期フラグメントセット
            getSupportFragmentManager().beginTransaction().add(R.id.container, ViewFragment.newInstance()).commit();
        }
    }

    /**
     * ${inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle != null) {
            return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * ${inheritDoc}
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (mDrawerToggle != null) {
            mDrawerToggle.syncState();
        }
    }

    /**
     * ${inheritDoc}
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (mDrawerToggle != null) {
            mDrawerToggle.onConfigurationChanged(newConfig);
        }
    }

    /**
     * ${inheritDoc}
     */
    @Override
    public void onBackPressed(){
        //ドロワーが開いていたら、バックキーでドロワーを閉じる
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * ドロワーセット
     */
    protected void setDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);

        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.menu_view:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, ViewFragment.newInstance()).commit();
                            mDrawerLayout.closeDrawers();
                            break;
                        case R.id.menu_db:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, DbFragment.newInstance()).commit();
                            mDrawerLayout.closeDrawers();
                            break;
                        case R.id.menu_http:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, HttpFragment.newInstance()).commit();
                            mDrawerLayout.closeDrawers();
                            break;
                        case R.id.menu_lifecycle:
                            startActivity(new Intent(MainActivity.this, LifecycleActivity.class));
                            mDrawerLayout.closeDrawers();
                            break;
                        case R.id.menu_regenerate:
                            RegenerateFragment fragment = RegenerateFragment.newInstance("abcdefg");
                            fragment.mText = "abcdefg";     //fragmentのメンバ変数に直接値を代入する良くない例

                            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                            mDrawerLayout.closeDrawers();
                            break;
                    }

                    return false;
                }
            });
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        if (mDrawerLayout != null) {
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name);
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            mDrawerLayout.addDrawerListener(mDrawerToggle);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }
}