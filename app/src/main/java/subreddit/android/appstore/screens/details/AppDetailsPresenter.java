package subreddit.android.appstore.screens.details;

import android.content.Intent;
import android.os.Bundle;

import subreddit.android.appstore.backend.AppInfo;


public class AppDetailsPresenter implements AppDetailsContract.Presenter {
    private AppDetailsContract.View view;
    private AppInfo appInfoItem;

    public AppDetailsPresenter(Intent activityIntent) {
        appInfoItem = activityIntent.getParcelableExtra(AppDetailsActivity.ARG_KEY);
    }

    @Override
    public void onCreate(Bundle bundle) {

    }

    @Override
    public void onAttachView(AppDetailsContract.View view) {
        this.view = view;
        view.onShowDetails(appInfoItem);
    }

    @Override
    public void onDetachView() {
        view = null;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {

    }

    @Override
    public void onDestroy() {

    }
}
