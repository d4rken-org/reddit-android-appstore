package subreddit.android.appstore.screens.details;

import android.app.Activity;
import android.content.Intent;

import dagger.Module;
import dagger.Provides;
import subreddit.android.appstore.util.dagger.ActivityScope;
import subreddit.android.appstore.util.mvp.PresenterFactory;


@Module
public class AppDetailsModule {
    private final Activity activity;

    public AppDetailsModule(Activity activity) {this.activity = activity;}

    @Provides
    @ActivityScope
    public Intent provideIntent() {
        return activity.getIntent();
    }

    @Provides
    @ActivityScope
    public PresenterFactory<AppDetailsContract.Presenter> providePresenterFactory(final Intent activityIntent) {
        return new PresenterFactory<AppDetailsContract.Presenter>() {
            @Override
            public AppDetailsContract.Presenter create() {
                return new AppDetailsPresenter(activityIntent);
            }

            @Override
            public Class<? extends AppDetailsContract.Presenter> getTypeClazz() {
                return AppDetailsPresenter.class;
            }
        };
    }
}
