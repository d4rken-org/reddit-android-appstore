package subreddit.android.appstore.backend.parser;

import java.util.Map;

import subreddit.android.appstore.AppStoreApp;
import subreddit.android.appstore.backend.data.AppInfo;


public class CategoryParser implements AppParser {
    static final String TAG = AppStoreApp.LOGPREFIX + "CategoryParser";

    @Override
    public void parse(AppInfo appInfo, Map<Column, String> rawColumns) {
        appInfo.setPrimaryCategory(rawColumns.get(Column.PRIMARY_CATEGORY));
        appInfo.setSecondaryCategory(rawColumns.get(Column.SECONDARY_CATEGORY));
    }
}
