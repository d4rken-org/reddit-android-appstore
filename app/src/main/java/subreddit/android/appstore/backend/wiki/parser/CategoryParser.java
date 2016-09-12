package subreddit.android.appstore.backend.wiki.parser;

import android.text.Html;

import java.util.Map;

import subreddit.android.appstore.AppStoreApp;
import subreddit.android.appstore.backend.data.AppInfo;


public class CategoryParser implements AppParser {
    static final String TAG = AppStoreApp.LOGPREFIX + "CategoryParser";

    @Override
    public void parse(AppInfo appInfo, Map<Column, String> rawColumns) {
        //noinspection deprecation
        appInfo.setPrimaryCategory(Html.fromHtml(rawColumns.get(Column.PRIMARY_CATEGORY)).toString());
        //noinspection deprecation
        appInfo.setSecondaryCategory(Html.fromHtml(rawColumns.get(Column.SECONDARY_CATEGORY)).toString());
    }
}
