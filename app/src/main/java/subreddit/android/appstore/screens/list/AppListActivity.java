package subreddit.android.appstore.screens.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import subreddit.android.appstore.R;
import subreddit.android.appstore.screens.details.SettingsActivity;
import subreddit.android.appstore.util.ui.BaseActivity;


public class AppListActivity extends BaseActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.main_drawer) DrawerLayout mDrawer;
    @BindView(R.id.applist_toolbar) Toolbar mToolbar;
    @BindView(R.id.footer_nav) NavigationView nav_footer;
    @BindView(R.id.content_nav) NavigationView content_nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applist_layout);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_menu_white_48px);
        mToolbar.setNavigationOnClickListener(this);

        if (savedInstanceState == null) {
            AppListFragment fragment = AppListFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentFrame, fragment)
                    .commit();
        }

        nav_footer.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(this,SettingsActivity.class));
        return false;
    }

    @Override
    public void onClick(View view) {
        mDrawer.openDrawer(GravityCompat.START);
    }
}