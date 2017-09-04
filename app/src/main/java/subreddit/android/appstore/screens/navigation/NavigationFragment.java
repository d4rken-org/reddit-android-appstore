package subreddit.android.appstore.screens.navigation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import subreddit.android.appstore.AppStoreApp;
import subreddit.android.appstore.BuildConfig;
import subreddit.android.appstore.R;
import subreddit.android.appstore.backend.github.GithubApi;
import subreddit.android.appstore.screens.settings.SettingsActivity;
import subreddit.android.appstore.util.mvp.BasePresenterFragment;
import subreddit.android.appstore.util.mvp.PresenterFactory;


public class NavigationFragment extends BasePresenterFragment<NavigationContract.Presenter, NavigationContract.View>
        implements NavigationContract.View, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.footer_nav) NavigationView navFooter;
    @BindView(R.id.header_version_text) TextView versionText;
    @BindView(R.id.navigationview) NavigationView navigationView;
    @BindView(R.id.update_banner) View updateBanner;
    OnCategorySelectedListener onCategorySelectedListener;


    public static NavigationFragment newInstance() {
        return new NavigationFragment();
    }

    @Inject
    PresenterFactory<NavigationContract.Presenter> presenterFactory;
    private Unbinder unbinder;

    @NonNull
    @Override
    protected PresenterFactory<NavigationContract.Presenter> getPresenterFactory() {
        return presenterFactory;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        DaggerNavigationComponent.builder()
                .appComponent(AppStoreApp.Injector.INSTANCE.getAppComponent())
                .build().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        versionText.setText(getResources().getString(R.string.version) + " " + BuildConfig.VERSION_NAME);
        navFooter.setNavigationItemSelectedListener(item -> {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return false;
        });
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onDestroyView() {
        if (unbinder != null) unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void showUpdateSnackbar(GithubApi.Release release) {
        if (release == null) return;
        Snackbar
                .make(navigationView, R.string.update, Snackbar.LENGTH_LONG)
                .setAction(R.string.view_update, view -> getPresenter().buildChangelog(release)).show();
    }

    @Override
    public void showUpdateErrorToast() {
        Toast.makeText(getContext(), getContext().getResources().getString(R.string.update_error), Toast.LENGTH_LONG).show();
    }

    @Override
    public void enableUpdateAvailableText(GithubApi.Release release) {
        if (release != null) {
            updateBanner.setVisibility(View.VISIBLE);
            updateBanner.setOnClickListener(v -> getPresenter().buildChangelog(release));
        }
    }

    @Override
    public void showDownload(String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        builder.setSecondaryToolbarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(getActivity(), Uri.parse(url));
    }

    public void showChangelog(GithubApi.Release release) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Date date = release.publishDate;
        String desc = release.releaseDescription;
        String name = release.releaseName;
        String tag = release.tagName;
        String message =
                DateFormat.format("MMM", date) + " " + DateFormat.format("dd", date) + "\n";

        builder.setTitle(tag + ": " + name);

        if (buildFromGithub()) {
            builder.setMessage(message + desc);
        } else {
            builder.setMessage(message + desc + "\n" + R.string.build_from_fdroid);
        }

        builder.setPositiveButton(R.string.update_confirm, (dialog, id) -> getPresenter().downloadUpdate(release));
        builder.setNegativeButton(R.string.cancel, (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem clickedItem) {
        CategoryFilter clickedFilter = menuItemCategoryFilterMap.get(clickedItem);
        for (int i = 0; i < navigationView.getMenu().size(); i++) {
            MenuItem item = navigationView.getMenu().getItem(i);
            item.setChecked(false);
            CategoryFilter filter = menuItemCategoryFilterMap.get(item);
            item.setVisible(filter.getPrimaryCategory() == null
                    || filter.getSecondaryCategory() == null
                    || filter.getTertiaryCategory() == null
                    || (filter.getPrimaryCategory().equals(clickedFilter.getPrimaryCategory())) && filter.getSecondaryCategory().equals(clickedFilter.getSecondaryCategory())
                    || filter.getPrimaryCategory().equals(getContext().getString(R.string.app_category_everything))
                    || filter.getPrimaryCategory().equals(getContext().getString(R.string.app_category_new))
            );
        }
        clickedItem.setChecked(true);
        getPresenter().notifySelectedFilter(clickedFilter);
        onCategorySelectedListener.onCategorySelected(clickedFilter);

        return true;
    }

    Map<MenuItem, CategoryFilter> menuItemCategoryFilterMap = new HashMap<>();

    @Override
    public void showNavigationItems(NavigationData navigationData, CategoryFilter selectedItem) {

        Menu menu = navigationView.getMenu();
        menu.clear();
        menuItemCategoryFilterMap.clear();

        CategoryFilter noFilterFilter = new CategoryFilter();
        MenuItem noFilterItem = menu.add(Menu.NONE, Menu.NONE, Menu.NONE, noFilterFilter.getName(getContext()));
        menuItemCategoryFilterMap.put(noFilterItem, noFilterFilter);

        String n = getContext().getString(R.string.app_category_new);
        CategoryFilter newAppsFilter = new CategoryFilter(n, n, n, n);
        MenuItem newAppsItem = menu.add(Menu.NONE, Menu.NONE, Menu.NONE, newAppsFilter.getName(getContext()));
        menuItemCategoryFilterMap.put(newAppsItem, newAppsFilter);

        for (CategoryFilter primaryFilter : navigationData.getPrimaryCategories()) {
            int groupId = navigationData.getPrimaryCategories().indexOf(primaryFilter) + 2;
            MenuItem primaryItem = menu.add(Menu.NONE, Menu.NONE, Menu.NONE, primaryFilter.getName(getContext()));
            menuItemCategoryFilterMap.put(primaryItem, primaryFilter);

                for (CategoryFilter secondaryFilter : navigationData.getSecondaryCategories().get(primaryFilter)) {
                    int secondGroupId = navigationData.getSecondaryCategories().get(primaryFilter).indexOf(secondaryFilter) + 1000;
                    MenuItem secondaryItem = menu.add(groupId, Menu.NONE, Menu.NONE, "   " + secondaryFilter.getName(getContext()));
                    menuItemCategoryFilterMap.put(secondaryItem, secondaryFilter);
                    for (CategoryFilter tertiaryFilter : navigationData.getTertiaryCategories().get(secondaryFilter)) {
                        MenuItem tertiaryItem = menu.add(secondGroupId, Menu.NONE, Menu.NONE, "      " + tertiaryFilter.getName(getContext()));
                        menuItemCategoryFilterMap.put(tertiaryItem, tertiaryFilter);
                        tertiaryItem.setVisible(false);
                    }
                }
        }


        for (int i = 0; i < navigationView.getMenu().size(); i++) {
            MenuItem item = navigationView.getMenu().getItem(i);
            CategoryFilter filter = menuItemCategoryFilterMap.get(item);
            item.setChecked(filter.equals(selectedItem));
            if (item.isChecked()) break; // only one checked
        }
    }

    @Override
    public void selectFilter(CategoryFilter toSelect) {
        for (int i = 0; i < navigationView.getMenu().size(); i++) {
            MenuItem item = navigationView.getMenu().getItem(i);
            CategoryFilter filter = menuItemCategoryFilterMap.get(item);
            if (filter.equals(toSelect)) {
                item.setChecked(true);
                break;
            }
        }
        onCategorySelectedListener.onCategorySelected(toSelect);
    }

    public void setOnCategorySelectedListener(OnCategorySelectedListener onCategorySelectedListener) {
        this.onCategorySelectedListener = onCategorySelectedListener;
    }


    public interface OnCategorySelectedListener {
        void onCategorySelected(CategoryFilter filter);
    }

    private boolean buildFromGithub() {
        List<String> signatures =
                AppStoreApp.getSignatures(getContext(), "subreddit.android.appstore");

        for (String signature : signatures) {
            if (signature.equals(AppStoreApp.GITHUB_SIGNATURE)) return true;
        }
        return false;
    }

}
