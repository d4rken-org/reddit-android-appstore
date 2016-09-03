package subreddit.android.appstore;

import dagger.Component;
import subreddit.android.appstore.backend.WikiRepository;
import subreddit.android.appstore.backend.WikiRepositoryModule;
import subreddit.android.appstore.util.dagger.ApplicationScope;


@ApplicationScope
@Component(modules = {
        AndroidModule.class,
        WikiRepositoryModule.class
})
public interface AppComponent {
    WikiRepository wikiRepository();
}
