package subreddit.android.appstore.backend.scrapers.gplay;


import io.reactivex.Observable;
import subreddit.android.appstore.backend.ScrapeResult;
import subreddit.android.appstore.backend.TargetScraper;
import subreddit.android.appstore.backend.data.AppInfo;

public class GPlayScraper implements TargetScraper {
    @Override
    public Observable<ScrapeResult> scrape(AppInfo appToScrape) {
        return null;
    }
}
