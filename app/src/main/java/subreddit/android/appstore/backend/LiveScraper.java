package subreddit.android.appstore.backend;


import android.support.annotation.NonNull;

import io.reactivex.Observable;
import subreddit.android.appstore.AppStoreApp;
import subreddit.android.appstore.backend.data.AppInfo;
import subreddit.android.appstore.backend.data.Download;
import subreddit.android.appstore.backend.scrapers.gplay.GPlayScraper;
import timber.log.Timber;

public class LiveScraper implements Scraper {
    static final String TAG = AppStoreApp.LOGPREFIX + "LiveScraper";
    final GPlayScraper gPlayScraper = new GPlayScraper();

    @NonNull
    @Override
    public Observable<ScrapeResult> scrape(@NonNull AppInfo appToScrape) {
        Timber.tag(TAG).d("Scraping %s", appToScrape.toString());
        for (Download download : appToScrape.getDownloads()) {
            switch (download.getType()) {
                case GPLAY:
                    return gPlayScraper.scrape(appToScrape);
                default:
                    Timber.tag(TAG).d("No scraper available for type %s", download.getType());
            }
        }
        return Observable.error(new UnsupportedScrapeTargetException(appToScrape));
    }
}
