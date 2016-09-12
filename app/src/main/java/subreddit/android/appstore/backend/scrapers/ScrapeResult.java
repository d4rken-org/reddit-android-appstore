package subreddit.android.appstore.backend.scrapers;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collection;

public interface ScrapeResult {

    /**
     * @param width  if you don't care, put your hands... err put -1
     * @param height same as width
     * @return fitting url pointing to an image
     */
    @Nullable
    String getIconUrl(@NonNull ImgSize width, @NonNull ImgSize height);

    Collection<String> getScreenshotUrls();
}
