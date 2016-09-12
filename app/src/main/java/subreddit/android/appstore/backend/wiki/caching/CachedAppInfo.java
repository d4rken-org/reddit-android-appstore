package subreddit.android.appstore.backend.wiki.caching;

import com.google.gson.Gson;

import io.realm.RealmObject;
import subreddit.android.appstore.backend.data.AppInfo;

public class CachedAppInfo extends RealmObject {
    public CachedAppInfo() {
    }

    public CachedAppInfo(Gson gson, AppInfo appInfo) {
        json = appInfo.toJson(gson);
    }

    String json;

    public AppInfo toAppInfo(Gson gson) {
        return AppInfo.fromJson(gson, json);
    }
}
