package subreddit.android.appstore.backend;

import io.reactivex.Observable;
import subreddit.android.appstore.backend.data.AppInfo;

public interface TargetScraper {
    Observable<ScrapeResult> scrape(AppInfo appToScrape);
}
