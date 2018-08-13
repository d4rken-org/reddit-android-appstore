package subreddit.android.appstore.backend.scrapers.gplay;

import android.support.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import subreddit.android.appstore.backend.DeadLinkException;
import subreddit.android.appstore.backend.data.AppInfo;
import subreddit.android.appstore.backend.data.Download;
import subreddit.android.appstore.backend.scrapers.ImgSize;
import subreddit.android.appstore.backend.scrapers.MediaScraper;
import subreddit.android.appstore.backend.scrapers.ScrapeResult;
import timber.log.Timber;

public class GPlayScraper implements MediaScraper {
    // TODO: okhttp
    private final OkHttpClient client;

    public GPlayScraper(OkHttpClient client) {
        this.client = client;
    }

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
                    if (response.code() == 404) throw new DeadLinkException(response.request().url().toString());

                    Document doc = Jsoup.parse(Objects.requireNonNull(response.body()).string());

                    String iconUrl = doc.select("img[alt*=Cover Art]").attr("src");
                    // Strip size parameter we generate these
                    iconUrl = iconUrl.replaceAll("=([swh])\\d+", "");

                    Collection<String> screenUrls = new ArrayList<>();
                    for (Element screenshots : doc.select("img[alt*=Screenshot Image]")) {
                        String screen = screenshots.attr("src");
                        // Strip size parameter we generate these
                        screen = screen.replaceAll("=(-*([whs])\\d+)*", "");
                        screenUrls.add(screen);
                    }

                    return new GPlayResult(iconUrl, screenUrls);
                })
                .toList()
                .map((Function<List<GPlayResult>, ScrapeResult>) (List<GPlayResult> scrapeResults) -> {
                    String iconUrl = null;
                    Collection<String> screenshotUrls = new ArrayList<>();
                    for (ScrapeResult scrapeResult : scrapeResults) {
                        if (scrapeResult.getIconUrl(ImgSize.dontCare(), ImgSize.dontCare()) != null && iconUrl == null) {
                            iconUrl = scrapeResult.getIconUrl(ImgSize.dontCare(), ImgSize.dontCare());
                        }
                        screenshotUrls.addAll(scrapeResult.getScreenshotUrls());
                    }
                    return new GPlayResult(iconUrl, screenshotUrls);
                })
                .toObservable();
    }
}
