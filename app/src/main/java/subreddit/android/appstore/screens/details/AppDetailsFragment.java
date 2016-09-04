package subreddit.android.appstore.screens.details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wefika.flowlayout.FlowLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import subreddit.android.appstore.AppStoreApp;
import subreddit.android.appstore.R;
import subreddit.android.appstore.backend.AppInfo;
import subreddit.android.appstore.util.mvp.BasePresenterFragment;
import subreddit.android.appstore.util.mvp.PresenterFactory;


public class AppDetailsFragment extends BasePresenterFragment<AppDetailsContract.Presenter, AppDetailsContract.View>
        implements AppDetailsContract.View {
    @BindView(R.id.appname) TextView appName;
    @BindView(R.id.description) TextView description;
    @BindView(R.id.tag_container) FlowLayout tagContainer;

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
        appName.setText(appInfo.getAppName());
        description.setText(appInfo.getDescription());
        tagContainer.removeAllViews();
        for (AppInfo.Tag tag : appInfo.getTags()) {
            TextView tv = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.view_tagtemplate, tagContainer, false);
            tv.setText(tag.name());
            FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 8, 0);
            tv.setLayoutParams(params);
            tagContainer.addView(tv);
        }
        tagContainer.setVisibility(appInfo.getTags().isEmpty() ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.applist_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_download:
                // TODO implement download link
                return true;
            case R.id.menu_contact:
                Intent i = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
            "mailto","darken@darken.eu", null));
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"darken@darken.eu"});
                i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                try {
                        startActivity(Intent.createChooser(i, "Send email..."));
                } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
