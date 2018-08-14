package subreddit.android.appstore.backend.reddit.wiki.parser;

import java.util.ArrayList;
import java.util.List;

import subreddit.android.appstore.backend.data.AppInfo;


public class CategoryParser {

    private final EncodingFixer encodingFixer;

    public CategoryParser(EncodingFixer encodingFixer) {

        this.encodingFixer = encodingFixer;
    }

    public void parse(AppInfo appInfo, List<String> rawCategories) {
        List<String> processedCategories = new ArrayList<>();
        for (String unprocessed : rawCategories) processedCategories.add(encodingFixer.fixHtmlEscapes(unprocessed));
        appInfo.setCategories(processedCategories);
    }
}
