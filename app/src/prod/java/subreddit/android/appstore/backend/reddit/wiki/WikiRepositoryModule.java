package subreddit.android.appstore.backend.reddit.wiki;

import android.content.Context;

import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.HashSet;
import java.util.Set;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import subreddit.android.appstore.backend.DeviceIdentifier;
import subreddit.android.appstore.backend.HttpModule;
import subreddit.android.appstore.backend.UserAgentInterceptor;
import subreddit.android.appstore.backend.reddit.TokenApi;
import subreddit.android.appstore.backend.reddit.TokenRepository;
import subreddit.android.appstore.backend.reddit.wiki.caching.WikiDiskCache;
import subreddit.android.appstore.backend.reddit.wiki.parser.AppParser;
import subreddit.android.appstore.backend.reddit.wiki.parser.BodyParser;
import subreddit.android.appstore.backend.reddit.wiki.parser.CategoryParser;
import subreddit.android.appstore.backend.reddit.wiki.parser.ContactColumnParser;
import subreddit.android.appstore.backend.reddit.wiki.parser.DescriptionColumnParser;
import subreddit.android.appstore.backend.reddit.wiki.parser.DeviceColumnParser;
import subreddit.android.appstore.backend.reddit.wiki.parser.EncodingFixer;
import subreddit.android.appstore.backend.reddit.wiki.parser.NameColumnParser;
import subreddit.android.appstore.backend.reddit.wiki.parser.PriceColumnParser;
import subreddit.android.appstore.util.dagger.ApplicationScope;


@Module(includes = HttpModule.class)
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
    TokenRepository provideTokenSource(Context context,
                                       DeviceIdentifier deviceIdentifier,
                                       TokenApi tokenApi,
                                       Gson gson) {
        return new TokenRepository(context, deviceIdentifier, tokenApi, gson);
    }

    @Provides
    @ApplicationScope
    WikiDiskCache provideWikiDiskCache(Context context) {
        return new WikiDiskCache(context);
    }

    @Provides
    @ApplicationScope
    WikiRepository provideBackendService(TokenRepository tokenRepository,
                                         WikiDiskCache wikiDiskCache,
                                         BodyParser bodyParser,
                                         WikiApi wikiApi) {
        return new LiveWikiRepository(tokenRepository, wikiDiskCache, bodyParser, wikiApi);
    }

    @Provides
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

    @Provides
    @ApplicationScope
    WikiApi provideWikiApi(OkHttpClient client) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(WikiApi.BASEURL)
                .build();
        return retrofit.create(WikiApi.class);
    }

    @Provides
    @ApplicationScope
    TokenApi provideTokenApi(OkHttpClient client) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(TokenApi.BASEURL)
                .build();
        return retrofit.create(TokenApi.class);
    }

}
