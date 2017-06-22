package subreddit.android.appstore.backend.github;


import org.json.JSONArray;

import io.reactivex.Observable;

public class FakeSelfUpdater implements SelfUpdater {
    @Override
    public Observable<Release> getLatestRelease() {
        Release release = new Release();
        release.releaseName = "v100.100.100";
        release.tagName = "v100.100.100";
        return Observable.just(release);
    }

    public Observable<JSONArray> getContributors() {
        JSONArray contributors = new JSONArray();
        Contributor c1 = new Contributor();
        c1.username = "d4rken";
        Contributor c2 = new Contributor();
        c2.username = "bobheadxi";
        contributors.put(c1);
        contributors.put(c2);
        return Observable.just(contributors);
    }
}
