package subreddit.android.appstore.backend;

import java.util.Collection;

import rx.Observable;


public interface WikiRepository {
    Observable<Collection<AppInfo>> getAppList();
}
