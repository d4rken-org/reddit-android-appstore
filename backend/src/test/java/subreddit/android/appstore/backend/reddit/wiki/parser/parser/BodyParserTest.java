package subreddit.android.appstore.backend.reddit.wiki.parser.parser;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.Collection;

import subreddit.android.appstore.backend.data.AppInfo;
import subreddit.android.appstore.backend.reddit.wiki.parser.BodyParser;
import subreddit.android.appstore.backend.reddit.wiki.parser.EncodingFixer;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class BodyParserTest {
    @Mock
    EncodingFixer encodingFixer;
    private BodyParser bodyParser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(encodingFixer.fixHtmlEscapes(ArgumentMatchers.anyString())).then(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArgument(0);
            }
        });
        bodyParser = new BodyParser(encodingFixer);
    }

    @Test
    public void testBodyParser() throws IOException {
        Collection<AppInfo> appInfos = bodyParser.parseBody(TestBody.HTMLBODY);
        assertFalse(appInfos.isEmpty());
        assertEquals(41, appInfos.size());
    }
}
