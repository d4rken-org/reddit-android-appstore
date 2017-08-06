package subreddit.android.appstore.backend.reddit.wiki.parser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import subreddit.android.appstore.backend.data.AppInfo;
import subreddit.android.appstore.backend.data.Download;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class NameColumnParserTest {
    @Mock EncodingFixer encodingFixer;
    private NameColumnParser parser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(encodingFixer.fixHtmlEscapes(anyString())).then(new Answer<String>() {
            public String answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArgument(0);
            }
        });
        parser = new NameColumnParser(encodingFixer);
    }
    @Test
    public void testParse_names() throws IOException {
        String rawNameData = "[10000000](https://play.google.com/store/apps/details?id=com.eightyeightgames.tenmillion)";
        Map<AppParser.Column, String> rawColumnMap = new HashMap<>();
        rawColumnMap.put(AppParser.Column.NAME, rawNameData);
        AppInfo appInfo = new AppInfo();
        parser.parse(appInfo, rawColumnMap);
        assertEquals("10000000", appInfo.getAppName());
    }

    @Test
    public void testParse_downloadTypes() throws IOException {
        //Google Play link
        String rawPlayDownload = "[10000000](https://play.google.com/store/apps/details?id=com.eightyeightgames.tenmillion)";
        Map<AppParser.Column, String> rawColumnMap = new HashMap<>();
        rawColumnMap.put(AppParser.Column.NAME, rawPlayDownload);
        AppInfo appInfo = new AppInfo();
        parser.parse(appInfo, rawColumnMap);
        ArrayList<Download> downloads = (ArrayList<Download>) appInfo.getDownloads();
        assertEquals(Download.Type.GPLAY, downloads.get(0).getType());
        assertEquals("https://play.google.com/store/apps/details?id=com.eightyeightgames.tenmillion",
                downloads.get(0).getTarget());

        //Unknown/parsing failed link
        String rawUnknownDownload = "What is this download?";
        rawColumnMap = new HashMap<>();
        rawColumnMap.put(AppParser.Column.NAME, rawUnknownDownload);
        AppInfo appInfo0 = new AppInfo();
        parser.parse(appInfo0, rawColumnMap);
        downloads = (ArrayList<Download>) appInfo0.getDownloads();
        assertEquals(Download.Type.UNKNOWN, downloads.get(0).getType());
        assertEquals("What is this download?",
                downloads.get(0).getTarget());
        assertEquals("What is this download?", appInfo0.getAppName());

        //Amazon link
        String rawAmazonDownload = "[Lost Within](http://www.amazon.com/Amazon-Game-Studios-Lost-Within/dp/B00VGKMTKC/)";
        rawColumnMap = new HashMap<>();
        rawColumnMap.put(AppParser.Column.NAME, rawAmazonDownload);
        AppInfo appInfo1 = new AppInfo();
        parser.parse(appInfo1, rawColumnMap);
        downloads = (ArrayList<Download>) appInfo0.getDownloads();
        //TODO: this, along with other download types in NameColumnParser
    }
}
