package subreddit.android.appstore.backend.scrapers.gplay;

import android.support.annotation.Nullable;

import java.util.Collection;

import subreddit.android.appstore.backend.ScrapeResult;


public class GPlayResult implements ScrapeResult {
    private Collection<String> urls;

    GPlayResult(Collection<String> urls) {
        this.urls=urls;
    }

    @Nullable
    @Override
    public String getIconUrl() {
        return null;
    }

    @Override
    public Collection<String> getScreenshotUrls() {
        return urls;
    }
}
