package subreddit.android.appstore.screens.details;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wefika.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import subreddit.android.appstore.AppStoreApp;
import subreddit.android.appstore.R;
import subreddit.android.appstore.backend.data.AppInfo;
import subreddit.android.appstore.backend.data.AppTags;
import subreddit.android.appstore.backend.data.Contact;
import subreddit.android.appstore.backend.data.Download;
import subreddit.android.appstore.backend.scrapers.ScrapeResult;
import subreddit.android.appstore.util.mvp.BasePresenterFragment;
import subreddit.android.appstore.util.mvp.PresenterFactory;
import subreddit.android.appstore.util.ui.glide.GlideApp;
import subreddit.android.appstore.util.ui.glide.IconRequest;
import subreddit.android.appstore.util.ui.glide.PlaceHolderRequestListener;
import timber.log.Timber;


public class AppDetailsFragment extends BasePresenterFragment<AppDetailsContract.Presenter, AppDetailsContract.View>
        implements AppDetailsContract.View, ScreenshotsAdapter.ScreenshotClickedListener, View.OnClickListener, Toolbar.OnMenuItemClickListener {
    @BindView(R.id.download_fab) FloatingActionButton downloadButton;
    @BindView(R.id.details_toolbar) Toolbar toolbar;
    @BindView(R.id.collapsingToolbar) CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.appbar) AppBarLayout appBar;

    @BindView(R.id.icon_frame) View iconFrame;
    @BindView(R.id.icon_image) ImageView iconImage;
    @BindView(R.id.icon_placeholder) View iconPlaceholder;
    @BindView(R.id.title_secondary) TextView secondaryTitle;
    @BindView(R.id.tag_container) FlowLayout tagContainer;

    @BindView(R.id.screenshot_pager) ViewPager screenshotPager;
    @BindView(R.id.description) TextView description;

    private static final String REDDIT_MSG_URL_HEADER="https://www.reddit.com/message/compose/?to=/r/Android&subject=**RAS Flag Report**&message=";

    private List<String> contactItems = new ArrayList<>();
    private List<String> downloadItems = new ArrayList<>();
    private List<String> screenshotUrls = new ArrayList<>();
    private ScreenshotsAdapter screenshotsAdapter;

    ArrayList<Download> downloads = new ArrayList<>();
    ArrayList<Contact> contacts = new ArrayList<>();

    @Inject
    PresenterFactory<AppDetailsContract.Presenter> presenterFactory;
    private Unbinder unbinder;

    public static AppDetailsFragment newInstance() {
        return new AppDetailsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerAppDetailsComponent.builder()
                .appComponent(AppStoreApp.Injector.INSTANCE.getAppComponent())
                .appDetailsModule(new AppDetailsModule(getActivity()))
                .build().inject(this);
    }

    @Override
    public void onClick(View view) {
        getActivity().finish();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_contact: {
                onContactClicked();
                break;
            }
            case R.id.menu_flag: {
                EditText flagMessage = ((EditText) getActivity().getLayoutInflater().inflate(R.layout.dialog_flag, null));
                Dialog d = new AlertDialog.Builder(getContext())
                        .setTitle(R.string.flag)
                        .setMessage(R.string.flag_text)
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (flagMessage.getText().toString().isEmpty()) {
                                    Toast.makeText(getContext(), getContext().getResources().getString(R.string.no_message), Toast.LENGTH_LONG).show();
                                } else {
                                    openInChrome(REDDIT_MSG_URL_HEADER + "*****" + collapsingToolbar.getTitle() +" REPORT" + "*****" + "%0A" +(flagMessage.getText().toString().trim()));
                                }
                            }
                        })
                        .setView(flagMessage)
                        .show();
            }
        }
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_appdetails_layout, container, false);
        unbinder = ButterKnife.bind(this, layout);
        toolbar.setContentInsetStartWithNavigation(0);

        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float scrollRange = (float) appBarLayout.getTotalScrollRange();
                fadeHeaderItems(scrollRange, verticalOffset);
            }
        });

        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setNavigationOnClickListener(this);
        toolbar.inflateMenu(R.menu.appdetails_fragment);
        toolbar.setOnMenuItemClickListener(this);
        screenshotsAdapter = new ScreenshotsAdapter(getContext(), 3);
        screenshotPager.setAdapter(screenshotsAdapter);
        screenshotPager.setOffscreenPageLimit(3);
        screenshotsAdapter.setScreenshotClickedListener(this);
    }

    @NonNull
    @Override
    protected PresenterFactory<AppDetailsContract.Presenter> getPresenterFactory() {
        return presenterFactory;
    }

    @Override
    public void onDestroyView() {
        if (unbinder != null) unbinder.unbind();
        super.onDestroyView();
    }

    @OnClick(R.id.download_fab)
    void onDownloadClicked(View view) {
        if (downloads.size() < 2) {
            openDownload(downloads.get(0));
        } else {
            new AlertDialog.Builder(getContext())
                    .setItems(
                            downloadItems.toArray(new CharSequence[downloadItems.size()]),
                            (dialogInterface, i) -> openDownload(downloads.get(i)))
                    .show();
        }
    }

    void onContactClicked() {
        if (contacts.size() < 2) {
            openContact(contacts.get(0));
        } else {
            new AlertDialog.Builder(getContext())
                    .setItems(
                            contactItems.toArray(new CharSequence[contactItems.size()]),
                            (dialogInterface, i) -> openContact(contacts.get(i)))
                    .show();
        }
    }

    void openDownload(Download d) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, d.getDownloadUri()));
        } catch (Exception e) {
            Timber.e(e, "Problem launching intent for a Download link");
            displayToast(R.string.no_download_client);
        }
    }

    void openContact(Contact c) {
        switch (c.getType()) {
            case EMAIL:
                try {
                    startActivity(new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", c.getTarget(), null)));
                } catch (Exception e) {
                    Timber.e(e, "Problem launching intent for Email contact");
                    displayToast(R.string.no_email_client);
                }

                break;
            case WEBSITE:
                // TODO do reddit apps recognize a specific message intent?
                openInChrome(c.getTarget());
                break;
            case REDDIT_USERNAME:
                openInChrome(String.format(Locale.US, "http://www.reddit.com/message/compose/?to=%s", c.getTarget()));
                break;
            default:
                openInChrome(c.getTarget());
        }
    }

    @Override
    public void displayDetails(@Nullable AppInfo appInfo) {
        if (appInfo == null) {
            getActivity().finish();
            return;
        }
        collapsingToolbar.setTitle(appInfo.getAppName());
        secondaryTitle.setText(appInfo.getSecondaryCategory());
        downloads = new ArrayList<>(appInfo.getDownloads());
        contacts = new ArrayList<>(appInfo.getContacts());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            description.setText(Html.fromHtml(appInfo.getDescription(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            description.setText(Html.fromHtml(appInfo.getDescription()));
        }
        description.setMovementMethod(LinkMovementMethod.getInstance());

        tagContainer.removeAllViews();
        for (AppTags appTags : appInfo.getTags()) {
            TextView tv = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.view_tagtemplate, tagContainer, false);
            tv.setText(appTags.name());
            FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 8, 0);
            tv.setLayoutParams(params);
            tagContainer.addView(tv);
        }
        tagContainer.setVisibility(appInfo.getTags().isEmpty() ? View.GONE : View.VISIBLE);
        createMenus();

    }

    private void createMenus() {
        downloadItems.clear();
        contactItems.clear();
        for (Download d : downloads) {
            switch (d.getType()) {
                case GPLAY:
                    downloadItems.add(getResources().getString(R.string.gplay));
                    break;
                case FDROID:
                    downloadItems.add(getResources().getString(R.string.fdroid));
                    break;
                case WEBSITE:
                    downloadItems.add(getResources().getString(R.string.website) + ": " + d.getTarget());
                    break;
            }
        }
        for (Contact c : contacts) {
            switch (c.getType()) {
                case EMAIL:
                    contactItems.add(getResources().getString(R.string.mail) + ": " + c.getTarget());
                    break;
                case WEBSITE:
                    contactItems.add(getResources().getString(R.string.website) + ": " + c.getTarget());
                    break;
                case REDDIT_USERNAME:
                    contactItems.add(getResources().getString(R.string.reddit) + ": " + c.getTarget());
                    break;
            }
        }
    }

    private void openInChrome(String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        builder.setSecondaryToolbarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(getActivity(), Uri.parse(url));
    }

    private void displayToast(int message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayScreenshots(@Nullable ScrapeResult scrapeResult) {
        if (scrapeResult != null) {
            this.screenshotUrls = new ArrayList<>(scrapeResult.getScreenshotUrls());
            screenshotPager.setVisibility(View.VISIBLE);
            screenshotsAdapter.update(screenshotUrls);
        } else screenshotPager.setVisibility(View.GONE);
    }

    @Override
    public void displayIcon(@Nullable AppInfo appInfo) {
        if (appInfo != null) {
            iconFrame.setVisibility(View.VISIBLE);
            GlideApp.with(this)
                    .load(new IconRequest(appInfo))
                    .listener(new PlaceHolderRequestListener(iconImage, iconPlaceholder))
                    .into(iconImage);
        } else iconFrame.setVisibility(View.GONE);
    }

    @Override
    public void onScreenshotClicked(String url) {
        new ScreenshotDialog(getContext(), screenshotUrls, screenshotUrls.indexOf(url)).show();
    }

    private void fadeHeaderItems(float scrollRange, int verticalOffset) {
        float fadeFactor = 1.0f - Math.abs(2 * verticalOffset / scrollRange);
        secondaryTitle.setAlpha(fadeFactor);
        tagContainer.setAlpha(fadeFactor);
        iconFrame.setAlpha(fadeFactor);
    }
}
