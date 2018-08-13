package subreddit.android.appstore.backend.reddit.wiki.parser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import subreddit.android.appstore.backend.data.AppInfo;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doAnswer;

public class BodyParserTest {
    @Mock
    private AppParser appParser;
    @Mock
    private CategoryParser categoryParser;
    private BodyParser bodyParser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        doAnswer((Answer<Void>) invocation -> null).when(categoryParser).parse(isA(AppInfo.class), anyList());
        doAnswer((Answer<Void>) invocation -> null).when(appParser).parse(isA(AppInfo.class), anyMap());

        Set<AppParser> appParsers = new HashSet<>();
        for (int i=0; i<5; i++) {
            appParsers.add(appParser);
        }

        bodyParser = new BodyParser(categoryParser, appParsers);
    }

    @Test
    public void testBodyParser() {
        Collection<AppInfo> appInfos = bodyParser.parseBody(TestBody.HTMLBODY);
        assertFalse(appInfos.isEmpty());
        assertEquals(41, appInfos.size());
    }
}
