package subreddit.android.appstore.backend.scrapers;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
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
    MediaScraper provideScraper(ScrapeDiskCache scrapeDiskCache, OkHttpClient client) {
        return new LiveMediaScraper(scrapeDiskCache, client);
    }
}
