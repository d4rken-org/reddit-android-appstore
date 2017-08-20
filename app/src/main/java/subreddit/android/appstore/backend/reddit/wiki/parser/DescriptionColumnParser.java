package subreddit.android.appstore.backend.reddit.wiki.parser;

import java.util.Map;

import subreddit.android.appstore.backend.data.AppInfo;

public class DescriptionColumnParser extends BaseParser {

    public DescriptionColumnParser(EncodingFixer encodingFixer) {
        super(encodingFixer);
    }

    @Override
    public void parse(AppInfo appInfo, Map<Column, String> rawColumns) {
        final String rawDescriptionString = rawColumns.get(Column.DESCRIPTION);

        String fixedDescription = fixEncoding(rawDescriptionString);
        fixedDescription = fixMarkdown(fixedDescription);
        fixedDescription = convertSubredditsToLinks(fixedDescription);

        appInfo.setDescription(fixedDescription);
    }
}
