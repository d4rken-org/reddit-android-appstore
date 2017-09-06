package subreddit.android.appstore.util.ui.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideExtension;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.annotation.GlideOption;
import com.bumptech.glide.annotation.GlideType;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

import subreddit.android.appstore.AppStoreApp;

/**
 * {@link GlideModule} along with the annotation processor from Glide causes a
 * {@link GlideApp} to be generated inside this package. The generated class
 * merges APIs from {@link GlideExtension} marked classes.
 * Such marked classes can contribute {@link GlideOption} and {@link GlideType}
 * to the generated API.
 */
@GlideModule
public class GlideConfigModule extends AppGlideModule {

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

    /**
     * @return {@code false}
     * @see com.bumptech.glide.module.LibraryGlideModule
     */
    @Override
    public boolean isManifestParsingEnabled() {
        // Should return false as long as no modules use the AndroidManifest.
        // Only modules for pre Glide 4 will use the manifest
        return false;
    }
}
