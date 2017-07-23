package subreddit.android.appstore.util.ui.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.module.GlideModule;

import java.io.InputStream;

import subreddit.android.appstore.AppStoreApp;


public class GlideConfigModule implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
//        builder.setDefaultRequestOptions(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE));
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        registry.append(
                IconRequest.class,
                InputStream.class,
                new IconRequestModelLoader.Factory(
                        AppStoreApp.Injector.INSTANCE.getAppComponent().mediaScraper()
                )
        );
    }
}
