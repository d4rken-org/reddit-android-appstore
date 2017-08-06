package subreddit.android.appstore.backend.reddit.wiki.parser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import subreddit.android.appstore.backend.data.AppInfo;
import subreddit.android.appstore.backend.data.AppTags;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class PriceColumnParserTest {
    @Mock EncodingFixer encodingFixer;
    private PriceColumnParser parser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        parser = new PriceColumnParser(encodingFixer);
    }

    @Test
    public void testParse() throws IOException {
        String rawPriceData = "Paid";
        Map<AppParser.Column, String> rawColumnMap = new HashMap<>();
        rawColumnMap.put(AppParser.Column.PRICE, rawPriceData);
        AppInfo appInfo = new AppInfo();
        parser.parse(appInfo, rawColumnMap);
        assertTrue(appInfo.getTags().contains(AppTags.PAID));
        assertFalse(appInfo.getTags().contains(AppTags.FREE));

        rawPriceData = "Free";
        rawColumnMap = new HashMap<>();
        rawColumnMap.put(AppParser.Column.PRICE, rawPriceData);
        AppInfo appInfo1 = new AppInfo();
        parser.parse(appInfo, rawColumnMap);
        assertTrue(appInfo.getTags().contains(AppTags.FREE));
        assertFalse(appInfo.getTags().contains(AppTags.PAID));

    }


}
