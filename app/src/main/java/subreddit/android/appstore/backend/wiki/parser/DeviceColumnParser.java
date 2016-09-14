package subreddit.android.appstore.backend.wiki.parser;

import java.util.Map;

import subreddit.android.appstore.backend.data.AppInfo;
import subreddit.android.appstore.backend.data.AppTags;


public class DeviceColumnParser extends BaseParser {

    public DeviceColumnParser(EncodingFixer encodingFixer) {
        super(encodingFixer);
    }

    @Override
    public void parse(AppInfo appInfo, Map<Column, String> rawColumns) {
        final String rawDeviceString = rawColumns.get(Column.DEVICE);

        if (rawDeviceString.toLowerCase().contains("watch")) appInfo.getTags().add(AppTags.WEAR);
        if (rawDeviceString.toLowerCase().contains("phone")) appInfo.getTags().add(AppTags.PHONE);
        if (rawDeviceString.toLowerCase().contains("tablet")) appInfo.getTags().add(AppTags.TABLET);
    }
}
