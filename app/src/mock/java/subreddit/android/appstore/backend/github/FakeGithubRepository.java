package subreddit.android.appstore.backend.github;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import io.reactivex.Observable;

public class FakeGithubRepository implements GithubRepository {
    @Override
    public Observable<GithubApi.Release> getLatestRelease() {
        GithubApi.Release release = new GithubApi.Release();
        release.releaseName = "A Fabulous Release";
        release.tagName = "v100.100.100";
        release.publishDate = new GregorianCalendar(2017, Calendar.MARCH, 11).getTime();
        release.releaseDescription = "This update fixes x, x, x";
        release.assets = new ArrayList<>();
        GithubApi.Release.Assets a = new GithubApi.Release.Assets();
        a.downloadUrl = "https://github.com/d4rken/reddit-android-appstore/releases/latest";
        release.assets.add(a);

        return Observable.just(release);
    }

    public Observable<List<GithubApi.Contributor>> getContributors() {
        List<GithubApi.Contributor> contributors = new ArrayList<>();
        GithubApi.Contributor c1 = new GithubApi.Contributor();
        c1.username = "d4rken";
        GithubApi.Contributor c2 = new GithubApi.Contributor();
        c2.username = "bobheadxi";
        contributors.add(c1);
        contributors.add(c2);
        return Observable.just(contributors);
    }
}
