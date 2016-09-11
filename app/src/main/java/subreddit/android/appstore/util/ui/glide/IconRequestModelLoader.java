package subreddit.android.appstore.util.ui.glide;

import android.support.annotation.Nullable;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.signature.ObjectKey;

import java.io.IOException;
import java.io.InputStream;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import subreddit.android.appstore.backend.ScrapeResult;
import subreddit.android.appstore.backend.Scraper;


public class IconRequestModelLoader implements ModelLoader<IconRequest, InputStream> {
    private final Scraper scraper;

    IconRequestModelLoader(Scraper scraper) {
        this.scraper = scraper;
    }

    @Nullable
    @Override
    public LoadData<InputStream> buildLoadData(IconRequest iconRequest, int width, int height, Options options) {
        return new LoadData<>(new ObjectKey(iconRequest.getAppInfo()), new ScrapeResultFetcher(scraper, iconRequest));
    }

    @Override
    public boolean handles(IconRequest file) {
        return true;
    }

    private static class ScrapeResultFetcher implements DataFetcher<InputStream> {
        private final Scraper scraper;
        final IconRequest iconRequest;
        Disposable scraperSubscription;

        ScrapeResultFetcher(Scraper scraper, IconRequest iconRequest) {
            this.scraper = scraper;
            this.iconRequest = iconRequest;
        }

        @Override
        public void loadData(Priority priority, final DataCallback<? super InputStream> callback) {
            scraper.scrape(iconRequest.getAppInfo())
                    .map(new Function<ScrapeResult, InputStream>() {
                        @Override
                        public InputStream apply(ScrapeResult scrapeResult) throws Exception {
                            if (scrapeResult.getIconUrl() == null) {
                                throw new IllegalArgumentException("Icon url was null for" + iconRequest.getAppInfo());
                            }
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder().url(scrapeResult.getIconUrl()).build();
                            return client.newCall(request).execute().body().byteStream();
                        }
                    }).subscribe(new Observer<InputStream>() {
                @Override
                public void onSubscribe(Disposable d) {
                    scraperSubscription = d;
                }

                @Override
                public void onNext(InputStream value) {
                    callback.onDataReady(value);
                }

                @Override
                public void onError(Throwable e) {
                    callback.onLoadFailed(new IOException(e));
                }

                @Override
                public void onComplete() {

                }
            });
        }

        @Override
        public void cleanup() {
            // Do nothing.
        }

        @Override
        public void cancel() {
            if (scraperSubscription != null) scraperSubscription.dispose();
        }

        @Override
        public Class<InputStream> getDataClass() {
            return InputStream.class;
        }

        @Override
        public DataSource getDataSource() {
            return DataSource.REMOTE;
        }
    }

    public static class Factory implements ModelLoaderFactory<IconRequest, InputStream> {
        private final Scraper scraper;

        public Factory(Scraper scraper) {
            this.scraper = scraper;
        }

        @Override
        public ModelLoader<IconRequest, InputStream> build(MultiModelLoaderFactory multiFactory) {
            return new IconRequestModelLoader(scraper);
        }

        @Override
        public void teardown() {}
    }
}