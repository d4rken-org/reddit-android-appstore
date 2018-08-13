package subreddit.android.appstore.screens.details;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import subreddit.android.appstore.R;

/**
 * Created by andrewadams on 2017-09-29.
 */

public class ScreenshotViewPager extends ViewPager {
    private ScreenshotsAdapter screenshotsAdapter;

    public ScreenshotViewPager(Context context) {
        super(context);
    }

    public ScreenshotViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (screenshotsAdapter != null) {
            View currentView = screenshotsAdapter.getCurrentView();
            if (currentView != null) {
                ImageView imageView = currentView.findViewById(R.id.gallery_image);
                Drawable drawable = imageView.getDrawable();
                if (drawable != null) {
                    Rect drawableBounds = drawable.getBounds();
                    int height = drawableBounds.height();
                    int width = drawableBounds.width();
                    if (height > width) {
                        int newHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
                        heightMeasureSpec = MeasureSpec.makeMeasureSpec(newHeight, MeasureSpec.EXACTLY);
                    } else if (height < width) {
                        int newHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 75, getResources().getDisplayMetrics());
                        heightMeasureSpec = MeasureSpec.makeMeasureSpec(newHeight, MeasureSpec.EXACTLY);
                    }
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        screenshotsAdapter = (ScreenshotsAdapter) adapter;
        super.setAdapter(adapter);
    }
}
