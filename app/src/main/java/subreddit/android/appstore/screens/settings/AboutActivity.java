package subreddit.android.appstore.screens.settings;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import subreddit.android.appstore.BuildConfig;
import subreddit.android.appstore.R;
import subreddit.android.appstore.util.ui.BaseActivity;

public class AboutActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.app_nav) NavigationView app_nav;
    @BindView(R.id.contributor_nav) NavigationView contributor_nav;
    @BindView(R.id.about_toolbar) Toolbar mToolbar;

    protected static final String REDDIT_URL = "https://www.reddit.com";
    protected static final String BUG_URL = "https://github.com/d4rken/reddit-android-appstore/issues";

    NavigationView.OnNavigationItemSelectedListener aboutListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.reportbug: {
                    openInChrome(BUG_URL);
                    break;
                }
                case R.id.licenses: {
                    //TODO: open a license dialog
                    break;
                }
            }
            return false;
        }
    };

    NavigationView.OnNavigationItemSelectedListener contributorListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            openInChrome(REDDIT_URL + item.getTitle());
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_48px);
        mToolbar.setNavigationOnClickListener(this);

        app_nav.getMenu().findItem(0).setTitle(getResources().getString(R.string.version) + " " + BuildConfig.VERSION_NAME);
        app_nav.setNavigationItemSelectedListener(aboutListener);

        contributor_nav.setNavigationItemSelectedListener(contributorListener);
    }

    @Override
    public void onClick(View view) {
        finish();
    }

    void openInChrome(String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        builder.setSecondaryToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }
}
