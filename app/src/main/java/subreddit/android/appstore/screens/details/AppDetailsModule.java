package subreddit.android.appstore.screens.details;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dagger.Module;
import dagger.Provides;
import subreddit.android.appstore.backend.data.AppInfo;
import subreddit.android.appstore.backend.scrapers.MediaScraper;
import subreddit.android.appstore.util.dagger.FragmentScope;
import subreddit.android.appstore.util.mvp.PresenterFactory;


@Module
public class AppDetailsModule {
    private final AppInfo appInfo;

    public AppDetailsModule(AppCompatActivity activity) {
        Gson gson = new GsonBuilder().create();
        appInfo = AppInfo.fromJson(gson, activity.getIntent().getStringExtra(AppDetailsActivity.ARG_KEY));
    }

    @Provides
    @FragmentScope
    public AppInfo provideAppInfo() {
        return appInfo;
    }

    @Provides
    @FragmentScope
    public PresenterFactory<AppDetailsContract.Presenter> providePresenterFactory(
            final SharedPreferences preferences, final MediaScraper mediaScraper, final AppInfo appInfo
    ) {
        return new PresenterFactory<AppDetailsContract.Presenter>() {
            @Override
            public AppDetailsContract.Presenter create() {
                return new AppDetailsPresenter(preferences, mediaScraper, appInfo);
            }

            @Override
            public Class<? extends AppDetailsContract.Presenter> getTypeClazz() {
                return AppDetailsPresenter.class;
            }
        };
    }
}
