package subreddit.android.appstore.screens.navigation;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import subreddit.android.appstore.AppStoreApp;
import subreddit.android.appstore.BuildConfig;
import subreddit.android.appstore.R;
import subreddit.android.appstore.screens.settings.SettingsActivity;
import subreddit.android.appstore.util.mvp.BasePresenterFragment;
import subreddit.android.appstore.util.mvp.PresenterFactory;


public class NavigationFragment extends BasePresenterFragment<NavigationContract.Presenter, NavigationContract.View>
        implements NavigationContract.View, NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    @BindView(R.id.footer_nav) NavigationView navFooter;
    @BindView(R.id.header_version_text) TextView versionText;
    @BindView(R.id.app_category_header) TextView appHeader;
    @BindView(R.id.game_category_header) TextView gameHeader;
    @BindView(R.id.app_category_submenu) NavigationView appMenu;
    @BindView(R.id.game_category_submenu) NavigationView gameMenu;
    @BindView(R.id.nav_scroll) ScrollView navScroll;

    OnCategorySelectedListener l;

    private NavigationView.OnNavigationItemSelectedListener appListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            l.onCategorySelected(new CategoryFilter("Apps",item.getTitle().toString()));
            return true;
        }
    };

    private NavigationView.OnNavigationItemSelectedListener gameListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            l.onCategorySelected(new CategoryFilter("Games",item.getTitle().toString()));
            return true;
        }
    };

    @Override
    public void onClick(View view) {
        for (int i=0;i<appMenu.getMenu().size();i++) {
            appMenu.getMenu().getItem(i).setChecked(false);
        }
        for (int i=0;i<gameMenu.getMenu().size();i++) {
            gameMenu.getMenu().getItem(i).setChecked(false);
        }
        switch (view.getId()) {
            case R.id.app_category_header: {
                gameMenu.setVisibility(View.GONE);
                appMenu.setVisibility(View.VISIBLE);
                l.onCategorySelected(new CategoryFilter("Apps",null));
                break;
            }case R.id.game_category_header: {
                appMenu.setVisibility(View.GONE);
                gameMenu.setVisibility(View.VISIBLE);
                l.onCategorySelected(new CategoryFilter("Games",null));
            }
        }
    }

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
        navFooter.setNavigationItemSelectedListener(this);
        versionText.setText(getResources().getString(R.string.version) + " " + BuildConfig.VERSION_NAME);
        appMenu.setNavigationItemSelectedListener(appListener);
        gameMenu.setNavigationItemSelectedListener(gameListener);
        appHeader.setOnClickListener(this);
        gameHeader.setOnClickListener(this);
        if (Build.VERSION.SDK_INT>20) {
            appMenu.setElevation(0);
            gameMenu.setElevation(0);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(getActivity(), SettingsActivity.class));
        return false;
    }

    @Override
    public void onDestroyView() {
        if (unbinder != null) unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void showNavigationItems(NavigationData navigationData) {
        appHeader.setText(navigationData.getPrimaryCategories().get(0));
        gameHeader.setText(navigationData.getPrimaryCategories().get(1));
        for (String subCategory : navigationData.getSecondaryCategories().get(navigationData.getPrimaryCategories().get(0))) {
            appMenu.getMenu().add(Html.fromHtml(subCategory)).setCheckable(true);
        }
        for (String subCategory : navigationData.getSecondaryCategories().get(navigationData.getPrimaryCategories().get(1))) {
            gameMenu.getMenu().add(Html.fromHtml(subCategory)).setCheckable(true);
        }
    }

    public void setOnCategorySelectedListener(OnCategorySelectedListener l) {
        this.l = l;
    }

    public interface OnCategorySelectedListener {
        void onCategorySelected(CategoryFilter filter);
    }

}
