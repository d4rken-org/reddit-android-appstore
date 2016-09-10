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
import de.psdev.licensesdialog.LicensesDialog;
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20;
import de.psdev.licensesdialog.licenses.BSD2ClauseLicense;
import de.psdev.licensesdialog.model.Notice;
import de.psdev.licensesdialog.model.Notices;
import subreddit.android.appstore.BuildConfig;
import subreddit.android.appstore.R;
import subreddit.android.appstore.util.ui.BaseActivity;

public class AboutActivity extends BaseActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.app_nav) NavigationView app_nav;
    @BindView(R.id.contributor_nav) NavigationView contributor_nav;
    @BindView(R.id.about_toolbar) Toolbar mToolbar;

    protected static final String REDDIT_URL = "https://www.reddit.com";
    protected static final String BUG_URL = "https://github.com/d4rken/reddit-android-appstore/issues";

    NavigationView.OnNavigationItemSelectedListener contributorListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            openInChrome(REDDIT_URL + item.getTitle());
            return false;
        }
    };

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reportbug: {
                openInChrome(BUG_URL);
                break;
            }
            case R.id.licenses: {
                Notices notices = new Notices();
                notices.addNotice(new Notice("Fastscroll","https://github.com/FutureMind/recycler-fast-scroll","Copyright 2015 Future Mind", new ApacheSoftwareLicense20()));
                notices.addNotice(new Notice("Glide","https://github.com/bumptech/glide",null, new BSD2ClauseLicense()));
                notices.addNotice(new Notice("RxJava","https://github.com/ReactiveX/RxJava","Copyright 2013 Netflix, Inc.", new ApacheSoftwareLicense20()));
                notices.addNotice(new Notice("Tomorrow MVP","https://github.com/michal-luszczuk/tomorrow-mvp",null, new ApacheSoftwareLicense20()));
                notices.addNotice(new Notice("GSon","https://github.com/google/gson","Copyright 2008 Google Inc.",new ApacheSoftwareLicense20()));
                notices.addNotice(new Notice("Dagger","https://github.com/square/dagger","Copyright 2012 Square, Inc.", new ApacheSoftwareLicense20()));
                notices.addNotice(new Notice("Butterknife","https://github.com/JakeWharton/butterknife","Copyright 2013 Jake Wharton", new ApacheSoftwareLicense20()));
                notices.addNotice(new Notice("Timber","https://github.com/JakeWharton/timber","Copyright 2013 Jake Wharton", new ApacheSoftwareLicense20()));
                notices.addNotice(new Notice("Flow Layout","https://github.com/blazsolar/FlowLayout","Copyright 2013 Blaž Šolar",new ApacheSoftwareLicense20()));
                notices.addNotice(new Notice("OkHttp", "http://square.github.io/okhttp/", "Copyright 2016 Square, Inc.", new ApacheSoftwareLicense20()));
                notices.addNotice(new Notice("Android Support Libraries", "https://github.com/android/platform_frameworks_support", null, new ApacheSoftwareLicense20()));
                notices.addNotice(new Notice("LeakCanary","https://github.com/square/leakcanary","Copyright 2015 Square, Inc.", new ApacheSoftwareLicense20()));
                notices.addNotice(new Notice("License Dialog","https://github.com/PSDev/LicensesDialog","Copyright 2013-2016 Philip Schiffer", new ApacheSoftwareLicense20()));
                new LicensesDialog.Builder(this)
                        .setNotices(notices)
                        .setTitle(R.string.licenses)
                        .build()
                        .show();
                break;
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_48px);
        mToolbar.setNavigationOnClickListener(this);

        app_nav.getMenu().findItem(0).setTitle(getResources().getString(R.string.version) + " " + BuildConfig.VERSION_NAME);
        app_nav.setNavigationItemSelectedListener(this);

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
