package subreddit.android.appstore.screens.details;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import subreddit.android.appstore.R;

public class AppDetailsFlagDialog extends Dialog {
    private static final String REDDIT_MSG_URL_HEADER="https://www.reddit.com/message/compose/?to=/r/Android&subject=**RAS Flag Report**&message=";
    Context context;
    String appName;

    @BindView(R.id.flag_message) EditText flagMessage;

    public AppDetailsFlagDialog(Context context, String appName) {
        super(context);
        this.context=context;
        this.appName = appName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_flag);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.flag_cancel)
    public void cancel() {
        dismiss();
    }

    @OnClick(R.id.flag_go)
    public void submit() {
        dismiss();
        if (flagMessage.getText().toString().isEmpty()) {
            Toast.makeText(context, context.getResources().getString(R.string.no_message), Toast.LENGTH_LONG).show();
        } else {
            openInChrome(REDDIT_MSG_URL_HEADER + "*****" + appName +" REPORT" + "*****" + "%0A" +(flagMessage.getText().toString().trim()));
        }
    }

    private void openInChrome(String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
        builder.setSecondaryToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(((Activity) context), Uri.parse(url));
    }
}
