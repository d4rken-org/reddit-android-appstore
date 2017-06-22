package subreddit.android.appstore.backend.github;

import dagger.Module;
import dagger.Provides;
import subreddit.android.appstore.util.dagger.ApplicationScope;


@Module
public class SelfUpdaterModule {
    @Provides
    @ApplicationScope
    public SelfUpdater provideSelfUpdater() {
        return new FakeSelfUpdater();
    }
}
