package subreddit.android.appstore.screens.navigation;

import subreddit.android.appstore.util.mvp.BasePresenter;
import subreddit.android.appstore.util.mvp.BaseView;


public interface NavigationContract {
    interface View extends BaseView {

        void showNavigationItems(NavigationData navigationData, CategoryFilter selectedFilter);

        void selectFilter(CategoryFilter filter);
    }

    interface Presenter extends BasePresenter<View> {
        void notifySelectedFilter(CategoryFilter categoryFilter);
    }
}
