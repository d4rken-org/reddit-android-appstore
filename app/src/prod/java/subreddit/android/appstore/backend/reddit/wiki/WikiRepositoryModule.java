package subreddit.android.appstore.backend.reddit.wiki;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import subreddit.android.appstore.backend.DeviceIdentifier;
import subreddit.android.appstore.backend.UserAgentInterceptor;
import subreddit.android.appstore.backend.reddit.TokenRepository;
import subreddit.android.appstore.backend.reddit.wiki.caching.WikiDiskCache;
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
    UserAgentInterceptor provideUserAgentInterceptor(Context context) {
        return new UserAgentInterceptor(context);
    }

    @Provides
    @ApplicationScope
    TokenRepository provideTokenSource(Context context, UserAgentInterceptor userAgentInterceptor, DeviceIdentifier deviceIdentifier) {
        return new TokenRepository(context, deviceIdentifier, userAgentInterceptor);
    }

    @Provides
    @ApplicationScope
    WikiDiskCache provideWikiDiskCache(Context context) {
        return new WikiDiskCache(context);
    }

    @Provides
    @ApplicationScope
    WikiRepository provideBackendService(TokenRepository tokenRepository, UserAgentInterceptor userAgentInterceptor, WikiDiskCache wikiDiskCache) {
        return new LiveWikiRepository(tokenRepository, wikiDiskCache, userAgentInterceptor);
    }
}
