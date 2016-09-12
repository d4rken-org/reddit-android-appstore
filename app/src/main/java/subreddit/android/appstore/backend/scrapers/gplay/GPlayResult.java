package subreddit.android.appstore.backend.scrapers.gplay;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collection;
import java.util.Locale;

import subreddit.android.appstore.backend.scrapers.BaseScrapeResult;
import subreddit.android.appstore.backend.scrapers.ImgSize;


public class GPlayResult extends BaseScrapeResult {

    public GPlayResult(String iconUrl, Collection<String> screenshotUrls) {
        super(iconUrl, screenshotUrls);
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
        return screenshotUrls;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "GPlayResult(iconUrl=%s, screenshotUrls=%s", iconUrl, screenshotUrls);
    }
}
