package subreddit.android.appstore.backend.github;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import subreddit.android.appstore.backend.HttpModule;
import subreddit.android.appstore.util.dagger.ApplicationScope;


@Module(includes = HttpModule.class)
public class GithubRepositoryModule {

    @Provides
    @ApplicationScope
    public GithubRepository provideGithubRepository(Retrofit retrofit) {
        return new LiveGithubRepository(retrofit);
    }
}
