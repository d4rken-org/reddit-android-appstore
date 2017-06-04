package subreddit.android.appstore.backend.github;


import io.reactivex.Observable;

public class FakeSelfUpdater implements SelfUpdater {
    @Override
    public Observable<Release> getLatestRelease() {
        Release release = new Release();
        release.releaseName = "v100.100.100";
        release.tagName = "v100.100.100";
        return Observable.just(release);
    }
}
