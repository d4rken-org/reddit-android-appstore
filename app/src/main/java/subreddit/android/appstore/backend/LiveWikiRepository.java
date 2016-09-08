package subreddit.android.appstore.backend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.ReplaySubject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import subreddit.android.appstore.AppStoreApp;
import subreddit.android.appstore.backend.data.AppInfo;
import subreddit.android.appstore.backend.parser.BodyParser;
import timber.log.Timber;


public class LiveWikiRepository implements WikiRepository {
    static final String TAG = AppStoreApp.LOGPREFIX + "LiveWikiRepository";
    static final String TARGET_URL = "https://www.reddit.com/r/Android/wiki/apps";
    final OkHttpClient client = new OkHttpClient();

    ReplaySubject<Collection<AppInfo>> dataReplayer;

    @Override
    public synchronized Observable<Collection<AppInfo>> getAppList() {
        if (dataReplayer == null) {
            dataReplayer = ReplaySubject.createWithSize(1);
            refresh();
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
        return Observable
                .create(new ObservableOnSubscribe<Response>() {
                    @Override
                    public void subscribe(ObservableEmitter<Response> emitter) throws Exception {
                        try {
                            Response response = client.newCall(new Request.Builder().url(TARGET_URL).build()).execute();
                            emitter.onNext(response);
                            emitter.onComplete();
                            if (!response.isSuccessful()) emitter.onError(new Exception("error"));
                        } catch (IOException e) {
                            emitter.onError(e);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .map(new Function<Response, Collection<AppInfo>>() {
                    @Override
                    public Collection<AppInfo> apply(Response response) throws Exception {
                        Timber.tag(TAG).d(response.toString());
                        long timeStart = System.currentTimeMillis();
                        Collection<AppInfo> infos = new ArrayList<>();
                        infos.addAll(new BodyParser().parseBody(response.body()));
                        long timeStop = System.currentTimeMillis();
                        Timber.tag(TAG).d("Parsed %d items in %dms", infos.size(), (timeStop - timeStart));
                        return infos;
                    }
                });
    }
}
