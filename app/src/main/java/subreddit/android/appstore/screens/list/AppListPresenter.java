package subreddit.android.appstore.screens.list;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collection;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.ReplaySubject;
import subreddit.android.appstore.AppStoreApp;
import subreddit.android.appstore.backend.AppInfo;
import subreddit.android.appstore.backend.WikiRepository;
import timber.log.Timber;


public class AppListPresenter implements AppListContract.Presenter {
    static final String TAG = AppStoreApp.LOGPREFIX + "AppListPresenter";
    final WikiRepository repository;
    final ReplaySubject<Collection<AppInfo>> appInfoSubject;
    Subscription viewSubscription;
    AppListContract.View view;

    public AppListPresenter(WikiRepository repository) {
        this.repository = repository;
        appInfoSubject = ReplaySubject.createWithSize(1);
    }

    @Override
    public void onCreate(Bundle bundle) {

    }

    @Override
    public void onAttachView(AppListContract.View view) {
        this.view = view;
        viewSubscription = appInfoSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Collection<AppInfo>>() {
                    @Override
                    public void call(Collection<AppInfo> appInfos) {
                        // TODO sorting
                        AppListPresenter.this.view.showAppList(new ArrayList<>(appInfos));
                    }
                });
        if (appInfoSubject.size() == 0) refreshData();
    }

    @Override
    public void onDetachView() {
        viewSubscription.unsubscribe();
        viewSubscription = null;
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
        repository.getAppList()
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Collection<AppInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO error handling
                        Timber.tag(TAG).e(e, null);
                    }

                    @Override
                    public void onNext(Collection<AppInfo> appInfos) {
                        appInfoSubject.onNext(appInfos);
                    }
                });
    }
}
