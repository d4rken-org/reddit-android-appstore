package subreddit.android.appstore.screens.navigation;

import subreddit.android.appstore.util.mvp.BasePresenter;
import subreddit.android.appstore.util.mvp.BaseView;


public interface NavigationContract {
    interface View extends BaseView {

        void showNavigationItems(NavigationData navigationData);
    }

    interface Presenter extends BasePresenter<View> {

    }
}
