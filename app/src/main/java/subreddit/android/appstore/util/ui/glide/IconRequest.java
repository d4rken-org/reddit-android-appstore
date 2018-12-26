package subreddit.android.appstore.util.ui.glide;


import java.util.Locale;

import subreddit.android.appstore.backend.data.AppInfo;

public class IconRequest {
    private final AppInfo appInfo;

    public IconRequest(AppInfo appInfo) {
        this.appInfo = appInfo;
    }

    public AppInfo getAppInfo() {
        return appInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IconRequest that = (IconRequest) o;

        return appInfo.equals(that.appInfo);

    }

    @Override
    public int hashCode() {
        return appInfo.hashCode();
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "IconRequest(appInfo=%s)", appInfo);
    }
}
