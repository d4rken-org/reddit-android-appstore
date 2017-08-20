package subreddit.android.appstore.backend.reddit.wiki.parser;

import android.text.Html;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EncodingFixer {
    private static final Pattern URL_MARKDOWN_PATTERN = Pattern.compile("(\\[(.*?)\\]\\s{0,1}\\((http.*?)\\))");
    private static final Pattern BOLD_MARKDOWN_PATTERN = Pattern.compile("(\\*\\*(.*?)\\*\\*)");
    private static final Pattern SUBREDDIT_PATTERN = Pattern.compile("(\\/r\\/.*?(?!\\S))");

    public String fixHtmlEscapes(String input) {
        //noinspection deprecation
        return Html.fromHtml(input).toString();
    }

    public String convertStringToHtml(String string) {
        // Converts link markdown to HTML
        String output = string;
        Matcher urlMarkdownMatcher = URL_MARKDOWN_PATTERN.matcher(output);
        while (urlMarkdownMatcher.find()) {
            String matchedString = urlMarkdownMatcher.group(2);
            String matchedUrl = urlMarkdownMatcher.group(3);

            String fixedString = "<a href=\"" + matchedUrl + "\">" + matchedString + "</a>";

            output = output.replace(urlMarkdownMatcher.group(1), fixedString);
        }

        // Converts bold markdown to HTML
        Matcher boldMarkdownMatcher = BOLD_MARKDOWN_PATTERN.matcher(output);
        while (boldMarkdownMatcher.find()) {
            String matchedString = boldMarkdownMatcher.group(2);

            String fixedString = "<b>" + matchedString + "</b>";

            output = output.replace(boldMarkdownMatcher.group(1), fixedString);
        }

        return output;
    }

    public String convertSubredditsToLinks(String string) {
        // Converts subreddit mentions to links in HTML
        String output = string;
        Matcher subredditMatcher = SUBREDDIT_PATTERN.matcher(output);
        while (subredditMatcher.find()) {
            String matchedSubreddit = subredditMatcher.group(1);

            String fixedString = "<a href=\"https://www.reddit.com" + matchedSubreddit +
                    "\">" + matchedSubreddit + "</a>";

            output = output.replaceAll(matchedSubreddit + "(?!\\S)", fixedString);
        }

        return output;
    }

}
