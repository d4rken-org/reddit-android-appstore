package subreddit.android.appstore.backend.reddit.wiki.parser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import subreddit.android.appstore.backend.data.AppInfo;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

public class DescriptionColumnParserTest {
    @Mock
    EncodingFixer encodingFixer;
    private DescriptionColumnParser parser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        parser = new DescriptionColumnParser(encodingFixer);
    }
    @Test
    public void testParse() throws IOException {
        String rawDescriptionData = "This is a description";
        Map<AppParser.Column, String> rawColumnMap = new HashMap<>();
        rawColumnMap.put(AppParser.Column.DESCRIPTION, rawDescriptionData);
        AppInfo appInfo = new AppInfo();
        parser.parse(appInfo, rawColumnMap);

        assertFalse(appInfo.getDescription().isEmpty());
        assertEquals("This is a description", appInfo.getDescription());
    }

    @Test
    public void testParse_withMarkdown() throws IOException {
        String rawDescriptionData = "";
        Map<AppParser.Column, String> rawColumnMap = new HashMap<>();
        rawColumnMap.put(AppParser.Column.DESCRIPTION, rawDescriptionData);
        AppInfo appInfo = new AppInfo();
        parser.parse(appInfo, rawColumnMap);

        //TODO: Implement markdown parsing in DescriptionColumnParser as well
    }
}
