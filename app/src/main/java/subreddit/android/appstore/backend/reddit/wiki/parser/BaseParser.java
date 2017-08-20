package subreddit.android.appstore.backend.reddit.wiki.parser;

public abstract class BaseParser implements AppParser {
    final EncodingFixer encodingFixer;

    public BaseParser(EncodingFixer encodingFixer) {
        this.encodingFixer = encodingFixer;
    }

    String fixEncoding(String input) {
        return encodingFixer.fixHtmlEscapes(input);
    }

    String fixMarkdown(String input) {
        return encodingFixer.convertStringToHtml(input);
    }

    String convertSubredditsToLinks(String input) {
        return encodingFixer.convertSubredditsToLinks(input);
    }
}
