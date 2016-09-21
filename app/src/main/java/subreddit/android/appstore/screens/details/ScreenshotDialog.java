package subreddit.android.appstore.screens.details;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import subreddit.android.appstore.R;

public class ScreenshotDialog extends Dialog implements View.OnClickListener {
    @BindView(R.id.dialog_image) ImageView image;
    @BindView(R.id.dialog_toolbar) Toolbar toolbar;
    private String url;
    private Context context;

    public ScreenshotDialog(Context context, String url) {
        super(context,android.R.style.Theme_Black_NoTitleBar);
        this.url=url;
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_screenshot);
        ButterKnife.bind(this);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_48px);
        toolbar.setNavigationOnClickListener(this);
        Glide.with(context).load(url).into(image);
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }
}
