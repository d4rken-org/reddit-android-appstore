package subreddit.android.appstore.backend.scrapers;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import subreddit.android.appstore.backend.data.AppInfo;

public interface MediaScraper {
    @NonNull
    Observable<ScrapeResult> get(@NonNull AppInfo appToScrape);
}
