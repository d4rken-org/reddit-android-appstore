package subreddit.android.appstore.backend.github;

import dagger.Module;
import dagger.Provides;
import subreddit.android.appstore.util.dagger.ApplicationScope;


@Module
public class GithubRepositoryModule {
    @Provides
    @ApplicationScope
    public GithubRepository provideGithubRepository() {
        return new FakeGithubRepository();
    }
}
