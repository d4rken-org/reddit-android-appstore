package subreddit.android.appstore.backend.parser;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import subreddit.android.appstore.AppStoreApp;
import subreddit.android.appstore.backend.data.AppInfo;
import subreddit.android.appstore.backend.data.Download;
import timber.log.Timber;


public class NameColumnParser implements AppParser {
    static final String TAG = AppStoreApp.LOGPREFIX + "NameColumnparser";
    static final Pattern NAME_PATTERN = Pattern.compile("^(?:\\[(.+)\\]\\((.+)\\))$");

    @Override
    public void parse(AppInfo appInfo, Map<Column, String> rawColumns) {
        final String rawNameString = rawColumns.get(Column.NAME);

        String appName;
        String downloadUrl;
        Download.Type downloadType;

        Matcher matcher = NAME_PATTERN.matcher(rawNameString);
        if (matcher.matches()) {
            appName = matcher.group(1);

            downloadUrl = matcher.group(2);
            // TODO more parsing of different types
            if (downloadUrl.contains("://play.google.com/store") || downloadUrl.contains("://market.android.com")) {
                downloadType = Download.Type.GPLAY;
            } else {
                downloadType = Download.Type.UNKNOWN;
            }
        } else {
            appName = rawNameString;
            downloadUrl = rawNameString;
            downloadType = Download.Type.UNKNOWN;
            Timber.tag(TAG).w("parseNameField(%s) failed", rawNameString);
        }
        appInfo.setAppName(appName);
        appInfo.addDownload(new Download(downloadType, downloadUrl));
    }
}
