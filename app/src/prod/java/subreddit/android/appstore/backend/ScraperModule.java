package subreddit.android.appstore.backend;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import subreddit.android.appstore.backend.scrapers.LiveMediaScraper;
import subreddit.android.appstore.backend.scrapers.MediaScraper;
import subreddit.android.appstore.backend.scrapers.caching.ScrapeDiskCache;
import subreddit.android.appstore.util.dagger.ApplicationScope;


@Module
public class ScraperModule {

    @Provides
    @ApplicationScope
    ScrapeDiskCache provideScrapeDiskCache(Context context) {
        return new ScrapeDiskCache(context);
    }

    @Provides
    @ApplicationScope
    MediaScraper provideScraper(ScrapeDiskCache scrapeDiskCache) {
        return new LiveMediaScraper(scrapeDiskCache);
    }
}
