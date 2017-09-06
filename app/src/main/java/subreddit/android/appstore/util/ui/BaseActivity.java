package subreddit.android.appstore.util.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import subreddit.android.appstore.AppStoreApp;


public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(((AppStoreApp) getApplication()).getSetTheme());
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        setTheme(((AppStoreApp) getApplication()).getSetTheme());
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppStoreApp.getRefWatcher().watch(this);
    }
}
