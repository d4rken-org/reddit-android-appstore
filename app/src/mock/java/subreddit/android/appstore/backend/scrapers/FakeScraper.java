package subreddit.android.appstore.backend.scrapers;


import android.support.annotation.NonNull;

import io.reactivex.Observable;
import subreddit.android.appstore.backend.data.AppInfo;
import subreddit.android.appstore.backend.scrapers.MediaScraper;
import subreddit.android.appstore.backend.scrapers.ScrapeResult;

public class FakeScraper implements MediaScraper {
    @NonNull
    @Override
    public Observable<ScrapeResult> get(@NonNull AppInfo appToScrape) {
        return Observable.empty();
    }
}
