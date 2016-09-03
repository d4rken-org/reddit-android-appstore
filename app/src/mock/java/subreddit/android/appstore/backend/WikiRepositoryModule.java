package subreddit.android.appstore.backend;

import dagger.Module;
import dagger.Provides;
import subreddit.android.appstore.util.dagger.ApplicationScope;


@Module
public class WikiRepositoryModule {
    @Provides
    @ApplicationScope
    WikiRepository provideWikiRepository() {
        return new FakeWikiRepository();
    }
}
