package subreddit.android.appstore.screens.details;

import android.os.Bundle;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import subreddit.android.appstore.AppStoreApp;
import subreddit.android.appstore.backend.data.AppInfo;
import subreddit.android.appstore.backend.scrapers.MediaScraper;
import subreddit.android.appstore.backend.scrapers.ScrapeResult;


public class AppDetailsPresenter implements AppDetailsContract.Presenter {
    static final String TAG = AppStoreApp.LOGPREFIX + "AppDetailsPresenter";
    AppDetailsContract.View view;
    private final MediaScraper mediaScraper;
    private AppInfo appInfoItem;
    Disposable disposable;

    public AppDetailsPresenter(MediaScraper mediaScraper, AppInfo appInfoItem) {
        this.mediaScraper = mediaScraper;
        this.appInfoItem = appInfoItem;
    }

    @Override
    public void onCreate(Bundle bundle) {

    }

    @Override
    public void onAttachView(AppDetailsContract.View view) {
        this.view = view;
        if (appInfoItem == null) view.closeDetails();
        else view.displayDetails(appInfoItem);
        mediaScraper.get(appInfoItem)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ScrapeResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(ScrapeResult scrapeResult) {
                        AppDetailsPresenter.this.view.displayScreenshots(scrapeResult);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void onDetachView() {
        if (disposable != null) disposable.dispose();
        view = null;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {

    }

    @Override
    public void onDestroy() {

    }
}
