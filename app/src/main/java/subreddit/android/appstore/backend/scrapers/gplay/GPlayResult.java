package subreddit.android.appstore.backend.scrapers.gplay;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collection;

import subreddit.android.appstore.backend.ImgSize;
import subreddit.android.appstore.backend.ScrapeResult;


public class GPlayResult implements ScrapeResult {
    private Collection<String> urls;
    private String iconUrl;

    GPlayResult(Collection<String> urls, String iconUrl) {
        this.urls = urls;
        this.iconUrl = iconUrl;
    }


    @Nullable
    @Override
    public String getIconUrl(@NonNull ImgSize width, @NonNull ImgSize height) {
        if (width.getSizePixel() == ImgSize.DONT_CARE) {
            return iconUrl;
        } else {
            return iconUrl + "=w" + width.getSizePixel();
        }
    }

    @Override
    public Collection<String> getScreenshotUrls() {
        return urls;
    }
}
