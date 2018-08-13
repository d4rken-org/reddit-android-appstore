package subreddit.android.appstore.backend.reddit.wiki.parser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import subreddit.android.appstore.backend.data.AppInfo;
import subreddit.android.appstore.backend.data.Contact;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertFalse;

public class ContactColumnParserTest {
    @Mock
    private EncodingFixer encodingFixer;
    private ContactColumnParser parser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        parser = new ContactColumnParser(encodingFixer);
    }

    @Test
    public void testParse() {
        String rawContactData = "/u/someuser1 thisismail1@gmail.com " +
                "Twitter: [@twitteraccount](https://twitter.com/twitteraccount) " +
                "Instagram: [@instagramaccount](http://instagram.com/instagramaccount) " +
                "okanothermail@internet.com /u/mDarken";
        Map<AppParser.Column, String> rawColumnMap = new HashMap<>();
        rawColumnMap.put(AppParser.Column.CONTACT, rawContactData);
        AppInfo appInfo = new AppInfo();
        parser.parse(appInfo, rawColumnMap);
        assertFalse(appInfo.getContacts().isEmpty());

        ArrayList<Contact> emailContacts = new ArrayList<>();
        for (Contact contact : appInfo.getContacts()) {
            if (contact.getType() == Contact.Type.EMAIL) emailContacts.add(contact);
        }
        assertEquals("thisismail1@gmail.com", emailContacts.get(0).getTarget());
        assertEquals("okanothermail@internet.com", emailContacts.get(1).getTarget());

        ArrayList<Contact> redditContacts = new ArrayList<>();
        for (Contact contact : appInfo.getContacts()) {
            if (contact.getType() == Contact.Type.REDDIT_USERNAME) redditContacts.add(contact);
        }
        assertEquals("/u/someuser1", redditContacts.get(0).getTarget());
        assertEquals("/u/mDarken", redditContacts.get(1).getTarget());
    }
}
