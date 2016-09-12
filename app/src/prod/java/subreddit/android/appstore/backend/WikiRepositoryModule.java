package subreddit.android.appstore.backend;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import subreddit.android.appstore.backend.wiki.LiveWikiRepository;
import subreddit.android.appstore.backend.wiki.WikiRepository;
import subreddit.android.appstore.backend.wiki.caching.WikiDiskCache;
import subreddit.android.appstore.util.dagger.ApplicationScope;


@Module
public class WikiRepositoryModule {
    @Provides
    @ApplicationScope
    WikiDiskCache provideWikiDiskCache(Context context) {
        return new WikiDiskCache(context);
    }

    @Provides
    @ApplicationScope
    WikiRepository provideBackendService(WikiDiskCache wikiDiskCache) {
        return new LiveWikiRepository(wikiDiskCache);
    }
}
