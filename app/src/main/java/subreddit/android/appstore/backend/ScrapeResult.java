package subreddit.android.appstore.backend;


import android.support.annotation.Nullable;

import java.util.Collection;

public interface ScrapeResult {

    @Nullable
    String getIconUrl();

    Collection<String> getScreenshotUrls();
}
