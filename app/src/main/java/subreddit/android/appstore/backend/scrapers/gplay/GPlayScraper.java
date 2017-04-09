package subreddit.android.appstore.backend.scrapers.gplay;


import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Function;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import subreddit.android.appstore.backend.DeadLinkException;
import subreddit.android.appstore.backend.data.AppInfo;
import subreddit.android.appstore.backend.data.Download;
import subreddit.android.appstore.backend.scrapers.ImgSize;
import subreddit.android.appstore.backend.scrapers.MediaScraper;
import subreddit.android.appstore.backend.scrapers.ScrapeResult;
import timber.log.Timber;

public class GPlayScraper implements MediaScraper {
    final OkHttpClient client = new OkHttpClient();

    @NonNull
    @Override
    public Observable<ScrapeResult> get(@NonNull final AppInfo appToScrape) {
        return Observable
                .<String>create(emitter -> {
                    for (Download d : appToScrape.getDownloads()) {
                        if (d.getType() == Download.Type.GPLAY) {
                            Timber.d("Scraping %s", d.getTarget());
                            emitter.onNext(d.getTarget());
                        }
                    }
                    emitter.onComplete();
                })
                .map(s -> client.newCall(new Request.Builder().url(s).build()).execute())
                .map(response -> {
                    if (response.code() == 404)
                        throw new DeadLinkException(response.request().url().toString());

                    Collection<String> urls = new ArrayList<>();
                    String body = response.body().string();
                    int iconStart = body.indexOf("<img class=\"cover-image\"");
                    int iconEnd = body.indexOf("\" alt=\"Cover art\"", iconStart);
                    String iconUrl = body.substring(body.indexOf("lh", iconStart), iconEnd);
                    Timber.v("%s | icon url: %s", appToScrape, iconUrl);

                    // Strip size parameter we generate these
                    iconUrl = iconUrl.replaceAll("=(w|h)\\d+", "");

                    if (!iconUrl.startsWith("http://")) iconUrl = "http://" + iconUrl;
                    while (body.contains("<img class=\"screenshot\"")) {
                        int start = body.indexOf("<img class=\"screenshot\"");
                        int end = body.indexOf("itemprop=\"screenshot\"", start);
                        String screenUrl = body.substring(start, end);
                        int subStart = screenUrl.indexOf("lh");
                        int subEnd = screenUrl.indexOf("\"", subStart);
                        screenUrl = screenUrl.substring(subStart, subEnd);
                        body = body.substring(end, body.length());
                        Timber.v("%s | screenshot url: %s", appToScrape, screenUrl);

                        // Strip size parameter we generate these
                        screenUrl = screenUrl.replaceAll("=(w|h)\\d+", "");

                        if (!screenUrl.startsWith("http://")) screenUrl = "http://" + screenUrl;
                        urls.add(screenUrl);
                    }

                    return new GPlayResult(iconUrl, urls);
                })
                .toList()
                .toObservable()
                .map(scrapeResults -> {
                    String iconUrl = null;
                    Collection<String> screenshotUrls = new ArrayList<>();
                    for (ScrapeResult scrapeResult : scrapeResults) {
                        if (scrapeResult.getIconUrl(ImgSize.dontCare(), ImgSize.dontCare()) != null && iconUrl == null) {
                            iconUrl = scrapeResult.getIconUrl(ImgSize.dontCare(), ImgSize.dontCare());
                        }
                        screenshotUrls.addAll(scrapeResult.getScreenshotUrls());
                    }
                    return new GPlayResult(iconUrl, screenshotUrls);
                });

    }
}
