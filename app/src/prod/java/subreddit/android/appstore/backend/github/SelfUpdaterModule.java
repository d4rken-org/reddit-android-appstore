package subreddit.android.appstore.backend.github;

import dagger.Module;
import dagger.Provides;
import subreddit.android.appstore.backend.UserAgentInterceptor;
import subreddit.android.appstore.util.dagger.ApplicationScope;


@Module
public class SelfUpdaterModule {

    @Provides
    @ApplicationScope
    SelfUpdater provideSelfUpdater(UserAgentInterceptor userAgentInterceptor) {
        return new LiveSelfUpdater(userAgentInterceptor);
    }
}
