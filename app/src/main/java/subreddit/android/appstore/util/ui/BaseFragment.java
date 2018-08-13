package subreddit.android.appstore.util.ui;

import android.support.v4.app.Fragment;

import subreddit.android.appstore.AppStoreApp;

public class BaseFragment extends Fragment {

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppStoreApp.getRefWatcher().watch(this);
    }
}
