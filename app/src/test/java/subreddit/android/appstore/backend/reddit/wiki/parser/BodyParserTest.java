package subreddit.android.appstore.backend.reddit.wiki.parser;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
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
    @Mock AppParser appParser;
    @Mock CategoryParser categoryParser;
    private BodyParser bodyParser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(categoryParser).parse(isA(AppInfo.class), anyList());
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(appParser).parse(isA(AppInfo.class), anyMap());

        Set<AppParser> appParsers = new HashSet<>();
        for (int i=0; i<5; i++) {
            appParsers.add(appParser);
        }

        bodyParser = new BodyParser(categoryParser, appParsers);
    }

    @Test
    public void testBodyParser() throws IOException {
        Collection<AppInfo> appInfos = bodyParser.parseBody(TestBody.HTMLBODY);
        assertFalse(appInfos.isEmpty());
        assertEquals(41, appInfos.size());
    }
}
