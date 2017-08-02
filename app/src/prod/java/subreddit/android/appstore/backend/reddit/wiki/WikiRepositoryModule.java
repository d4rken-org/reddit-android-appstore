package subreddit.android.appstore.backend.reddit.wiki;

import android.content.Context;

import java.util.HashSet;
import java.util.Set;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ElementsIntoSet;
import subreddit.android.appstore.backend.DeviceIdentifier;
import subreddit.android.appstore.backend.UserAgentInterceptor;
import subreddit.android.appstore.backend.reddit.TokenRepository;
import subreddit.android.appstore.backend.reddit.wiki.caching.WikiDiskCache;
import subreddit.android.appstore.backend.reddit.wiki.parser.AppParser;
import subreddit.android.appstore.backend.reddit.wiki.parser.CategoryParser;
import subreddit.android.appstore.backend.reddit.wiki.parser.ContactColumnParser;
import subreddit.android.appstore.backend.reddit.wiki.parser.DescriptionColumnParser;
import subreddit.android.appstore.backend.reddit.wiki.parser.DeviceColumnParser;
import subreddit.android.appstore.backend.reddit.wiki.parser.EncodingFixer;
import subreddit.android.appstore.backend.reddit.wiki.parser.NameColumnParser;
import subreddit.android.appstore.backend.reddit.wiki.parser.PriceColumnParser;
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

    @Provides
    @ElementsIntoSet
    Set<AppParser> provideAppParsers(EncodingFixer encodingFixer) {
        Set<AppParser> appParsers = new HashSet<>();
        appParsers.add(new NameColumnParser(encodingFixer));
        appParsers.add(new PriceColumnParser(encodingFixer));
        appParsers.add(new DeviceColumnParser(encodingFixer));
        appParsers.add(new DescriptionColumnParser(encodingFixer));
        appParsers.add(new ContactColumnParser(encodingFixer));
        return appParsers;
    }

    @Provides
    CategoryParser provideCategoryParser(EncodingFixer encodingFixer) {
        return new CategoryParser(encodingFixer);
    }

    @Provides
    EncodingFixer provideEncodingFixer() {
        return new EncodingFixer();
    }
}
