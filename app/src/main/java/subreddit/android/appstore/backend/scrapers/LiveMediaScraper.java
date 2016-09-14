package subreddit.android.appstore.backend.scrapers;


import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import subreddit.android.appstore.backend.UnsupportedScrapeTargetException;
import subreddit.android.appstore.backend.data.AppInfo;
import subreddit.android.appstore.backend.data.Download;
import subreddit.android.appstore.backend.scrapers.caching.ScrapeDiskCache;
import subreddit.android.appstore.backend.scrapers.gplay.GPlayScraper;
import timber.log.Timber;

public class LiveMediaScraper implements MediaScraper {
    final LruCache<AppInfo, Observable<ScrapeResult>> scrapeCache = new LruCache<>(1);
    final ScrapeDiskCache scrapeDiskCache;
    final GPlayScraper gPlayScraper = new GPlayScraper();

    public LiveMediaScraper(ScrapeDiskCache scrapeDiskCache) {
        this.scrapeDiskCache = scrapeDiskCache;
    }

    @NonNull
    @Override
    public Observable<ScrapeResult> get(@NonNull final AppInfo appToScrape) {
        synchronized (scrapeCache) {
            Observable<ScrapeResult> scrapeResultObserver = scrapeCache.get(appToScrape);
            if (scrapeResultObserver == null) {
                scrapeResultObserver = scrapeDiskCache.get(appToScrape)
                        .switchIfEmpty(
                                doScrape(appToScrape).doOnNext(new Consumer<ScrapeResult>() {
                                    @Override
                                    public void accept(ScrapeResult scrapeResult) throws Exception {
                                        scrapeDiskCache.put(appToScrape, scrapeResult);
                                    }
                                })
                        )
                        .cache();
                scrapeCache.put(appToScrape, scrapeResultObserver);
            } else Timber.d("Using cached result for %s", appToScrape);
            return scrapeResultObserver;
        }
    }

    private Observable<ScrapeResult> doScrape(@NonNull AppInfo appToScrape) {
        Timber.d("Scraping %s", appToScrape.toString());
        for (Download download : appToScrape.getDownloads()) {
            switch (download.getType()) {
                case GPLAY:
                    return gPlayScraper.get(appToScrape);
                default:
                    Timber.d("No scraper available for type %s", download.getType());
            }
        }
        return Observable.error(new UnsupportedScrapeTargetException(appToScrape));
    }
}
