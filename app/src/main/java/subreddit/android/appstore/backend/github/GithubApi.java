package subreddit.android.appstore.backend.github;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface GithubApi {
    String BASEURL = "https://api.github.com/";

    @GET("repos/d4rken/reddit-android-appstore/releases/latest")
    Observable<Release> getLatestRelease();

    @GET("repos/d4rken/reddit-android-appstore/contributors")
    Observable<List<Contributor>> getContributors();

    class Release {
        @SerializedName("url") public String releaseUrl;
        @SerializedName("tag_name") public String tagName;
        @SerializedName("name") public String releaseName;
        @SerializedName("body") public String releaseDescription;
        public boolean prerelease;
        @SerializedName("published_at") public Date publishDate;
        public List<Assets> assets;

        public static class Assets {
            @SerializedName("browser_download_url") public String downloadUrl;
            public long size;
        }
    }

    class Contributor {
        @SerializedName("login") public String username;
    }
}
