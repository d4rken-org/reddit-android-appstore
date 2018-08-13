package subreddit.android.appstore.backend.github;

import java.util.List;

import io.reactivex.Observable;

public interface GithubRepository {

    Observable<GithubApi.Release> getLatestRelease();
    Observable<List<GithubApi.Contributor>> getContributors();
}
