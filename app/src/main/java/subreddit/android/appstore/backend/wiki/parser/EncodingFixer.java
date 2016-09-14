package subreddit.android.appstore.backend.wiki.parser;

import android.text.Html;

public class EncodingFixer {

    public String fixHtmlEscapes(String input) {
        //noinspection deprecation
        return Html.fromHtml(input).toString();
    }

}
