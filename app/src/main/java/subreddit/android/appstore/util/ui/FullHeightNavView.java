package subreddit.android.appstore.util.ui;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.util.AttributeSet;

public class FullHeightNavView extends NavigationView {
    public FullHeightNavView(Context context) {
        super(context);
    }

    public FullHeightNavView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FullHeightNavView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, 0);
    }
}