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
import subreddit.android.appstore.backend.reddit.TokenRepository;
import subreddit.android.appstore.backend.reddit.wiki.caching.WikiDiskCache;
import subreddit.android.appstore.backend.reddit.wiki.parser.BodyParser;
import subreddit.android.appstore.backend.reddit.wiki.parser.EncodingFixer;
import timber.log.Timber;


public class LiveWikiRepository implements WikiRepository {
    static final String BASEURL = " https://oauth.reddit.com/";
    final OkHttpClient client = new OkHttpClient();
    final WikiDiskCache wikiDiskCache;
    final TokenRepository tokenRepository;
    final WikiApi wikiApi;
    ReplaySubject<Collection<AppInfo>> dataReplayer;

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
                .flatMap(token -> wikiApi.getWikiPage(token.getAuthorizationString(), "apps-test"))
                .map(response -> {
                    Timber.d(response.toString());
                    long timeStart = System.currentTimeMillis();
                    Collection<AppInfo> infos = new ArrayList<>();
                    infos.addAll(new BodyParser(new EncodingFixer()).parseBody(response.data.content_md));
                    long timeStop = System.currentTimeMillis();
                    Timber.d("Parsed %d items in %dms", infos.size(), (timeStop - timeStart));
                    return infos;
                })
                .onErrorReturn(throwable -> {
                    Timber.e(throwable, "Error while fetching wiki repository");
                    return new ArrayList<>();
                });
    }

    interface WikiApi {
        @GET("r/Android/wiki/page")
        Observable<WikiPageResponse> getWikiPage(@Header("Authorization") String authentication, @Query("page") String page);
    }

    static class WikiPageResponse {
        String kind;
        Data data;

        static class Data {
            long revision_date;
            String content_md;
        }
    }
}
