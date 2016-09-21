package subreddit.android.appstore.backend.reddit.wiki.parser;

import java.util.Map;

import subreddit.android.appstore.backend.data.AppInfo;


public interface AppParser {
    enum Column {
        PRIMARY_CATEGORY, SECONDARY_CATEGORY,
        NAME, PRICE, DEVICE, DESCRIPTION, CONTACT
    }

    void parse(AppInfo appInfo, Map<Column, String> rawColumns);
}
