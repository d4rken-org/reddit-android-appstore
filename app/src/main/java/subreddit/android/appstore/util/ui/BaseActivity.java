package subreddit.android.appstore.util.ui;

import android.support.v7.app.AppCompatActivity;

import subreddit.android.appstore.AppStoreApp;


public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppStoreApp.getRefWatcher().watch(this);
    }
}
