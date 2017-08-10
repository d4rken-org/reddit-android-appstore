package subreddit.android.appstore.backend.github;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface GithubApi {
    String BASEURL = "https://api.github.com/";

    @GET("repos/d4rken/reddit-android-appstore/releases/latest")
    Observable<GithubRepository.Release> getLatestRelease();

    @GET("repos/d4rken/reddit-android-appstore/contributors")
    Observable<List<GithubRepository.Contributor>> getContributors();
}
