package subreddit.android.appstore.backend.reddit.wiki.parser;

import java.util.Map;

import subreddit.android.appstore.backend.data.AppInfo;
import subreddit.android.appstore.backend.data.AppTags;


public class PriceColumnParser extends BaseParser {

    public PriceColumnParser(EncodingFixer encodingFixer) {
        super(encodingFixer);
    }

    @Override
    public void parse(AppInfo appInfo, Map<Column, String> rawColumns) {
        final String rawPriceString = rawColumns.get(Column.PRICE);

        if (rawPriceString.toLowerCase().contains("free")) appInfo.getTags().add(AppTags.FREE);
        if (rawPriceString.toLowerCase().contains("paid")) appInfo.getTags().add(AppTags.PAID);
    }
}
