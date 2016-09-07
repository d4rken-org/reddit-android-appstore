package subreddit.android.appstore.util.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import subreddit.android.appstore.AppStoreApp;


public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(((AppStoreApp) getApplication()).getSetTheme());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppStoreApp.getRefWatcher().watch(this);
    }
}
