package subreddit.android.appstore.backend;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import subreddit.android.appstore.backend.data.AppInfo;

public interface Scraper {
    @NonNull
    Observable<ScrapeResult> get(@NonNull AppInfo appToScrape);
}
