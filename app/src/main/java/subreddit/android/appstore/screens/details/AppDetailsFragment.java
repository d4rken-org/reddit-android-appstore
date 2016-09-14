package subreddit.android.appstore.screens.details;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wefika.flowlayout.FlowLayout;

import java.util.ArrayList;
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
import subreddit.android.appstore.util.Preconditions;
import subreddit.android.appstore.util.mvp.BasePresenterFragment;
import subreddit.android.appstore.util.mvp.PresenterFactory;
import subreddit.android.appstore.util.ui.glide.IconRequest;
import subreddit.android.appstore.util.ui.glide.PlaceHolderRequestListener;


public class AppDetailsFragment extends BasePresenterFragment<AppDetailsContract.Presenter, AppDetailsContract.View>
        implements AppDetailsContract.View, ScreenshotsAdapter.ScreenshotClickedListener {
    @BindView(R.id.description) TextView description;
    @BindView(R.id.tag_container) FlowLayout tagContainer;
    @BindView(R.id.details_download) Button downloadButton;
    @BindView(R.id.details_contact) Button contactButton;
    @BindView(R.id.icon_image) ImageView iconImage;
    @BindView(R.id.icon_placeholder) View iconPlaceholder;
    @BindView(R.id.appname) TextView appName;
    @BindView(R.id.screenshot_pager) ViewPager screenshotPager;

    private PopupMenu downloadPopup, contactPopup;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_appdetails_layout, container, false);
        unbinder = ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        screenshotsAdapter = new ScreenshotsAdapter(getContext());
        screenshotPager.setAdapter(screenshotsAdapter);
        screenshotsAdapter.setScreenshotClickedListener(this);
        downloadPopup = new PopupMenu(getContext(), downloadButton);
        contactPopup = new PopupMenu(getContext(), contactButton);
        downloadPopup.inflate(R.menu.placeholder_popup_download);
        contactPopup.inflate(R.menu.placeholder_popup_contact);

        downloadPopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                openDownload(downloads.get(menuItem.getItemId()));
                return true;
            }
        });

        contactPopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                openContact(contacts.get(menuItem.getItemId()));
                return true;
            }
        });
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

    @OnClick(R.id.details_download)
    void onDownloadClicked(View view) {
        if (downloads.size() < 2) {
            openDownload(downloads.get(0));
        } else {
            downloadPopup.show();
        }
    }

    @OnClick(R.id.details_contact)
    void onContactClicked(View view) {
        if (contacts.size() < 2) {
            openContact(contacts.get(0));
        } else {
            contactPopup.show();
        }
    }

    void openDownload(Download d) {
        startActivity(new Intent(Intent.ACTION_VIEW, d.getDownloadUri()));
    }

    void openContact(Contact c) {
        switch (c.getType()) {
            case EMAIL:
                startActivity(new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", c.getTarget(), null)));
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
    public void displayDetails(AppInfo appInfo) {
        Glide.with(this)
                .load(new IconRequest(appInfo))
                .listener(new PlaceHolderRequestListener(iconImage, iconPlaceholder))
                .into(iconImage);
        appName.setText(appInfo.getAppName());
        downloads = new ArrayList<>(appInfo.getDownloads());
        contacts = new ArrayList<>(appInfo.getContacts());
        description.setText(appInfo.getDescription());
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
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        Preconditions.checkNotNull(actionBar);
        actionBar.setSubtitle(appInfo.getAppName());
        createMenus();

    }

    private void createMenus() {
        downloadPopup.getMenu().clear();
        contactPopup.getMenu().clear();
        for (Download d : downloads) {
            switch (d.getType()) {
                case GPLAY:
                    downloadPopup.getMenu().add(Menu.NONE, downloads.indexOf(d), Menu.NONE, R.string.gplay);
                    break;
                case FDROID:
                    downloadPopup.getMenu().add(Menu.NONE, downloads.indexOf(d), Menu.NONE, R.string.fdroid);
                    break;
                case WEBSITE:
                    downloadPopup.getMenu().add(Menu.NONE, downloads.indexOf(d), Menu.NONE, R.string.website);
                    break;
            }
        }
        for (Contact c : contacts) {
            switch (c.getType()) {
                case EMAIL:
                    contactPopup.getMenu().add(Menu.NONE, contacts.indexOf(c), Menu.NONE, R.string.mail);
                    break;
                case WEBSITE:
                    contactPopup.getMenu().add(Menu.NONE, contacts.indexOf(c), Menu.NONE, R.string.website);
                    break;
                case REDDIT_USERNAME:
                    contactPopup.getMenu().add(Menu.NONE, contacts.indexOf(c), Menu.NONE, R.string.reddit);
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

    @Override
    public void displayScreenshots(ScrapeResult scrapeResult) {
        screenshotsAdapter.update(new ArrayList<String>(scrapeResult.getScreenshotUrls()));
    }

    @Override
    public void onScreenshotClicked(String url) {
        new ScreenshotDialog(getContext(), url).show();
    }

    @Override
    public void closeDetails() {
        getActivity().finish();
    }
}
