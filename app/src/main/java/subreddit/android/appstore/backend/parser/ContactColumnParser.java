package subreddit.android.appstore.backend.parser;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import subreddit.android.appstore.AppStoreApp;
import subreddit.android.appstore.backend.data.AppInfo;
import subreddit.android.appstore.backend.data.Contact;


public class ContactColumnParser implements AppParser {
    static final String TAG = AppStoreApp.LOGPREFIX + "ContactColumnParser";
    final static Pattern EMAIL_PATTERN = Pattern.compile("(\\b[\\w._%+-]+@[\\w.-]+\\.[\\w]{2,}\\b)");
    final static Pattern REDDIT_PATTERN = Pattern.compile("(/u/[\\w]+?\\b)");

    @Override
    public void parse(AppInfo appInfo, Map<Column, String> rawColumns) {
        final String raw = rawColumns.get(Column.CONTACT);
        Matcher emailMatcher = EMAIL_PATTERN.matcher(raw);
        while (emailMatcher.find()) {
            Contact contact = new Contact(Contact.Type.MAIL, emailMatcher.group(1));
            appInfo.getContacts().add(contact);
        }
        Matcher redditMatcher = REDDIT_PATTERN.matcher(raw);
        while (redditMatcher.find()) {
            Contact contact = new Contact(Contact.Type.REDDIT, redditMatcher.group(1));
            appInfo.getContacts().add(contact);
        }
        // TODO other contact methods
    }
}
