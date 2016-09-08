package subreddit.android.appstore.screens.navigation;

import dagger.Module;
import dagger.Provides;
import subreddit.android.appstore.backend.WikiRepository;
import subreddit.android.appstore.util.dagger.FragmentScope;
import subreddit.android.appstore.util.mvp.PresenterFactory;


@Module
public class NavigationModule {
    @Provides
    @FragmentScope
    public PresenterFactory<NavigationContract.Presenter> providePresenterFactory(final WikiRepository wikiRepository) {
        return new PresenterFactory<NavigationContract.Presenter>() {
            @Override
            public NavigationContract.Presenter create() {
                return new NavigationPresenter(wikiRepository);
            }

            @Override
            public Class<? extends NavigationContract.Presenter> getTypeClazz() {
                return NavigationPresenter.class;
            }
        };
    }
}
