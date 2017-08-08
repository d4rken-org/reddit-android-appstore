package subreddit.android.appstore.backend;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
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

    @Provides
    @ApplicationScope
    Retrofit provideRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("https://api.github.com/")
                .build();
    }


}
