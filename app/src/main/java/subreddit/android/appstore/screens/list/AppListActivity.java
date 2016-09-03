package subreddit.android.appstore.screens.list;

import android.os.Bundle;

import subreddit.android.appstore.R;
import subreddit.android.appstore.util.ui.BaseActivity;


public class AppListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applist_layout);
        if (savedInstanceState == null) {
            AppListFragment fragment = AppListFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentFrame, fragment)
                    .commit();
        }
    }
}
