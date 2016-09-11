package subreddit.android.appstore.util.ui.glide;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import subreddit.android.appstore.R;

public class PlaceHolderRequestListener implements RequestListener<Drawable> {
    private final ImageView imageTarget;
    private final View placeHolder;

    public PlaceHolderRequestListener(ImageView imageTarget) {
        this(imageTarget, null);
    }

    public PlaceHolderRequestListener(@NonNull ImageView imageTarget, @Nullable View placeHolder) {
        this.imageTarget = imageTarget;
        this.placeHolder = placeHolder;
        this.imageTarget.setImageBitmap(null);
        this.imageTarget.setVisibility(View.INVISIBLE);
        if (this.placeHolder != null) this.placeHolder.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
        placeHolder.setVisibility(View.INVISIBLE);
        imageTarget.setImageResource(R.drawable.ic_image_broken_black);
        imageTarget.setVisibility(View.VISIBLE);
        return true;
    }

    @Override
    public boolean onResourceReady(Drawable resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
        if (placeHolder != null) placeHolder.setVisibility(View.GONE);
        this.imageTarget.setVisibility(View.VISIBLE);
        return false;
    }
}
