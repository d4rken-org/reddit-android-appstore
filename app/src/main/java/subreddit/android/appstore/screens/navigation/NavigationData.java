package subreddit.android.appstore.screens.navigation;


import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import subreddit.android.appstore.backend.data.AppInfo;

public class NavigationData {
    private final List<String> primaryCategories = new ArrayList<>();
    private final Map<String, List<String>> secondaryCategories = new HashMap<>();
    int secondaryCount = 0;

    public void addApp(@NonNull AppInfo appInfo) {
        // TODO order this
        if (!primaryCategories.contains(appInfo.getPrimaryCategory())) {
            primaryCategories.add(appInfo.getPrimaryCategory());
        }
        List<String> secondary = secondaryCategories.get(appInfo.getPrimaryCategory());
        if (secondary == null) secondary = new ArrayList<>();
        if (!secondary.contains(appInfo.getSecondaryCategory())) {
            secondaryCount++;
            secondary.add(appInfo.getSecondaryCategory());
        }
        secondaryCategories.put(appInfo.getPrimaryCategory(), secondary);
    }

    public List<String> getPrimaryCategories() {
        return primaryCategories;
    }

    public Map<String, List<String>> getSecondaryCategories() {
        return secondaryCategories;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "NavigationData(%d primary, %d secondary)", primaryCategories.size(), secondaryCount);
    }
}
