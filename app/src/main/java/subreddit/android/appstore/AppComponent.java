package subreddit.android.appstore;

import dagger.Component;
import subreddit.android.appstore.backend.ScraperModule;
import subreddit.android.appstore.backend.WikiRepositoryModule;
import subreddit.android.appstore.backend.reddit.wiki.WikiRepository;
import subreddit.android.appstore.backend.scrapers.MediaScraper;
import subreddit.android.appstore.util.dagger.ApplicationScope;


@ApplicationScope
@Component(modules = {
        AndroidModule.class,
        WikiRepositoryModule.class,
        ScraperModule.class
})
public interface AppComponent {
    WikiRepository wikiRepository();

    MediaScraper mediaScraper();
}
