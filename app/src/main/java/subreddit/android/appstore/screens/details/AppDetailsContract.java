package subreddit.android.appstore.screens.details;

import subreddit.android.appstore.backend.AppInfo;
import subreddit.android.appstore.util.mvp.BasePresenter;
import subreddit.android.appstore.util.mvp.BaseView;


public class AppDetailsContract {
    interface View extends BaseView {

        void onShowDetails(AppInfo appInfo);

    }

    interface Presenter extends BasePresenter<View> {

    }
}
