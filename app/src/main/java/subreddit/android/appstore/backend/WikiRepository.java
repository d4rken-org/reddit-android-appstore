package subreddit.android.appstore.backend;

import java.util.Collection;

import io.reactivex.Observable;
import subreddit.android.appstore.backend.data.AppInfo;


public interface WikiRepository {
    Observable<Collection<AppInfo>> getAppList();

    void refresh();
}
