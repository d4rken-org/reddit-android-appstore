package subreddit.android.appstore.screens.navigation;

import dagger.Module;
import dagger.Provides;
import subreddit.android.appstore.backend.github.GithubRepository;
import subreddit.android.appstore.backend.reddit.wiki.WikiRepository;
import subreddit.android.appstore.util.dagger.FragmentScope;
import subreddit.android.appstore.util.mvp.PresenterFactory;


@Module
public class NavigationModule {
    @Provides
    @FragmentScope
    public PresenterFactory<NavigationContract.Presenter> providePresenterFactory(final WikiRepository wikiRepository, final GithubRepository githubRepository) {
        return new PresenterFactory<NavigationContract.Presenter>() {
            @Override
            public NavigationContract.Presenter create() {
                return new NavigationPresenter(wikiRepository, githubRepository);
            }

            @Override
            public Class<? extends NavigationContract.Presenter> getTypeClazz() {
                return NavigationPresenter.class;
            }
        };
    }
}
