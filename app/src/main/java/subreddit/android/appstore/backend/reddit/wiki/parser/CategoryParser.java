package subreddit.android.appstore.backend.reddit.wiki.parser;

import java.util.Map;

import subreddit.android.appstore.backend.data.AppInfo;


public class CategoryParser extends BaseParser {

    public CategoryParser(EncodingFixer encodingFixer) {
        super(encodingFixer);
    }

    @Override
    public void parse(AppInfo appInfo, Map<Column, String> rawColumns) {
        //noinspection deprecation
        appInfo.setPrimaryCategory(fixEncoding(rawColumns.get(Column.PRIMARY_CATEGORY)));
        //noinspection deprecation
        appInfo.setSecondaryCategory(fixEncoding(rawColumns.get(Column.SECONDARY_CATEGORY)));
    }
}
