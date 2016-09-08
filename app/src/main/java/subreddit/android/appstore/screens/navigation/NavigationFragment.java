package subreddit.android.appstore.screens.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import subreddit.android.appstore.AppStoreApp;
import subreddit.android.appstore.R;
import subreddit.android.appstore.screens.settings.SettingsActivity;
import subreddit.android.appstore.util.mvp.BasePresenterFragment;
import subreddit.android.appstore.util.mvp.PresenterFactory;


public class NavigationFragment extends BasePresenterFragment<NavigationContract.Presenter, NavigationContract.View>
        implements NavigationContract.View, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.footer_nav) NavigationView nav_footer;

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
        nav_footer.setNavigationItemSelectedListener(this);
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

    }
}
