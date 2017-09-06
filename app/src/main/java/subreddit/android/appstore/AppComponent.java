package subreddit.android.appstore;

import android.content.SharedPreferences;

import dagger.Component;
import subreddit.android.appstore.backend.github.GithubRepository;
import subreddit.android.appstore.backend.github.GithubRepositoryModule;
import subreddit.android.appstore.backend.reddit.wiki.WikiRepository;
import subreddit.android.appstore.backend.reddit.wiki.WikiRepositoryModule;
import subreddit.android.appstore.backend.reddit.wiki.parser.BodyParser;
import subreddit.android.appstore.backend.scrapers.MediaScraper;
import subreddit.android.appstore.backend.scrapers.ScraperModule;
import subreddit.android.appstore.util.dagger.ApplicationScope;


@ApplicationScope
@Component(modules = {
        AndroidModule.class,
        WikiRepositoryModule.class,
        ScraperModule.class,
        GithubRepositoryModule.class
})
public interface AppComponent {
    SharedPreferences providePreferences();

    WikiRepository wikiRepository();

    MediaScraper mediaScraper();

    GithubRepository selfUpdater();

    void inject(BodyParser bodyParser);
}
