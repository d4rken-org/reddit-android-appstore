package subreddit.android.appstore.backend.reddit.wiki;

import java.util.ArrayList;
import java.util.Collection;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.ReplaySubject;
import subreddit.android.appstore.backend.data.AppInfo;
import subreddit.android.appstore.backend.data.AppTags;
import subreddit.android.appstore.backend.reddit.TokenRepository;
import subreddit.android.appstore.backend.reddit.wiki.caching.WikiDiskCache;
import subreddit.android.appstore.backend.reddit.wiki.parser.BodyParser;
import timber.log.Timber;


public class LiveWikiRepository implements WikiRepository {
    public static final String BASEURL = "https://oauth.reddit.com/";
    static final int NUMOFREVISIONS = 6;
    final WikiDiskCache wikiDiskCache;
    final TokenRepository tokenRepository;
    final Wiki.Api wikiApi;
    final BodyParser bodyParser;
    ReplaySubject<Collection<AppInfo>> dataReplayer;
    String authString;

    public LiveWikiRepository(TokenRepository tokenRepository,
                              WikiDiskCache wikiDiskCache,
                              BodyParser bodyParser,
                              Wiki.Api wikiApi) {
        this.tokenRepository = tokenRepository;
        this.wikiDiskCache = wikiDiskCache;
        this.bodyParser = bodyParser;
        this.wikiApi = wikiApi;
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
                    infos.addAll(bodyParser.parseBody(response.data.content_md));
                    long timeStop = System.currentTimeMillis();
                    Timber.d("Initial parse: Parsed %d items in %dms", infos.size(), (timeStop - timeStart));

                    // get list of revision IDs
                    return wikiApi.getWikiRevisions(authString, "apps", String.valueOf(NUMOFREVISIONS))
                            .flatMap(idsResponse -> {
                                String revisionId = idsResponse.data.children.get(NUMOFREVISIONS - 1).id;
                                return wikiApi.getWikiRevision(authString, "apps", revisionId)
                                        .map(revisionResponse -> {
                                            long newAppsTaggerTimeStart = System.currentTimeMillis();
                                            for (AppInfo r : infos) {
                                                String name = r.getAppName();
                                                if (name.contains("&")) {
                                                    int pos = name.indexOf("&") + 1;
                                                    name = name.substring(0, pos) + "amp;" + name.substring(pos, name.length());
                                                }
                                                if (!(revisionResponse.data.content_md).contains(name)) {
                                                    r.addTag(AppTags.NEW);
                                                }
                                            }
                                            long newAppsTaggerTimeStop = System.currentTimeMillis();
                                            Timber.d("Tagged all NEW items in %dms", (newAppsTaggerTimeStop - newAppsTaggerTimeStart));
                                            return infos;
                                        })
                                        .onErrorReturn(throwable -> {
                                            Timber.e(throwable, "Error while fetching wiki revision: " + revisionId);
                                            return infos;
                                        });
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

    private String saveAuthString(String authString) {
        this.authString = authString;
        return authString;
    }


}
