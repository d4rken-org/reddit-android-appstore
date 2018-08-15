package subreddit.android.appstore.backend.reddit.wiki.parser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import subreddit.android.appstore.backend.data.AppInfo;
import subreddit.android.appstore.backend.data.AppTags;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class DeviceColumnParserTest {
    @Mock EncodingFixer encodingFixer;
    private DeviceColumnParser parser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        parser = new DeviceColumnParser(encodingFixer);
    }

    @Test
    public void testParse() {
        String rawDeviceData = "Phone";
        Map<AppParser.Column, String> rawColumnMap = new HashMap<>();
        rawColumnMap.put(AppParser.Column.DEVICE, rawDeviceData);
        AppInfo appInfo = new AppInfo();
        parser.parse(appInfo, rawColumnMap);
        assertEquals(1, appInfo.getTags().size());
        assertTrue(appInfo.getTags().contains(AppTags.PHONE));

        rawDeviceData = "Phone+Tablet";
        rawColumnMap = new HashMap<>();
        rawColumnMap.put(AppParser.Column.DEVICE, rawDeviceData);
        appInfo = new AppInfo();
        parser.parse(appInfo, rawColumnMap);
        assertEquals(2, appInfo.getTags().size());
        assertTrue(appInfo.getTags().contains(AppTags.PHONE));
        assertTrue(appInfo.getTags().contains(AppTags.TABLET));

        rawDeviceData = "Phone+Tablet+Watch";
        rawColumnMap = new HashMap<>();
        rawColumnMap.put(AppParser.Column.DEVICE, rawDeviceData);
        appInfo = new AppInfo();
        parser.parse(appInfo, rawColumnMap);
        assertEquals(3, appInfo.getTags().size());
        assertTrue(appInfo.getTags().contains(AppTags.PHONE));
        assertTrue(appInfo.getTags().contains(AppTags.TABLET));
        assertTrue(appInfo.getTags().contains(AppTags.WEAR));
    }
}
