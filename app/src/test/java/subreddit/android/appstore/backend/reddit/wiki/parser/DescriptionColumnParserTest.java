package subreddit.android.appstore.backend.reddit.wiki.parser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import subreddit.android.appstore.backend.data.AppInfo;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class DescriptionColumnParserTest {
    @Mock
    EncodingFixer encodingFixer;
    private DescriptionColumnParser parser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(encodingFixer.fixHtmlEscapes(anyString())).then(new Answer<String>() {
            public String answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArgument(0);
            }
        });
        when(encodingFixer.convertStringToHtml(anyString())).then(new Answer<String>() {
            public String answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArgument(0);
            }
        });
        when(encodingFixer.convertSubredditsToLinks(anyString())).then(new Answer<String>() {
            public String answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArgument(0);
            }
        });
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
}
