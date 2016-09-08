package subreddit.android.appstore.screens.list;

import android.os.Bundle;

import dagger.Module;
import dagger.Provides;
import subreddit.android.appstore.backend.WikiRepository;
import subreddit.android.appstore.screens.navigation.CategoryFilter;
import subreddit.android.appstore.util.dagger.FragmentScope;
import subreddit.android.appstore.util.mvp.PresenterFactory;


@Module
public class AppListModule {
    private CategoryFilter categoryFilter;

    public AppListModule(Bundle fragmentArguments) {
        categoryFilter = fragmentArguments.getParcelable(AppListFragment.ARG_KEY_CATEGORYFILTER);
    }

    @Provides
    @FragmentScope
    public CategoryFilter provideCategories() {
        return categoryFilter;
    }

    @Provides
    @FragmentScope
    public PresenterFactory<AppListContract.Presenter> providePresenterFactory(final WikiRepository wikiRepository, final CategoryFilter categoryFilter) {
        return new PresenterFactory<AppListContract.Presenter>() {
            @Override
            public AppListContract.Presenter create() {
                return new AppListPresenter(wikiRepository, categoryFilter);
            }

            @Override
            public Class<? extends AppListContract.Presenter> getTypeClazz() {
                return AppListPresenter.class;
            }
        };
    }
}
