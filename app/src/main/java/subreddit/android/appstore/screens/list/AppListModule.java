package subreddit.android.appstore.screens.list;

import dagger.Module;
import dagger.Provides;
import subreddit.android.appstore.backend.WikiRepository;
import subreddit.android.appstore.util.dagger.ActivityScope;
import subreddit.android.appstore.util.mvp.PresenterFactory;


@Module
public class AppListModule {
    @Provides
    @ActivityScope
    public PresenterFactory<AppListContract.Presenter> providePresenterFactory(final WikiRepository wikiRepository) {
        return new PresenterFactory<AppListContract.Presenter>() {
            @Override
            public AppListContract.Presenter create() {
                return new AppListPresenter(wikiRepository);
            }

            @Override
            public Class<? extends AppListContract.Presenter> getTypeClazz() {
                return AppListPresenter.class;
            }
        };
    }
}
