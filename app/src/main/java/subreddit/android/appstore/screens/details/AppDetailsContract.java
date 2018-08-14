package subreddit.android.appstore.screens.details;

import android.support.annotation.Nullable;

import subreddit.android.appstore.backend.data.AppInfo;
import subreddit.android.appstore.backend.scrapers.ScrapeResult;
import subreddit.android.appstore.util.mvp.BasePresenter;
import subreddit.android.appstore.util.mvp.BaseView;


public interface AppDetailsContract {
    interface View extends BaseView {

        void displayDetails(@Nullable AppInfo appInfo);

        void displayScreenshots(@Nullable ScrapeResult scrapeResult);

        void displayIcon(@Nullable AppInfo appInfo);
    }

    interface Presenter extends BasePresenter<View> {
    }
}
