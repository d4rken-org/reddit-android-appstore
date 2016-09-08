package subreddit.android.appstore.backend.parser;

import java.util.Map;

import subreddit.android.appstore.AppStoreApp;
import subreddit.android.appstore.backend.data.AppInfo;
import subreddit.android.appstore.backend.data.AppTags;


public class DeviceColumnParser implements AppParser {
    static final String TAG = AppStoreApp.LOGPREFIX + "DeviceColumnParser";

    @Override
    public void parse(AppInfo appInfo, Map<Column, String> rawColumns) {
        final String rawDeviceString = rawColumns.get(Column.DEVICE);

        if (rawDeviceString.toLowerCase().contains("watch")) appInfo.getTags().add(AppTags.WEAR);
        if (rawDeviceString.toLowerCase().contains("phone")) appInfo.getTags().add(AppTags.PHONE);
        if (rawDeviceString.toLowerCase().contains("tablet")) appInfo.getTags().add(AppTags.TABLET);
    }
}
