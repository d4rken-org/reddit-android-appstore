package subreddit.android.appstore;

import dagger.Component;
import subreddit.android.appstore.backend.ScraperModule;
import subreddit.android.appstore.backend.WikiRepositoryModule;
import subreddit.android.appstore.backend.scrapers.Scraper;
import subreddit.android.appstore.backend.wiki.WikiRepository;
import subreddit.android.appstore.util.dagger.ApplicationScope;


@ApplicationScope
@Component(modules = {
        AndroidModule.class,
        WikiRepositoryModule.class,
        ScraperModule.class
})
public interface AppComponent {
    WikiRepository wikiRepository();

    Scraper scraperRepository();
}
