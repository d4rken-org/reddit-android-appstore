package subreddit.android.appstore.backend.wiki.parser;

public abstract class BaseParser implements AppParser {
    final EncodingFixer encodingFixer;

    public BaseParser(EncodingFixer encodingFixer) {
        this.encodingFixer = encodingFixer;
    }

    String fixEncoding(String input) {
        return encodingFixer.fixHtmlEscapes(input);
    }
}
