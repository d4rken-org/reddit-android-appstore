package subreddit.android.appstore.screens.details;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import subreddit.android.appstore.R;

public class ScreenshotDialog extends Dialog implements View.OnClickListener {
    private final List<String> urls;
    private final int currentImage;
    @BindView(R.id.screenshot_dialog_pager)
    ViewPager viewPager;
    @BindView(R.id.screenshot_dialog_toolbar)
    Toolbar toolbar;

    public ScreenshotDialog(Context context, List<String> urls, int currentImage) {
        super(context, R.style.AppTheme_Black);
        this.currentImage = currentImage;
        this.urls = urls;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_screenshot);
        ButterKnife.bind(this);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_48px);
        toolbar.setNavigationOnClickListener(this);
        toolbar.setTitle(R.string.screenshots);

        ScreenshotsAdapter screenshotsAdapter = new ScreenshotsAdapter(getContext(), 1);
        screenshotsAdapter.update(urls);
        viewPager.setAdapter(screenshotsAdapter);
        viewPager.setCurrentItem(currentImage, false);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                updatePageIndicator(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        updatePageIndicator(currentImage);
    }

    private void updatePageIndicator(int position) {
        toolbar.setSubtitle(String.format("%s/%s", position + 1, urls.size()));
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }
}
