package subreddit.android.appstore.backend.reddit.wiki.parser;

import android.text.Html;

public class EncodingFixer {

    public String fixHtmlEscapes(String input) {
        //noinspection deprecation
        return Html.fromHtml(input).toString();
    }

}
