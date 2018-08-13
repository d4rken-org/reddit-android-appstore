package subreddit.android.appstore.backend.reddit.wiki;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

interface WikiApi {
    String BASEURL = "https://oauth.reddit.com/";

    @GET("r/Android/wiki/page")
    Observable<Response.Page> getWikiPage(@Header("Authorization") String authentication,
                                          @Query("page") String page);

    @GET("r/Android/wiki/revisions/page&limit")
    Observable<Response.Revisions> getWikiRevisions(@Header("Authorization") String authentication,
                                                    @Query("page") String page,
                                                    @Query("limit") String lim);

    @GET("r/Android/wiki/page&v")
    Observable<Response.Page> getWikiRevision(@Header("Authorization") String authentication,
                                              @Query("page") String page,
                                              @Query("v") String id);

    @GET("r/Android/wiki/page&v&v2")
    Observable<Response.Page> getWikiRevisionDiff(@Header("Authorization") String authentication,
                                                  @Query("page") String page,
                                                  @Query("v") String id1,
                                                  @Query("v2") String id2);

    class Response {
        static class Page {
            String kind;
            Data data;

            static class Data {
                long revision_date;
                String content_md;
            }
        }

        static class Revisions {
            String kind;
            Revisions.Data data;

            static class Data {
                String modhash;
                ArrayList<Revisions.Children> children;
            }

            static class Children {
                long timestamp;
                String reason;
                Author author;
                String page;
                String id;
            }

            static class Author {

            }
        }
    }
}