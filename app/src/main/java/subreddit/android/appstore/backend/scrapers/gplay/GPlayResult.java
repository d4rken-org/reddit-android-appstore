package subreddit.android.appstore.backend.scrapers.gplay;

import android.support.annotation.Nullable;

import java.util.Collection;

import subreddit.android.appstore.backend.ScrapeResult;


public class GPlayResult implements ScrapeResult {
    private Collection<String> urls;
    private String icon;

    GPlayResult(Collection<String> urls, String icon) {
        this.urls=urls;
        this.icon=icon;
    }

    @Nullable
    @Override
    public String getIconUrl() {
        return icon;
    }

    @Override
    public Collection<String> getScreenshotUrls() {
        return urls;
    }
}
