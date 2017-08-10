package subreddit.android.appstore.screens.navigation;

import android.support.annotation.Nullable;

import subreddit.android.appstore.backend.github.GithubApi;
import subreddit.android.appstore.util.mvp.BasePresenter;
import subreddit.android.appstore.util.mvp.BaseView;


public interface NavigationContract {
    interface View extends BaseView {

        void showNavigationItems(NavigationData navigationData, CategoryFilter selectedFilter);

        void selectFilter(CategoryFilter filter);

        void showUpdateSnackbar(@Nullable GithubApi.Release release);

        void showUpdateErrorToast();

        void enableUpdateAvailableText(@Nullable GithubApi.Release release);

        void showDownload(String url);

        void showChangelog(GithubApi.Release release);
    }

    interface Presenter extends BasePresenter<View> {
        void notifySelectedFilter(CategoryFilter categoryFilter);

        void downloadUpdate(GithubApi.Release release);

        void buildChangelog(GithubApi.Release release);
    }
}
