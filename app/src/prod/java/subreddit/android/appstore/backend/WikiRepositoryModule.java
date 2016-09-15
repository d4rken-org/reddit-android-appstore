package subreddit.android.appstore.backend;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import subreddit.android.appstore.backend.wiki.DeviceIdentifier;
import subreddit.android.appstore.backend.wiki.LiveWikiRepository;
import subreddit.android.appstore.backend.wiki.TokenSource;
import subreddit.android.appstore.backend.wiki.WikiRepository;
import subreddit.android.appstore.backend.wiki.caching.WikiDiskCache;
import subreddit.android.appstore.util.dagger.ApplicationScope;


@Module
public class WikiRepositoryModule {
    @Provides
    @ApplicationScope
    DeviceIdentifier provideDeviceIdentifier(Context context) {
        return new DeviceIdentifier(context);
    }

    @Provides
    @ApplicationScope
    TokenSource provideTokenSource(Context context, DeviceIdentifier deviceIdentifier) {
        return new TokenSource(context, deviceIdentifier);
    }

    @Provides
    @ApplicationScope
    WikiDiskCache provideWikiDiskCache(Context context) {
        return new WikiDiskCache(context);
    }

    @Provides
    @ApplicationScope
    WikiRepository provideBackendService(TokenSource tokenSource, WikiDiskCache wikiDiskCache) {
        return new LiveWikiRepository(tokenSource, wikiDiskCache);
    }
}
