package subreddit.android.appstore.screens.navigation;

import android.os.Bundle;

import java.util.Collection;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import subreddit.android.appstore.backend.data.AppInfo;
import subreddit.android.appstore.backend.reddit.wiki.WikiRepository;
import timber.log.Timber;


public class NavigationPresenter implements NavigationContract.Presenter {
    final WikiRepository repository;

    NavigationContract.View view;
    private Disposable categoryUpdater;
    CategoryFilter currentCategoryFilter = new CategoryFilter();

    public NavigationPresenter(WikiRepository repository) {
        this.repository = repository;
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
    }

    public void showUpdateSnackbar() {
        view.showUpdateSnackbar();
    }

    public void enableUpdateAvailable() {
        view.enableUpdateAvailableText();
    }

    @Override
    public void onDetachView() {
        categoryUpdater.dispose();
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
}
