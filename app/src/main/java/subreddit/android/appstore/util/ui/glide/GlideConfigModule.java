package subreddit.android.appstore.util.ui.glide;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Priority;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.request.RequestOptions;


public class GlideConfigModule implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDefaultRequestOptions(RequestOptions
                .diskCacheStrategyOf(DiskCacheStrategy.NONE)
                .priority(Priority.LOW));
    }

    @Override
    public void registerComponents(Context context, Registry registry) {

    }
}
