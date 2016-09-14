package subreddit.android.appstore.backend.wiki.parser;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import subreddit.android.appstore.backend.data.AppInfo;
import subreddit.android.appstore.backend.data.Contact;
import timber.log.Timber;


public class ContactColumnParser extends BaseParser {
    final static Pattern EMAIL_PATTERN = Pattern.compile("(\\b[\\w._%+-]+@[\\w.-]+\\.[\\w]{2,}\\b)");
    final static Pattern REDDIT_PATTERN = Pattern.compile("(/u/[\\-_\\w]+\\b)");
    final static Pattern WEBSITE_MATCHER = Pattern.compile("(?:\\[.+\\])\\((http.?://.+)\\)");

    public ContactColumnParser(EncodingFixer encodingFixer) {
        super(encodingFixer);
    }

    @Override
    public void parse(AppInfo appInfo, Map<Column, String> rawColumns) {
        final String raw = rawColumns.get(Column.CONTACT);
        Matcher emailMatcher = EMAIL_PATTERN.matcher(raw);
        while (emailMatcher.find()) {
            Contact contact = new Contact(Contact.Type.EMAIL, emailMatcher.group(1));
            appInfo.getContacts().add(contact);
        }
        Matcher redditMatcher = REDDIT_PATTERN.matcher(raw);
        while (redditMatcher.find()) {
            Contact contact = new Contact(Contact.Type.REDDIT_USERNAME, redditMatcher.group(1));
            appInfo.getContacts().add(contact);
        }
        Matcher webSiteMatcher = WEBSITE_MATCHER.matcher(raw);
        while (webSiteMatcher.find()) {
            Contact contact = new Contact(Contact.Type.WEBSITE, webSiteMatcher.group(1));
            appInfo.getContacts().add(contact);
        }
        if (appInfo.getContacts().isEmpty()) {
            Timber.w("No contacts parsed from %s", raw);
            appInfo.getContacts().add(new Contact(Contact.Type.UNKNOWN, raw));
        }
    }
}
