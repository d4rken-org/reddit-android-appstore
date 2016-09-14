package subreddit.android.appstore.screens.navigation;

import android.os.Bundle;

import java.util.Collection;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import subreddit.android.appstore.backend.data.AppInfo;
import subreddit.android.appstore.backend.wiki.WikiRepository;
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
                .map(new Function<Collection<AppInfo>, NavigationData>() {
                    @Override
                    public NavigationData apply(Collection<AppInfo> appInfos) throws Exception {
                        NavigationData navData = new NavigationData();
                        for (AppInfo appInfo : appInfos) navData.addApp(appInfo);
                        return navData;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<NavigationData>() {
                    @Override
                    public void accept(NavigationData navigationData) throws Exception {
                        Timber.d("showNavigationItems(%s)", navigationData);
                        NavigationPresenter.this.view.showNavigationItems(navigationData, currentCategoryFilter);
                    }
                });
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
