package subreddit.android.appstore.backend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import subreddit.android.appstore.AppStoreApp;
import timber.log.Timber;


public class LiveWikiRepository implements WikiRepository {
    static final String TAG = AppStoreApp.LOGPREFIX + "LiveWikiRepository";
    static final String TARGET_URL = "https://www.reddit.com/r/Android/wiki/apps";
    final OkHttpClient client = new OkHttpClient();

    @Override
    public Observable<Collection<AppInfo>> getAppList() {
        return Observable
                .create(new Observable.OnSubscribe<Response>() {
                    @Override
                    public void call(Subscriber<? super Response> subscriber) {
                        try {
                            Response response = client.newCall(new Request.Builder().url(TARGET_URL).build()).execute();
                            subscriber.onNext(response);
                            subscriber.onCompleted();
                            if (!response.isSuccessful()) subscriber.onError(new Exception("error"));
                        } catch (IOException e) {
                            subscriber.onError(e);
                        }
                    }
                })
                .map(new Func1<Response, Collection<AppInfo>>() {
                    @Override
                    public Collection<AppInfo> call(Response response) {
                        Timber.tag(TAG).d(response.toString());
                        long timeStart = System.currentTimeMillis();
                        Collection<AppInfo> infos = new ArrayList<>();
                        try {
                            infos.addAll(new BodyParser().parseBody(response.body()));
                        } catch (IOException e) {
                            Exceptions.propagate(e);
                        }
                        long timeStop = System.currentTimeMillis();
                        Timber.tag(TAG).d("Parsed %d items in %dms", infos.size(), (timeStop - timeStart));
                        return infos;
                    }
                });
    }

}
