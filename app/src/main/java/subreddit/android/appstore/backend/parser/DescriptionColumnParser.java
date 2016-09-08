package subreddit.android.appstore.backend.parser;

import java.util.Map;

import subreddit.android.appstore.AppStoreApp;
import subreddit.android.appstore.backend.data.AppInfo;

public class DescriptionColumnParser implements AppParser {
    static final String TAG = AppStoreApp.LOGPREFIX + "DescriptionColumnParser";

    @Override
    public void parse(AppInfo appInfo, Map<Column, String> rawColumns) {
        final String rawDescriptionString = rawColumns.get(Column.DESCRIPTION);
        // TODO we need to deal with markdown in the descriptions
        appInfo.setDescription(rawDescriptionString);
    }
}
