package subreddit.android.appstore.backend.scrapers;

import java.util.Collection;

public abstract class BaseScrapeResult implements ScrapeResult {
    protected String iconUrl;
    protected Collection<String> screenshotUrls;

    protected BaseScrapeResult(String iconUrl, Collection<String> screenshotUrls) {
        this.iconUrl = iconUrl;
        this.screenshotUrls = screenshotUrls;
    }
}
