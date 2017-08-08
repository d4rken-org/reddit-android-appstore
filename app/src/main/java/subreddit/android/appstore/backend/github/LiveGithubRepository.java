package subreddit.android.appstore.backend.github;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.http.GET;

public class LiveGithubRepository implements GithubRepository {
    private static final String BASEURL = "https://api.github.com/";
    private final GithubApi githubApi;
    private Observable<Release> latestReleaseCache;
    private Observable<List<Contributor>> latestContributorsCache;

    public LiveGithubRepository(Retrofit retrofit) {
        githubApi = retrofit.create(GithubApi.class);
    }

    interface GithubApi {
        @GET("repos/d4rken/reddit-android-appstore/releases/latest")
        Observable<Release> getLatestRelease();

        @GET("repos/d4rken/reddit-android-appstore/contributors")
        Observable<List<Contributor>> getContributors();
    }

    @Override
    public Observable<Release> getLatestRelease() {
        if (latestReleaseCache == null) latestReleaseCache = githubApi.getLatestRelease().cache();
        return latestReleaseCache;
    }

    @Override
    public Observable<List<Contributor>> getContributors() {
        if (latestContributorsCache == null) latestContributorsCache = githubApi.getContributors().cache();
        return latestContributorsCache;
    }
}
