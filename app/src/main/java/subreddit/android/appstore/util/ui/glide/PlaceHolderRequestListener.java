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

public class PlaceHolderRequestListener implements RequestListener<Drawable> {
    private final ImageView target;
    private final View placeHolder;

    public PlaceHolderRequestListener(ImageView target) {
        this(target, null);
    }

    public PlaceHolderRequestListener(@NonNull ImageView target, @Nullable View placeHolder) {
        this.target = target;
        this.placeHolder = placeHolder;
        this.target.setImageBitmap(null);
        this.target.setVisibility(View.INVISIBLE);
        if (this.placeHolder != null) this.placeHolder.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
        return false;
    }

    @Override
    public boolean onResourceReady(Drawable resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
        if (placeHolder != null) placeHolder.setVisibility(View.GONE);
        this.target.setVisibility(View.VISIBLE);
        return false;
    }
}
