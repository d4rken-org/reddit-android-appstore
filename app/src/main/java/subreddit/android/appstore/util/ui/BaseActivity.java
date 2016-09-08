package subreddit.android.appstore.util.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import subreddit.android.appstore.AppStoreApp;


public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(((AppStoreApp) getApplication()).getSetTheme());
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setTheme(((AppStoreApp) getApplication()).getSetTheme());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppStoreApp.getRefWatcher().watch(this);
    }
}
