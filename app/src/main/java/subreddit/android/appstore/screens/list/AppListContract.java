package subreddit.android.appstore.screens.list;

import java.util.List;

import subreddit.android.appstore.backend.AppInfo;
import subreddit.android.appstore.util.mvp.BasePresenter;
import subreddit.android.appstore.util.mvp.BaseView;


public class AppListContract {
    interface View extends BaseView {

        void showAppList(List<AppInfo> appInfos);

        void showLoadingScreen();

        void updateTagCount(int[] tagCount);
    }

    interface Presenter extends BasePresenter<View> {

        void refreshData();

    }
}
