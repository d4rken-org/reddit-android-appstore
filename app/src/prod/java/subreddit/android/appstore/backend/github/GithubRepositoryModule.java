package subreddit.android.appstore.backend.github;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import subreddit.android.appstore.backend.HttpModule;
import subreddit.android.appstore.util.dagger.ApplicationScope;


@Module(includes = HttpModule.class)
public class GithubRepositoryModule {

    @Provides
    @ApplicationScope
    public GithubRepository provideGithubRepository(Github.Api githubApi) {
        return new LiveGithubRepository(githubApi);
    }

    @Provides
    @ApplicationScope
    public Github.Api provideGithubApi(OkHttpClient client) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(LiveGithubRepository.BASEURL)
                .build();
        return retrofit.create(Github.Api.class);
    }

}
