package subreddit.android.appstore.backend;


import io.reactivex.Observable;
import subreddit.android.appstore.backend.data.AppInfo;
import subreddit.android.appstore.backend.scrapers.ScrapeResult;
import subreddit.android.appstore.backend.scrapers.Scraper;

public class FakeScraper implements Scraper {
    @Override
    public Observable<ScrapeResult> get(AppInfo appToScrape) {
        return Observable.empty();
    }
}
