package subreddit.android.appstore.backend.github;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import io.reactivex.Observable;

public class FakeSelfUpdater implements SelfUpdater {
    @Override
    public Observable<Release> getLatestRelease() {
        Release release = new Release();
        release.releaseName = "A Fabulous Release";
        release.tagName = "v100.100.100";
        release.publishDate = new GregorianCalendar(2017, Calendar.MARCH, 11).getTime();
        release.releaseDescription = "This update fixes x, x, x";
        release.assets = new ArrayList<>();
        Release.Assets a = new Release.Assets();
        a.downloadUrl = "https://github.com/d4rken/reddit-android-appstore/releases/download/v0.6.0/rAndroid_AppStore-v0.6.0.6000.-RELEASE-ee4ee75.apk";
        release.assets.add(a);

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
