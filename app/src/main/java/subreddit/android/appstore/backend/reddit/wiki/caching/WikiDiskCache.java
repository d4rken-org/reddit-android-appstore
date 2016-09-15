package subreddit.android.appstore.backend.reddit.wiki.caching;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collection;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;
import subreddit.android.appstore.backend.data.AppInfo;
import timber.log.Timber;

public class WikiDiskCache {
    final Gson gson;

    public WikiDiskCache(Context context) {
        gson = new GsonBuilder().create();
    }

    public void putAll(Collection<AppInfo> appInfos) {
        Timber.d("Storing %d appinfos", appInfos.size());
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(CachedAppInfo.class);
        for (AppInfo appInfo : appInfos) {
            long start = System.currentTimeMillis();
            realm.copyToRealm(new CachedAppInfo(gson, appInfo));
            long stop = System.currentTimeMillis();
            Timber.v("Stored: %s (%d ms)", appInfo, (stop - start));
        }
        realm.commitTransaction();
    }

    public Observable<Collection<AppInfo>> getAll() {
        Timber.d("Getting all cached AppInfos");
        // TODO invalidate old data at some point?
        return Observable.create(
                new ObservableOnSubscribe<RealmResults<CachedAppInfo>>() {
                    @Override
                    public void subscribe(ObservableEmitter<RealmResults<CachedAppInfo>> e) throws Exception {
                        Realm realm = Realm.getDefaultInstance();
                        RealmResults<CachedAppInfo> realmResults = realm.where(CachedAppInfo.class).findAll();
                        if (!realmResults.isEmpty()) e.onNext(realmResults);
                        else Timber.d("No AppInfos cached");
                        e.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .map(new Function<RealmResults<CachedAppInfo>, Collection<AppInfo>>() {
                    @Override
                    public Collection<AppInfo> apply(RealmResults<CachedAppInfo> cachedAppInfos) throws Exception {
                        Collection<AppInfo> appInfos = new ArrayList<>();
                        Timber.d("Returned %d AppInfos from cache", cachedAppInfos.size());
                        for (CachedAppInfo cachedAppInfo : cachedAppInfos) appInfos.add(cachedAppInfo.toAppInfo(gson));
                        return appInfos;
                    }
                });
    }


}
