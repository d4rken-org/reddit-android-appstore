package subreddit.android.appstore.screens.details;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.view.MenuItem;

import subreddit.android.appstore.BuildConfig;
import subreddit.android.appstore.R;
import subreddit.android.appstore.util.ui.BaseActivity;

public class AboutActivity extends BaseActivity {

    protected static final String REDDIT_URL="https://www.reddit.com";
    protected static final String BUG_URL="https://github.com/d4rken/reddit-android-appstore/issues";

    NavigationView app_nav, contributor_nav;

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

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        app_nav = (NavigationView) findViewById(R.id.app_nav);
        app_nav.getMenu().findItem(0).setTitle(getResources().getString(R.string.version) + " " + BuildConfig.VERSION_NAME);
        app_nav.setNavigationItemSelectedListener(aboutListener);

        contributor_nav = (NavigationView) findViewById(R.id.contributor_nav);
        contributor_nav.setNavigationItemSelectedListener(contributorListener);
    }

    private void openInChrome(String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        builder.setSecondaryToolbarColor(getResources().getColor(R.color.colorPrimary));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }
}
