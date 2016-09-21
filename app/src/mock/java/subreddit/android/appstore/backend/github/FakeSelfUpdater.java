package subreddit.android.appstore.backend.github;


import io.reactivex.Observable;

public class FakeSelfUpdater implements SelfUpdater {
    @Override
    public Observable<Release> getLatestRelease() {
        return Observable.just(new Release());
    }
}
