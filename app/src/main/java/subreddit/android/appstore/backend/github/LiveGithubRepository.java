package subreddit.android.appstore.backend.github;

import java.util.List;

import io.reactivex.Observable;

public class LiveGithubRepository implements GithubRepository {
    private final GithubApi githubApi;
    private Observable<Release> latestReleaseCache;
    private Observable<List<Contributor>> latestContributorsCache;

    public LiveGithubRepository(GithubApi githubApi) {
        this.githubApi = githubApi;
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
