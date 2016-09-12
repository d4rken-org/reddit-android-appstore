package subreddit.android.appstore.backend;

import dagger.Module;
import dagger.Provides;
import subreddit.android.appstore.backend.scrapers.LiveScraper;
import subreddit.android.appstore.backend.scrapers.Scraper;
import subreddit.android.appstore.util.dagger.ApplicationScope;


@Module
public class ScraperModule {
    @Provides
    @ApplicationScope
    Scraper provideScraper() {
        return new LiveScraper();
    }
}
