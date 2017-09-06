package subreddit.android.appstore.screens.navigation;

import android.support.annotation.Nullable;

import subreddit.android.appstore.backend.github.GithubRepository;
import subreddit.android.appstore.util.mvp.BasePresenter;
import subreddit.android.appstore.util.mvp.BaseView;


public interface NavigationContract {
    interface View extends BaseView {

        void showNavigationItems(NavigationData navigationData, CategoryFilter selectedFilter);

        void selectFilter(CategoryFilter filter);

        void showUpdateSnackbar(@Nullable GithubRepository.Release release);

        void showUpdateErrorToast();

        void enableUpdateAvailableText(@Nullable GithubRepository.Release release);

        void showDownload(String url);

        void showChangelog(GithubRepository.Release release);
    }

    interface Presenter extends BasePresenter<View> {
        void notifySelectedFilter(CategoryFilter categoryFilter);

        void downloadUpdate(GithubRepository.Release release);

        void buildChangelog(GithubRepository.Release release);
    }
}
