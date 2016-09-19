package subreddit.android.appstore.screens.navigation;

import android.support.annotation.Nullable;

import subreddit.android.appstore.backend.github.SelfUpdater;
import subreddit.android.appstore.util.mvp.BasePresenter;
import subreddit.android.appstore.util.mvp.BaseView;


public interface NavigationContract {
    interface View extends BaseView {

        void showNavigationItems(NavigationData navigationData, CategoryFilter selectedFilter);

        void selectFilter(CategoryFilter filter);

        void showUpdateSnackbar(@Nullable SelfUpdater.Release release);

        void enableUpdateAvailableText(@Nullable SelfUpdater.Release release);

        void showDownload(String url);
    }

    interface Presenter extends BasePresenter<View> {
        void notifySelectedFilter(CategoryFilter categoryFilter);

        void downloadUpdate(SelfUpdater.Release release);
    }
}
