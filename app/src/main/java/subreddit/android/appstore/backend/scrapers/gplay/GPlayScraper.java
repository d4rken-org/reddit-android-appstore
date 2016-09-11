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
import subreddit.android.appstore.AppStoreApp;
import subreddit.android.appstore.backend.DeadLinkException;
import subreddit.android.appstore.backend.ScrapeResult;
import subreddit.android.appstore.backend.Scraper;
import subreddit.android.appstore.backend.data.AppInfo;
import subreddit.android.appstore.backend.data.Download;
import timber.log.Timber;

public class GPlayScraper implements Scraper {
    static final String TAG = AppStoreApp.LOGPREFIX + "GPlayScraper";
    final OkHttpClient client = new OkHttpClient();

    @NonNull
    @Override
    public Observable<ScrapeResult> scrape(@NonNull final AppInfo appToScrape) {
        return Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        for (Download d : appToScrape.getDownloads()) {
                            if (d.getType() == Download.Type.GPLAY) {
                                Timber.tag(TAG).d("Scraping %s", d.getTarget());
                                e.onNext(d.getTarget());
                            }
                        }
                        e.onComplete();
                    }
                })
                .map(new Function<String, Response>() {
                    @Override
                    public Response apply(String s) throws Exception {
                        return client.newCall(new Request.Builder().url(s).build()).execute();
                    }
                })
                .map(new Function<Response, ScrapeResult>() {
                    @Override
                    public ScrapeResult apply(Response response) throws Exception {
                        if (response.code() == 404) throw new DeadLinkException(response.request().url().toString());

                        Collection<String> urls = new ArrayList<>();
                        String body = response.body().string();
                        int iconStart = body.indexOf("<img class=\"cover-image\"");
                        int iconEnd = body.indexOf("\" alt=\"Cover art\"", iconStart);
                        String icon = body.substring(body.indexOf("lh", iconStart), iconEnd);
                        Timber.tag(TAG).v("%s | icon url: %s", appToScrape, icon);
                        if (!icon.startsWith("http://")) icon = "http://" + icon;
                        while (body.contains("<img class=\"screenshot\"")) {
                            int start = body.indexOf("<img class=\"screenshot\"");
                            int end = body.indexOf("itemprop=\"screenshot\"", start);
                            String screenshot = body.substring(start, end);
                            int subStart = screenshot.indexOf("lh");
                            int subEnd = screenshot.indexOf("\"", subStart);
                            screenshot = screenshot.substring(subStart, subEnd);
                            body = body.substring(end, body.length());
                            Timber.tag(TAG).v("%s | screenshot url: %s", appToScrape, screenshot);
                            if (!screenshot.startsWith("http://")) screenshot = "http://" + screenshot;
                            urls.add(screenshot);
                        }

                        return new GPlayResult(urls, icon);
                    }
                })
                .toList()
                .map(new Function<List<ScrapeResult>, ScrapeResult>() {
                    @Override
                    public ScrapeResult apply(List<ScrapeResult> scrapeResults) throws Exception {
                        String iconUrl = null;
                        Collection<String> screenshotUrls = new ArrayList<>();
                        for (ScrapeResult scrapeResult : scrapeResults) {
                            if (scrapeResult.getIconUrl() != null && iconUrl == null) {
                                iconUrl = scrapeResult.getIconUrl();
                            }
                            screenshotUrls.addAll(scrapeResult.getScreenshotUrls());
                        }
                        return new GPlayResult(screenshotUrls, iconUrl);
                    }
                });

    }
}
