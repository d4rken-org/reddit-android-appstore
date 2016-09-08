package subreddit.android.appstore.screens.list;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import subreddit.android.appstore.AppStoreApp;
import subreddit.android.appstore.backend.WikiRepository;
import subreddit.android.appstore.backend.data.AppInfo;
import timber.log.Timber;


public class AppListPresenter implements AppListContract.Presenter {
    static final String TAG = AppStoreApp.LOGPREFIX + "AppListPresenter";
    final WikiRepository repository;
    Disposable listUpdater;
    AppListContract.View view;
    Disposable filterUpdater;

    public AppListPresenter(WikiRepository repository) {
        this.repository = repository;
    }

    @Override
    public void onCreate(Bundle bundle) {

    }

    @Override
    public void onAttachView(AppListContract.View view) {
        this.view = view;
        view.showLoadingScreen();
        listUpdater = repository.getAppList()
                .observeOn(Schedulers.computation())
                .map(new Function<Collection<AppInfo>, List<AppInfo>>() {
                    @Override
                    public List<AppInfo> apply(Collection<AppInfo> appInfos) throws Exception {
                        ArrayList<AppInfo> data = new ArrayList<>(appInfos);
                        Collections.sort(data);
                        return data;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<AppInfo>>() {
                    @Override
                    public void accept(List<AppInfo> appInfos) throws Exception {
                        Timber.tag(TAG).d("showAppList(%s items)", appInfos.size());
                        AppListPresenter.this.view.showAppList(appInfos);
                    }
                });
        filterUpdater = repository.getAppList()
                .observeOn(Schedulers.computation())
                .map(new Function<Collection<AppInfo>, TagMap>() {
                    @Override
                    public TagMap apply(Collection<AppInfo> appInfos) throws Exception {
                        return new TagMap(appInfos);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<TagMap>() {
                    @Override
                    public void accept(TagMap tagMap) throws Exception {
                        Timber.tag(TAG).d("updateTagCount(%s)", tagMap);
                        AppListPresenter.this.view.updateTagCount(tagMap);
                    }
                });
    }

    @Override
    public void onDetachView() {
        listUpdater.dispose();
        filterUpdater.dispose();
        view = null;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {

    }

    @Override
    public void onDestroy() {

    }


    @Override
    public void refreshData() {
        view.showLoadingScreen();
        repository.refresh();
    }
}
