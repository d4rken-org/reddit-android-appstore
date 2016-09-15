package subreddit.android.appstore.backend.wiki;

import java.util.ArrayList;
import java.util.Collection;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.ReplaySubject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import subreddit.android.appstore.backend.data.AppInfo;
import subreddit.android.appstore.backend.wiki.caching.WikiDiskCache;
import subreddit.android.appstore.backend.wiki.parser.BodyParser;
import subreddit.android.appstore.backend.wiki.parser.EncodingFixer;
import timber.log.Timber;


public class LiveWikiRepository implements WikiRepository {
    static final String TARGET_URL = "https://www.reddit.com/r/Android/wiki/apps";
    final OkHttpClient client = new OkHttpClient();
    final WikiDiskCache wikiDiskCache;
    final TokenSource tokenSource;
    ReplaySubject<Collection<AppInfo>> dataReplayer;

    public LiveWikiRepository(TokenSource tokenSource, WikiDiskCache wikiDiskCache) {
        this.tokenSource = tokenSource;
        this.wikiDiskCache = wikiDiskCache;
    }

    @Override
    public synchronized Observable<Collection<AppInfo>> getAppList() {
        if (dataReplayer == null) {
            dataReplayer = ReplaySubject.createWithSize(1);
            wikiDiskCache.getAll()
                    .switchIfEmpty(
                            loadData().doOnNext(new Consumer<Collection<AppInfo>>() {
                                @Override
                                public void accept(Collection<AppInfo> appInfos) throws Exception {
                                    wikiDiskCache.putAll(appInfos);
                                }
                            })
                    )
                    .subscribe(new Consumer<Collection<AppInfo>>() {
                        @Override
                        public void accept(Collection<AppInfo> appInfos) throws Exception {
                            dataReplayer.onNext(appInfos);
                        }
                    });
        }
        return dataReplayer;
    }

    @Override
    public void refresh() {
        loadData().subscribe(new Consumer<Collection<AppInfo>>() {
            @Override
            public void accept(Collection<AppInfo> appInfos) throws Exception {
                dataReplayer.onNext(appInfos);
            }
        });
    }

    private Observable<Collection<AppInfo>> loadData() {
        return tokenSource.getToken()
                .subscribeOn(Schedulers.io())
                .map(new Function<String, Response>() {
                    @Override
                    public Response apply(String token) throws Exception {
                        return client.newCall(new Request.Builder().url(TARGET_URL).build()).execute();
                    }
                })
                .map(new Function<Response, Collection<AppInfo>>() {
                    @Override
                    public Collection<AppInfo> apply(Response response) throws Exception {
                        Timber.d(response.toString());
                        long timeStart = System.currentTimeMillis();
                        Collection<AppInfo> infos = new ArrayList<>();
                        infos.addAll(new BodyParser(new EncodingFixer()).parseBody(response.body()));
                        long timeStop = System.currentTimeMillis();
                        Timber.d("Parsed %d items in %dms", infos.size(), (timeStop - timeStart));
                        return infos;
                    }
                })
                .onErrorReturn(new Function<Throwable, Collection<AppInfo>>() {
                    @Override
                    public Collection<AppInfo> apply(Throwable throwable) throws Exception {
                        Timber.e(throwable, "Error while fetching wiki repository");
                        return new ArrayList<>();
                    }
                });
    }
}
