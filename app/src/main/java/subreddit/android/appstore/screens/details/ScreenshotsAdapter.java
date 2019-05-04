package subreddit.android.appstore.screens.details;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import subreddit.android.appstore.R;
import subreddit.android.appstore.util.ui.glide.PlaceHolderRequestListener;

public class ScreenshotsAdapter extends PagerAdapter {
    private final int imagesPerPage;
    private Context context;
    private LayoutInflater layoutInflater;
    List<String> urls = new ArrayList<>();
    ScreenshotClickedListener l;
    private View currentView;

    public ScreenshotsAdapter(Context context, int imagesPerPage) {
        this.imagesPerPage = imagesPerPage;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void update(List<String> urls) {
        this.urls = urls;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.adapter_gallery_image, container, false);

        ImageView galleryImage = itemView.findViewById(R.id.gallery_image);
        View galleryPlaceholder = itemView.findViewById(R.id.gallery_placeholder);
        Glide.with(context)
                .load(urls.get(position))
                .listener(new PlaceHolderRequestListener(galleryImage, galleryPlaceholder))
                .into(galleryImage);

        galleryImage.setOnClickListener(view -> {
            if (l != null) l.onScreenshotClicked(urls.get(position));
        });

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((FrameLayout) object);
    }

    @Override
    public float getPageWidth(int position) {
        return super.getPageWidth(position) / imagesPerPage;
    }

    public void setScreenshotClickedListener(ScreenshotClickedListener l) {
        this.l = l;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        currentView = (View) object;
        super.setPrimaryItem(container, position, object);
    }

    public View getCurrentView() {
        return currentView;
    }

    interface ScreenshotClickedListener {
        void onScreenshotClicked(String url);
    }
}
