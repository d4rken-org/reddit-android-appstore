package subreddit.android.appstore.backend.parser;

import java.util.Map;

import subreddit.android.appstore.AppStoreApp;
import subreddit.android.appstore.backend.data.AppInfo;
import subreddit.android.appstore.backend.data.AppTags;


public class PriceColumnParser implements AppParser {
    static final String TAG = AppStoreApp.LOGPREFIX + "PriceColumnParser";

    @Override
    public void parse(AppInfo appInfo, Map<Column, String> rawColumns) {
        final String rawPriceString = rawColumns.get(Column.PRICE);

        if (rawPriceString.toLowerCase().contains("free")) appInfo.getTags().add(AppTags.FREE);
        if (rawPriceString.toLowerCase().contains("paid")) appInfo.getTags().add(AppTags.PAID);
    }
}
