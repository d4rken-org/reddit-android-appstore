package subreddit.android.appstore.screens.details;

import android.os.Bundle;

import butterknife.ButterKnife;
import subreddit.android.appstore.R;
import subreddit.android.appstore.util.ui.BaseActivity;


public class AppDetailsActivity extends BaseActivity {
    public static final String ARG_KEY = "appInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appdetails_layout);
        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            AppDetailsFragment fragment = AppDetailsFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentFrame, fragment)
                    .commit();
        }
    }
}
