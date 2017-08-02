package subreddit.android.appstore.backend.reddit.wiki;

import java.util.HashSet;
import java.util.Set;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ElementsIntoSet;
import subreddit.android.appstore.backend.reddit.wiki.parser.AppParser;
import subreddit.android.appstore.backend.reddit.wiki.parser.CategoryParser;
import subreddit.android.appstore.backend.reddit.wiki.parser.ContactColumnParser;
import subreddit.android.appstore.backend.reddit.wiki.parser.DescriptionColumnParser;
import subreddit.android.appstore.backend.reddit.wiki.parser.DeviceColumnParser;
import subreddit.android.appstore.backend.reddit.wiki.parser.EncodingFixer;
import subreddit.android.appstore.backend.reddit.wiki.parser.NameColumnParser;
import subreddit.android.appstore.backend.reddit.wiki.parser.PriceColumnParser;
import subreddit.android.appstore.util.dagger.ApplicationScope;


@Module
public class WikiRepositoryModule {
    @Provides
    @ApplicationScope
    WikiRepository provideWikiRepository() {
        return new FakeWikiRepository();
    }

    @Provides
    @ElementsIntoSet
    Set<AppParser> provideAppParsers(EncodingFixer encodingFixer) {
        Set<AppParser> appParsers = new HashSet<>();
        appParsers.add(new NameColumnParser(encodingFixer));
        appParsers.add(new PriceColumnParser(encodingFixer));
        appParsers.add(new DeviceColumnParser(encodingFixer));
        appParsers.add(new DescriptionColumnParser(encodingFixer));
        appParsers.add(new ContactColumnParser(encodingFixer));
        return appParsers;
    }

    @Provides
    CategoryParser provideCategoryParser(EncodingFixer encodingFixer) {
        return new CategoryParser(encodingFixer);
    }

    @Provides
    EncodingFixer provideEncodingFixer() {
        return new EncodingFixer();
    }
}
