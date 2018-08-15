package subreddit.android.appstore.backend.scrapers;

import dagger.Module;
import dagger.Provides;
import subreddit.android.appstore.util.dagger.ApplicationScope;

@Module
public class ScraperModule {
    @Provides
    @ApplicationScope
    MediaScraper provideMediaScraper() {
        return new FakeScraper();
    }
}
