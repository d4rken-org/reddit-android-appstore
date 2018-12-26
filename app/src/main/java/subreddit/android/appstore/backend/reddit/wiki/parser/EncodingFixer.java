package subreddit.android.appstore.backend.reddit.wiki.parser;

import android.text.Html;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EncodingFixer {
    private static final Pattern URL_MARKDOWN_PATTERN = Pattern.compile("(\\[(.*?)]\\s?\\((http.*?)\\))");
    private static final Pattern BOLD_MARKDOWN_PATTERN = Pattern.compile("(\\*\\*(.*?)\\*\\*)");
    private static final Pattern SUBREDDIT_PATTERN = Pattern.compile("(/r/.*?(?!\\S))");

    public String fixHtmlEscapes(String input) {
        //noinspection deprecation
        return Html.fromHtml(input).toString();
    }

    public String convertMarkdownToHtml(String input) {
        input = urlMarkdownToHtml(input);
        input = boldMarkdownToHtml(input);

        return input;
    }

    public String convertSubredditsToLinks(String input) {
        // Converts subreddit mentions to links in HTML
        Matcher subredditMatcher = SUBREDDIT_PATTERN.matcher(input);
        while (subredditMatcher.find()) {
            String matchedSubreddit = subredditMatcher.group(1);

            String fixedString = "<a href=\"https://www.reddit.com" + matchedSubreddit +
                    "\">" + matchedSubreddit + "</a>";

            input = input.replaceAll(matchedSubreddit + "(?!\\S)", fixedString);
        }
        return input;
    }

    // Converts string with link markdown to HTML
    private String urlMarkdownToHtml(String string) {
        Matcher urlMarkdownMatcher = URL_MARKDOWN_PATTERN.matcher(string);
        while (urlMarkdownMatcher.find()) {
            String matchedString = urlMarkdownMatcher.group(2);
            String matchedUrl = urlMarkdownMatcher.group(3);

            String fixedString = "<a href=\"" + matchedUrl + "\">" + matchedString + "</a>";

            string = string.replace(urlMarkdownMatcher.group(1), fixedString);
        }
        return string;
    }

    // Converts string with bold markdown to HTML
    private String boldMarkdownToHtml(String string) {
        Matcher boldMarkdownMatcher = BOLD_MARKDOWN_PATTERN.matcher(string);
        while (boldMarkdownMatcher.find()) {
            String matchedString = boldMarkdownMatcher.group(2);

            String fixedString = "<b>" + matchedString + "</b>";

            string = string.replace(boldMarkdownMatcher.group(1), fixedString);
        }
        return string;
    }

}
