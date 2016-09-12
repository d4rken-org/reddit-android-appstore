package subreddit.android.appstore.backend;


import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;

import io.reactivex.Observable;
import subreddit.android.appstore.AppStoreApp;
import subreddit.android.appstore.backend.data.AppInfo;
import subreddit.android.appstore.backend.data.Download;
import subreddit.android.appstore.backend.scrapers.gplay.GPlayScraper;
import timber.log.Timber;

public class LiveScraper implements Scraper {
    static final String TAG = AppStoreApp.LOGPREFIX + "LiveScraper";
    final LruCache<AppInfo, Observable<ScrapeResult>> scrapeCache = new LruCache<>(1000);
    final GPlayScraper gPlayScraper = new GPlayScraper();

    @NonNull
    @Override
    public Observable<ScrapeResult> get(@NonNull AppInfo appToScrape) {
        synchronized (scrapeCache) {
            Observable<ScrapeResult> scrapeResult = scrapeCache.get(appToScrape);
            if (scrapeResult == null) {
                scrapeResult = doScrape(appToScrape).cache();
                scrapeCache.put(appToScrape, scrapeResult);
            } else Timber.tag(TAG).d("Using cached result for %s", appToScrape);
            return scrapeResult;
        }
    }

    private Observable<ScrapeResult> doScrape(@NonNull AppInfo appToScrape) {
        Timber.tag(TAG).d("Scraping %s", appToScrape.toString());
        for (Download download : appToScrape.getDownloads()) {
            switch (download.getType()) {
                case GPLAY:
                    return gPlayScraper.get(appToScrape);
                default:
                    Timber.tag(TAG).d("No scraper available for type %s", download.getType());
            }
        }
        return Observable.error(new UnsupportedScrapeTargetException(appToScrape));
    }
}
