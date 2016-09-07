package subreddit.android.appstore.screens.details;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import subreddit.android.appstore.R;
import subreddit.android.appstore.util.ui.BaseActivity;


public class AppDetailsActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.details_toolbar) Toolbar mToolbar;
    public static final String ARG_KEY = "appInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appdetails_layout);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_48px);
        mToolbar.setNavigationOnClickListener(this);
        if (savedInstanceState == null) {
            AppDetailsFragment fragment = AppDetailsFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentFrame, fragment)
                    .commit();
        }

    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
