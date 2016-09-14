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

public class ScreenshotsAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<String> urls = new ArrayList<>();
    private ScreenshotClickedListener l;

    public ScreenshotsAdapter(Context context) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void update(List<String> urls) {
        this.urls=urls;
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

        ImageView imageView = (ImageView) itemView.findViewById(R.id.gallery_image);
        Glide.with(context).load(urls.get(position)).into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (l!=null) {
                    l.onScreenshotClicked(urls.get(position));
                }
            }
        });

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((FrameLayout) object);
    }

    public void setScreenshotClickedListener(ScreenshotClickedListener l) {
        this.l=l;
    }

    interface ScreenshotClickedListener {
        void onScreenshotClicked(String url);
    }
}
