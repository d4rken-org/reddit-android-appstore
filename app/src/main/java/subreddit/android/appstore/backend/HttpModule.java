package subreddit.android.appstore.backend;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import subreddit.android.appstore.BuildConfig;
import subreddit.android.appstore.util.dagger.ApplicationScope;

@Module
public class HttpModule {

    @Provides
    @ApplicationScope
    Gson provideGson() {
        return new GsonBuilder().create();
    }

    @Provides
    @ApplicationScope
    OkHttpClient provideOkHttpClient(UserAgentInterceptor userAgent) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }
        builder.addInterceptor(userAgent);
        return builder.build();
    }

}
