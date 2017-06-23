package subreddit.android.appstore.backend.github;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class FakeSelfUpdater implements SelfUpdater {
    @Override
    public Observable<Release> getLatestRelease() {
        Release release = new Release();
        release.releaseName = "v100.100.100";
        release.tagName = "v100.100.100";
        return Observable.just(release);
    }

    public Observable<List<Contributor>> getContributors() {
        List<Contributor> contributors = new ArrayList<>();
        Contributor c1 = new Contributor();
        c1.username = "d4rken";
        Contributor c2 = new Contributor();
        c2.username = "bobheadxi";
        contributors.add(c1);
        contributors.add(c2);
        return Observable.just(contributors);
    }
}
