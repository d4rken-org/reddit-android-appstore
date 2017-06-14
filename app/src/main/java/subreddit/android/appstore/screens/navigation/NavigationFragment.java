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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import subreddit.android.appstore.AppStoreApp;
import subreddit.android.appstore.BuildConfig;
import subreddit.android.appstore.R;
import subreddit.android.appstore.backend.github.SelfUpdater;
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
    public void showUpdateSnackbar(SelfUpdater.Release release) {
        if (release == null) return;
        Snackbar
                .make(navigationView, R.string.update, Snackbar.LENGTH_LONG)
                .setAction(R.string.update_confirm, view -> getPresenter().downloadUpdate(release)).show();
    }

    @Override
    public void enableUpdateAvailableText(SelfUpdater.Release release) {
        if (release != null) {
            updateBanner.setVisibility(View.VISIBLE);
            updateBanner.setOnClickListener(v -> getPresenter().downloadUpdate(release));
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
                    || (filter.getPrimaryCategory().equals(clickedFilter.getPrimaryCategory())) && filter.getSecondaryCategory().equals(clickedFilter.getSecondaryCategory()));
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

        for (CategoryFilter primaryFilter : navigationData.getPrimaryCategories()) {
            int groupId = navigationData.getPrimaryCategories().indexOf(primaryFilter) + 1;
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
        MenuItem primaryItem = menu.add(Menu.NONE, Menu.NONE, Menu.NONE, "New");
        menuItemCategoryFilterMap.put(primaryItem, new CategoryFilter("New", "New", "New", "New"));

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

}
