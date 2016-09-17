package subreddit.android.appstore.screens.details;

import android.content.SharedPreferences;
import android.os.Bundle;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import subreddit.android.appstore.backend.data.AppInfo;
import subreddit.android.appstore.backend.scrapers.MediaScraper;
import subreddit.android.appstore.backend.scrapers.ScrapeResult;
import subreddit.android.appstore.screens.settings.SettingsActivity;


public class AppDetailsPresenter implements AppDetailsContract.Presenter {
    AppDetailsContract.View view;
    private final SharedPreferences preferences;
    private final MediaScraper mediaScraper;
    AppInfo appInfoItem;
    Disposable disposable;

    public AppDetailsPresenter(SharedPreferences preferences, MediaScraper mediaScraper, AppInfo appInfoItem) {
        this.preferences = preferences;
        this.mediaScraper = mediaScraper;
        this.appInfoItem = appInfoItem;
    }

    @Override
    public void onCreate(Bundle bundle) {

    }

    @Override
    public void onAttachView(AppDetailsContract.View view) {
        this.view = view;
        view.displayDetails(appInfoItem);

        if (preferences.getBoolean(SettingsActivity.PREF_KEY_LOAD_MEDIA, true)) {
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
                            AppDetailsPresenter.this.view.displayIcon(appInfoItem);
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
