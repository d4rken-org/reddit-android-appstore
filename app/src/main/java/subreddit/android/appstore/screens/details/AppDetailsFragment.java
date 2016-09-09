package subreddit.android.appstore.screens.details;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.wefika.flowlayout.FlowLayout;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import subreddit.android.appstore.AppStoreApp;
import subreddit.android.appstore.R;
import subreddit.android.appstore.backend.data.AppInfo;
import subreddit.android.appstore.backend.data.AppTags;
import subreddit.android.appstore.backend.data.Contact;
import subreddit.android.appstore.backend.data.Download;
import subreddit.android.appstore.util.mvp.BasePresenterFragment;
import subreddit.android.appstore.util.mvp.PresenterFactory;


public class AppDetailsFragment extends BasePresenterFragment<AppDetailsContract.Presenter, AppDetailsContract.View>
        implements AppDetailsContract.View, View.OnClickListener {
    @BindView(R.id.description) TextView description;
    @BindView(R.id.tag_container) FlowLayout tagContainer;
    @BindView(R.id.details_download) Button downloadButton;
    @BindView(R.id.details_contact) Button contactButton;

    private PopupMenu downloadPopup, contactPopup;

    private ArrayList<Download> downloads = new ArrayList<>();
    private ArrayList<Contact> contacts = new ArrayList<>();
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
        switch (view.getId()) {
            case R.id.details_download: {
                if (downloads.size()<2) {
                    openDownload(downloads.get(0));
                } else {
                    downloadPopup.show();
                }
                break;
            }case R.id.details_contact: {
                if (contacts.size()<2) {
                    openContact(contacts.get(0));
                } else {
                    contactPopup.show();
                }
            }
        }
    }

    private void openDownload(Download d) {
        switch (d.getType()) {
            case GPLAY: {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(d.getTarget())));
                break;
            }
            case WEBSITE: {
                //TODO: WEBSITE
                break;
            }
            case FDROID: {
                //TODO: FDROID
            }
        }
    }

    private void openContact(Contact c) {
        switch (c.getType()) {
            case MAIL: {
                startActivity(new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",c.getTarget(), null)));
                break;
            }
            case WEBSITE: {
                //TODO: WEBSITE
                break;
            }
            case INSTAGRAM: {
                //TODO: INSTAGRAM
                break;
            }
            case REDDIT: {
                //TODO: REDDIT
                break;
            }
            case TWITTER: {
                //TODO: TWITTER
            }
        }
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
        contactButton.setOnClickListener(this);
        downloadButton.setOnClickListener(this);
        downloadPopup = new PopupMenu(getContext(),downloadButton);
        contactPopup = new PopupMenu(getContext(),contactButton);
        downloadPopup.inflate(R.menu.placeholder_popup_download);
        contactPopup.inflate(R.menu.placeholder_popup_contact);

        downloadPopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                openDownload(downloads.get(menuItem.getOrder()));
                return true;
            }
        });

        contactPopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                openContact(contacts.get(menuItem.getOrder()));
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

    @Override
    public void onShowDetails(AppInfo appInfo) {
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

        getActivity().setTitle(appInfo.getAppName());

        createMenus();
    }

    private void createMenus() {
        downloadPopup.getMenu().clear();
        contactPopup.getMenu().clear();
        for (Download d : downloads) {
            switch (d.getType()) {
                case GPLAY: {
                    downloadPopup.getMenu().add(R.string.gplay);
                    break;
                }
                case FDROID: {
                    downloadPopup.getMenu().add(R.string.fdroid);
                    break;
                }
                case WEBSITE: {
                    downloadPopup.getMenu().add(R.string.website);
                }
            }
        }

        for (Contact c : contacts) {
            switch (c.getType()) {
                case MAIL: {
                    contactPopup.getMenu().add(R.string.mail);
                    break;
                }
                case WEBSITE: {
                    contactPopup.getMenu().add(R.string.website);
                    break;
                }
                case REDDIT: {
                    contactPopup.getMenu().add(R.string.reddit);
                    break;
                }
                case TWITTER: {
                    contactPopup.getMenu().add(R.string.twitter);
                    break;
                }
                case INSTAGRAM: {
                    contactPopup.getMenu().add(R.string.instagram);
                }
            }

        }
    }

    @Override
    public void closeDetails() {
        getActivity().finish();
    }
}
