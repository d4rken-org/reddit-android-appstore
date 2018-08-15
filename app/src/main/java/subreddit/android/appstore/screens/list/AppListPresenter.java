package subreddit.android.appstore.screens.list;

import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import subreddit.android.appstore.backend.data.AppInfo;
import subreddit.android.appstore.backend.data.AppTags;
import subreddit.android.appstore.backend.reddit.wiki.WikiRepository;
import subreddit.android.appstore.screens.navigation.CategoryFilter;
import subreddit.android.appstore.screens.settings.SettingsActivity;
import timber.log.Timber;


public class AppListPresenter implements AppListContract.Presenter {
    final WikiRepository repository;
    final CategoryFilter categoryFilter;
    private SharedPreferences sharedPreferences;
    private Disposable listUpdater;
    private Disposable tagUpdater;
    AppListContract.View view;

    public AppListPresenter(WikiRepository repository, CategoryFilter categoryFilter, SharedPreferences preferences) {
        this.repository = repository;
        this.categoryFilter = categoryFilter;
        this.sharedPreferences = preferences;
    }

    @Override
    public void onCreate(Bundle bundle) {
    }

    @Override
    public void onAttachView(final AppListContract.View view) {
        this.view = view;
        view.showLoadingScreen();
        Observable<List<AppInfo>> filteredData = repository.getAppList()
                .observeOn(Schedulers.computation())
                .map(appInfos -> {
                    ArrayList<AppInfo> data = new ArrayList<>(appInfos);
                    List<AppInfo> filteredData1 = new ArrayList<>();
                    Timber.d("Filtering to %s %s %s %s",categoryFilter.getPrimaryCategory(), categoryFilter.getSecondaryCategory(), categoryFilter.getTertiaryCategory(), categoryFilter.isNewlyAdded());
                    for (AppInfo app : data) {
                        if (app.getTags().contains(AppTags.NEW) && categoryFilter.isNewlyAdded() != null) {
                            filteredData1.add(app);
                            continue;
                        }
                        if ((app.getPrimaryCategory().equals(categoryFilter.getPrimaryCategory()) || categoryFilter.getPrimaryCategory() == null) &&
                                (app.getSecondaryCategory().equals(categoryFilter.getSecondaryCategory()) || categoryFilter.getSecondaryCategory() == null) &&
                                (app.getTertiaryCategory().equals(categoryFilter.getTertiaryCategory()) || categoryFilter.getTertiaryCategory() == null)) {
                            filteredData1.add(app);
                        }
                    }
                    Collections.sort(filteredData1);
                    return filteredData1;
                }).replay().refCount();

        listUpdater = filteredData
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(appInfos -> {
                    Timber.d("showAppList(%s items)", appInfos.size());
                    AppListPresenter.this.view.showAppList(appInfos);

                    if (saveTagFiltersSelected()) {
                        AppListPresenter.this.view.restoreSelectedTags(getSavedTagFilters());
                    }
                });

        tagUpdater = filteredData
                .observeOn(Schedulers.computation())
                .map((Function<Collection<AppInfo>, TagMap>) TagMap::new)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tagMap -> {
                    Timber.d("updateTagCount(%s)", tagMap);
                    AppListPresenter.this.view.updateTagCount(tagMap);
                });

        filteredData
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(appInfos -> {
                    if (appInfos.size()<1) {
                        view.showError();
                    }
                });
    }

    @Override
    public void onDetachView() {
        if (saveTagFiltersSelected()) {
            saveSelectedTags(AppListPresenter.this.view.getSelectedTags());
        }
        listUpdater.dispose();
        tagUpdater.dispose();
        view = null;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void refreshData() {
        view.showLoadingScreen();
        repository.refresh();
    }

    private boolean saveTagFiltersSelected() {
        return sharedPreferences.getBoolean(SettingsActivity.PREF_KEY_SAVE_TAG_FILTERS, false);
    }

    private Collection<AppTags> getSavedTagFilters() {
        List<AppTags> data = Arrays.asList(AppTags.values());
        Collection<AppTags> appTags = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            if (sharedPreferences.getBoolean("savedTags_" + i, false)) {
                appTags.add(data.get(i));
            }
        }
        return appTags;
    }

    public void saveSelectedTags(Collection<AppTags> appTags) {
        List<AppTags> data = Arrays.asList(AppTags.values());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        for (int i = 0; i < data.size(); i++) {
            if (appTags.contains(data.get(i))) {
                editor.putBoolean("savedTags_" + i, true);
            }
        }
        editor.commit();
    }
}
