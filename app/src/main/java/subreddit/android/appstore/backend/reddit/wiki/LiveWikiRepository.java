package subreddit.android.appstore.backend.reddit.wiki;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;
import java.util.Collection;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.ReplaySubject;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import subreddit.android.appstore.BuildConfig;
import subreddit.android.appstore.backend.UserAgentInterceptor;
import subreddit.android.appstore.backend.data.AppInfo;
import subreddit.android.appstore.backend.data.AppTags;
import subreddit.android.appstore.backend.reddit.TokenRepository;
import subreddit.android.appstore.backend.reddit.wiki.caching.WikiDiskCache;
import subreddit.android.appstore.backend.reddit.wiki.parser.BodyParser;
import subreddit.android.appstore.backend.reddit.wiki.parser.EncodingFixer;
import subreddit.android.appstore.backend.reddit.wiki.parser.SimpleBodyParser;
import timber.log.Timber;


public class LiveWikiRepository implements WikiRepository {
    static final String BASEURL = " https://oauth.reddit.com/";
    static final int NUMOFREVISIONS = 6;
    final OkHttpClient client = new OkHttpClient();
    final WikiDiskCache wikiDiskCache;
    final TokenRepository tokenRepository;
    final WikiApi wikiApi;
    ReplaySubject<Collection<AppInfo>> dataReplayer;
    String authString;

    public LiveWikiRepository(TokenRepository tokenRepository, WikiDiskCache wikiDiskCache, UserAgentInterceptor userAgent) {
        this.tokenRepository = tokenRepository;
        this.wikiDiskCache = wikiDiskCache;

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }
        builder.addInterceptor(userAgent);
        OkHttpClient client = builder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASEURL)
                .build();
        wikiApi = retrofit.create(WikiApi.class);
    }

    @Override
    public synchronized Observable<Collection<AppInfo>> getAppList() {
        if (dataReplayer == null) {
            dataReplayer = ReplaySubject.createWithSize(1);
            wikiDiskCache.getAll()
                    .switchIfEmpty(loadData().doOnNext(wikiDiskCache::putAll))
                    .subscribe(appInfos -> dataReplayer.onNext(appInfos));
        }
        return dataReplayer;
    }

    @Override
    public void refresh() {
        loadData().subscribe(appInfos -> dataReplayer.onNext(appInfos));
    }

    private Observable<Collection<AppInfo>> loadData() {
        return tokenRepository.getUserlessAuthToken()
                .subscribeOn(Schedulers.io())
                .flatMap(token -> {
                    saveAuthString(token.getAuthorizationString());
                    return wikiApi.getWikiPage(token.getAuthorizationString(), "apps");
                })
                .flatMap(response -> {
                    Timber.d(response.toString());
                    long timeStart = System.currentTimeMillis();
                    Collection<AppInfo> infos = new ArrayList<>();
                    infos.addAll(new BodyParser(new EncodingFixer()).parseBody(response.data.content_md));
                    long timeStop = System.currentTimeMillis();
                    Timber.d("Parsed %d items in %dms", infos.size(), (timeStop - timeStart));

                    // get list of revision IDs
                    return wikiApi.getWikiRevisions(authString, "apps", String.valueOf(NUMOFREVISIONS))
                            .flatMap(idsResponse -> {
                                ArrayList<String> revisionIds = new ArrayList<String>();
                                for (int x = 1; x < NUMOFREVISIONS; x++) { // start at 1 to ignore current revision
                                    String revisionId = idsResponse.data.children.get(x).id;
                                    revisionIds.add(revisionId);
                                }

                                return newAppTagger(revisionIds, infos, 0);
                            })
                            .onErrorReturn(throwable -> {
                                Timber.e(throwable, "Error while checking for new apps");
                                return infos;
                            });
                })
                .onErrorReturn(throwable -> {
                    Timber.e(throwable, "Error while fetching wiki repository");
                    return new ArrayList<>();
                });
    }

    // iterate through revision diffs to find which apps were added
    private Observable<Collection<AppInfo>> newAppTagger(ArrayList<String> revisionIds, Collection<AppInfo> infos, int i) {
        if (i < NUMOFREVISIONS-2) {
            return wikiApi.getWikiRevision(authString, "apps", revisionIds.get(i))
                    .flatMap(response -> {
                        Collection<AppInfo> thisRevisionInfos = new ArrayList<>();
                        thisRevisionInfos.addAll(new SimpleBodyParser(new EncodingFixer()).parseBody(response.data.content_md));
                        for (AppInfo r : infos) {
                            if (!thisRevisionInfos.contains(r)) {
                                r.addTag(AppTags.NEW);
                            }
                        }
                        return newAppTagger(revisionIds, infos, i + 1);
                    })
                    .onErrorReturn(throwable -> {
                        Timber.e(throwable, "Error while fetching wiki revision");
                        return infos;
                    });
        } else {
            return wikiApi.getWikiRevision(authString, "apps", revisionIds.get(i))
                    .map(response -> {
                        Collection<AppInfo> thisRevisionInfos = new ArrayList<>();
                        thisRevisionInfos.addAll(new SimpleBodyParser(new EncodingFixer()).parseBody(response.data.content_md));
                        for (AppInfo r : infos) {
                            if (!thisRevisionInfos.contains(r)) {
                                r.addTag(AppTags.NEW);
                            }
                        }
                        return infos;
                    })
                    .onErrorReturn(throwable -> {
                        Timber.e(throwable, "Error while fetching wiki revision " + revisionIds.get(i));
                        return infos;
                    });
        }
    }

    private String saveAuthString(String authString) {
        this.authString = authString;
        return authString;
    }

    interface WikiApi {
        @GET("r/Android/wiki/page")
        Observable<WikiPageResponse> getWikiPage(@Header("Authorization") String authentication,
                                                 @Query("page") String page);

        @GET("r/Android/wiki/revisions/page&limit")
        Observable<WikiRevisionsResponse> getWikiRevisions(@Header("Authorization") String authentication,
                                                           @Query("page") String page,
                                                           @Query("limit") String lim);

        @GET("r/Android/wiki/page&v")
        Observable<WikiPageResponse> getWikiRevision(@Header("Authorization") String authentication,
                                                         @Query("page") String page,
                                                         @Query("v") String id);

        // Idea: could use this instead? not sure what it does though
        @GET("r/Android/wiki/page&v&v2")
        Observable<WikiPageResponse> getWikiRevisionDiff(@Header("Authorization") String authentication,
                                                            @Query("page") String page,
                                                            @Query("v") String id1,
                                                            @Query("v2") String id2);
    }

    static class WikiPageResponse {
        String kind;
        Data data;

        static class Data {
            long revision_date;
            String content_md;
        }
    }

    static class WikiRevisionsResponse {
        String kind;
        Data data;

        static class Data {
            String modhash;
            ArrayList<Children> children;
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
