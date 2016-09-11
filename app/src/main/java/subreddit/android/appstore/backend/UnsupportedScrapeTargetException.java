package subreddit.android.appstore.backend;

import java.util.Locale;

import subreddit.android.appstore.backend.data.AppInfo;

public class UnsupportedScrapeTargetException extends IllegalArgumentException {
    public UnsupportedScrapeTargetException(AppInfo appInfo) {
        super(String.format(Locale.US, "%s has no supported scrape target: %s", appInfo, appInfo.getDownloads()));
    }
}
