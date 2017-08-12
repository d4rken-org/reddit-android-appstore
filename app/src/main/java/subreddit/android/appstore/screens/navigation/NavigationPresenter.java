package subreddit.android.appstore.screens.navigation;

import android.os.Bundle;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import subreddit.android.appstore.BuildConfig;
import subreddit.android.appstore.backend.data.AppInfo;
import subreddit.android.appstore.backend.github.GithubApi;
import subreddit.android.appstore.backend.github.GithubRepository;
import subreddit.android.appstore.backend.reddit.wiki.WikiRepository;
import subreddit.android.appstore.util.VersionHelper;
import timber.log.Timber;


public class NavigationPresenter implements NavigationContract.Presenter {
    final WikiRepository repository;
    final GithubRepository githubRepository;

    NavigationContract.View view;
    private Disposable categoryUpdater;
    CategoryFilter currentCategoryFilter = new CategoryFilter();
    Disposable updateCheck;
    GithubApi.Release release;

    public NavigationPresenter(WikiRepository repository, GithubRepository githubRepository) {
        this.repository = repository;
        this.githubRepository = githubRepository;
    }

    @Override
    public void onCreate(Bundle bundle) {

    }

    @Override
    public void onAttachView(NavigationContract.View view) {

        this.view = view;
        categoryUpdater = repository.getAppList()
                .observeOn(Schedulers.computation())
                .map(appInfos -> {
                    NavigationData navData = new NavigationData();
                    for (AppInfo appInfo : appInfos) navData.addApp(appInfo);
                    return navData;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(navigationData -> {
                    Timber.d("showNavigationItems(%s)", navigationData);
                    NavigationPresenter.this.view.showNavigationItems(navigationData, currentCategoryFilter);
                });

        githubRepository.getLatestRelease()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GithubApi.Release>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        updateCheck = d;
                    }

                    @Override
                    public void onNext(GithubApi.Release release) {
                        NavigationPresenter.this.release = release;
                        if (VersionHelper.versionCompare(BuildConfig.VERSION_NAME, release.tagName) < 0) {
                            Timber.d("Update available, current: %s, new: %s", BuildConfig.VERSION_NAME, release.tagName);
                            view.showUpdateSnackbar(release);
                            view.enableUpdateAvailableText(release);
                        } else Timber.d("No newer version available");
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showUpdateErrorToast();
                        Timber.e(e, "Error during update check");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onDetachView() {
        categoryUpdater.dispose();
        updateCheck.dispose();
        view = null;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void notifySelectedFilter(CategoryFilter categoryFilter) {
        currentCategoryFilter = categoryFilter;
    }

    @Override
    public void downloadUpdate(GithubApi.Release release) {
        // TODO we could directory use Androids DownloadManager.class
        view.showDownload(release.assets.get(0).downloadUrl);
    }

    @Override
    public void buildChangelog(GithubApi.Release release) {
        view.showChangelog(release);
    }
}
