package subreddit.android.appstore.backend.scrapers.caching;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import subreddit.android.appstore.backend.data.AppInfo;
import subreddit.android.appstore.backend.scrapers.BaseScrapeResult;
import subreddit.android.appstore.backend.scrapers.ImgSize;
import subreddit.android.appstore.backend.scrapers.ScrapeResult;
import subreddit.android.appstore.util.RealmString;

public class CachedScrape extends RealmObject {
    public CachedScrape() {
    }

    public CachedScrape(AppInfo appInfo, ScrapeResult scrapeResult) {
        this.id = appInfo.hashCode();
        this.scrapeResultClassName = scrapeResult.getClass().getName();
        this.iconUrl = scrapeResult.getIconUrl(ImgSize.dontCare(), ImgSize.dontCare());
        this.screenshotUrls = RealmString.fromCollection(scrapeResult.getScreenshotUrls());
    }

    @PrimaryKey
    int id;
    String scrapeResultClassName;
    String iconUrl;
    RealmList<RealmString> screenshotUrls;

    public BaseScrapeResult toBaseScrapeResult() throws ClassNotFoundException,
            NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> c = Class.forName(scrapeResultClassName);
        Constructor<?> cons = c.getConstructor(String.class, Collection.class);
        return (BaseScrapeResult) cons.newInstance(iconUrl, RealmString.toCollection(screenshotUrls));
    }
}
