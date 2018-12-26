package subreddit.android.appstore.util.ui.glide;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.util.Preconditions;

import java.io.InputStream;
import java.security.MessageDigest;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import subreddit.android.appstore.backend.scrapers.ImgSize;
import subreddit.android.appstore.backend.scrapers.MediaScraper;


public class IconRequestModelLoader implements ModelLoader<IconRequest, InputStream> {
    private final MediaScraper mediaScraper;

    IconRequestModelLoader(MediaScraper mediaScraper) {
        this.mediaScraper = mediaScraper;
    }

    @Nullable
    @Override
    public LoadData<InputStream> buildLoadData(IconRequest iconRequest, int width, int height, Options options) {
        IconRequestKey requestKey = new IconRequestKey(iconRequest, width, height, options);
        return new LoadData<>(requestKey, new ScrapeResultFetcher(mediaScraper, iconRequest, width, height, options));
    }

    static class IconRequestKey implements Key {
        private final IconRequest iconRequest;
        private final int width;
        private final int height;
        private final Object object;

        public IconRequestKey(@NonNull IconRequest iconRequest, int width, int height, @Nullable Object object) {
            this.iconRequest = Preconditions.checkNotNull(iconRequest);
            this.width = width;
            this.height = height;
            this.object = object;
        }

        @Override
        public String toString() {
            return "ObjectKey{" + "iconRequest=" + iconRequest + ", width=" + width + ", height=" + height + '}';
        }

        @Override
        public void updateDiskCacheKey(MessageDigest messageDigest) {
            messageDigest.update(toString().getBytes(CHARSET));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            IconRequestKey that = (IconRequestKey) o;

            if (width != that.width) return false;
            if (height != that.height) return false;
            if (!iconRequest.equals(that.iconRequest)) return false;
            return object != null ? object.equals(that.object) : that.object == null;

        }

        @Override
        public int hashCode() {
            int result = iconRequest.hashCode();
            result = 31 * result + width;
            result = 31 * result + height;
            result = 31 * result + (object != null ? object.hashCode() : 0);
            return result;
        }
    }

    @Override
    public boolean handles(IconRequest file) {
        return true;
    }

    private static class ScrapeResultFetcher implements DataFetcher<InputStream> {
        private final MediaScraper mediaScraper;
        final IconRequest iconRequest;
        final int width;
        final int height;
        private final Options options;
        Disposable scraperSubscription;

        ScrapeResultFetcher(MediaScraper mediaScraper, IconRequest iconRequest, int width, int height, Options options) {
            this.mediaScraper = mediaScraper;
            this.iconRequest = iconRequest;
            this.width = width;
            this.height = height;
            this.options = options;
        }

        @Override
        public void loadData(Priority priority, final DataCallback<? super InputStream> callback) {
            mediaScraper.get(iconRequest.getAppInfo())
                    .map(scrapeResult -> {
                        String iconUrl = scrapeResult.getIconUrl(ImgSize.px(width), ImgSize.px(height));
                        if (iconUrl == null) {
                            throw new IllegalArgumentException("Icon url was null for" + iconRequest.getAppInfo());
                        }
                        //TODO: okhttp
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder().url(iconUrl).build();
                        return client.newCall(request).execute().body().byteStream();
                    })
                    .subscribe(new Observer<InputStream>() {
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
                            callback.onDataReady(null);
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
        private final MediaScraper mediaScraper;

        public Factory(MediaScraper mediaScraper) {
            this.mediaScraper = mediaScraper;
        }

        @Override
        public ModelLoader<IconRequest, InputStream> build(MultiModelLoaderFactory multiFactory) {
            return new IconRequestModelLoader(mediaScraper);
        }

        @Override
        public void teardown() {
        }
    }
}