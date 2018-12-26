package subreddit.android.appstore.screens.navigation;


import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import subreddit.android.appstore.backend.data.AppInfo;

public class NavigationData {
    private final List<CategoryFilter> primaryCategories = new ArrayList<>();
    private final Map<CategoryFilter, List<CategoryFilter>> secondaryCategories = new HashMap<>();
    private final Map<CategoryFilter, List<CategoryFilter>> tertiaryCategories = new HashMap<>();
    int secondaryCount = 0;
    int tertiaryCount = 0;

    public void addApp(@NonNull AppInfo appInfo) {
        // TODO order this
        CategoryFilter primaryFilter = new CategoryFilter(appInfo.getPrimaryCategory(), null, null, null);
        if (!primaryCategories.contains(primaryFilter)) {
            primaryCategories.add(primaryFilter);
        }

        List<CategoryFilter> secondary = secondaryCategories.get(primaryFilter);
        if (secondary == null) secondary = new ArrayList<>();
        CategoryFilter secondaryFilter = new CategoryFilter(appInfo.getPrimaryCategory(), appInfo.getSecondaryCategory(), null, null);
        if (!secondary.contains(secondaryFilter)) {
            secondaryCount++;
            secondary.add(secondaryFilter);
        }
        secondaryCategories.put(primaryFilter, secondary);

        List<CategoryFilter> tertiary = tertiaryCategories.get(secondaryFilter);
        if (tertiary == null) tertiary = new ArrayList<>();
        CategoryFilter tertiaryFilter = new CategoryFilter(appInfo.getPrimaryCategory(), appInfo.getSecondaryCategory(), appInfo.getTertiaryCategory(), null);
        if (!tertiary.contains(tertiaryFilter)) {
            tertiaryCount++;
            tertiary.add(tertiaryFilter);
        }
        tertiaryCategories.put(secondaryFilter, tertiary);
    }

    public List<CategoryFilter> getPrimaryCategories() {
        return primaryCategories;
    }

    public Map<CategoryFilter, List<CategoryFilter>> getSecondaryCategories() {
        return secondaryCategories;
    }

    public Map<CategoryFilter, List<CategoryFilter>> getTertiaryCategories() {
        return tertiaryCategories;
    }

    public void addPrimaryCategory(CategoryFilter category) {
        primaryCategories.add(1, category);
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "NavigationData(%d primary, %d secondary, %d tertiary)", primaryCategories.size(), secondaryCount, tertiaryCount);
    }
}
