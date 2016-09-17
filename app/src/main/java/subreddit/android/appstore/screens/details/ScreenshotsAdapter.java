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

import butterknife.ButterKnife;
import subreddit.android.appstore.R;
import subreddit.android.appstore.util.ui.glide.PlaceHolderRequestListener;

public class ScreenshotsAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    List<String> urls = new ArrayList<>();
    ScreenshotClickedListener l;

    public ScreenshotsAdapter(Context context) {
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

        ImageView galleryImage = ButterKnife.findById(itemView, R.id.gallery_image);
        View galleryPlaceholder = ButterKnife.findById(itemView, R.id.gallery_placeholder);
        Glide.with(context)
                .load(urls.get(position))
                .listener(new PlaceHolderRequestListener(galleryImage, galleryPlaceholder))
                .into(galleryImage);

        galleryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (l != null) l.onScreenshotClicked(urls.get(position));
            }
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
        return super.getPageWidth(position) / 3;
    }

    public void setScreenshotClickedListener(ScreenshotClickedListener l) {
        this.l = l;
    }

    interface ScreenshotClickedListener {
        void onScreenshotClicked(String url);
    }
}
