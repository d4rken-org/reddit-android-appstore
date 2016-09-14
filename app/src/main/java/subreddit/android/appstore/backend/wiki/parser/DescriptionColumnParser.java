package subreddit.android.appstore.backend.wiki.parser;

import android.text.Html;

import java.util.Map;

import subreddit.android.appstore.backend.data.AppInfo;

public class DescriptionColumnParser implements AppParser {

    @Override
    public void parse(AppInfo appInfo, Map<Column, String> rawColumns) {
        final String rawDescriptionString = rawColumns.get(Column.DESCRIPTION);
        // TODO we need to deal with markdown in the descriptions
        //noinspection deprecation
        appInfo.setDescription(Html.fromHtml(rawDescriptionString).toString());
    }
}
