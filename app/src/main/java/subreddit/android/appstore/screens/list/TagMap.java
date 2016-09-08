package subreddit.android.appstore.screens.list;


import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import subreddit.android.appstore.backend.data.AppInfo;
import subreddit.android.appstore.backend.data.AppTags;

public class TagMap {
    final Map<AppTags, Integer> tagCounts = new HashMap<>();

    public TagMap() {
    }

    public TagMap(Collection<AppInfo> appInfos) {
        for (AppTags tag : AppTags.values()) tagCounts.put(tag, 0);

        for (AppInfo info : appInfos) {
            for (AppTags tag : info.getTags()) tagCounts.put(tag, tagCounts.get(tag) + 1);
        }
    }

    public int getCount(AppTags tag) {
        if (tagCounts.containsKey(tag)) return tagCounts.get(tag);
        else return 0;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "TagMap(%s)", tagCounts);
    }
}
