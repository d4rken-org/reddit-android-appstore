package subreddit.android.appstore.backend;


import io.reactivex.Observable;
import subreddit.android.appstore.backend.data.AppInfo;

public class FakeScraper implements Scraper {
    @Override
    public Observable<ScrapeResult> scrape(AppInfo appToScrape) {
        return Observable.empty();
    }
}
