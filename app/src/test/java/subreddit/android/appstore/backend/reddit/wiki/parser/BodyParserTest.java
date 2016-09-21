package subreddit.android.appstore.backend.reddit.wiki.parser;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Collection;

import subreddit.android.appstore.backend.data.AppInfo;

import static junit.framework.Assert.assertFalse;

public class BodyParserTest {
    @Mock EncodingFixer encodingFixer;
    private BodyParser bodyParser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        bodyParser = new BodyParser(encodingFixer);
    }

    @Test
    public void testBodyParser() throws IOException {
        Collection<AppInfo> appInfos = bodyParser.parseBody(TestBody.HTMLBODY);
        assertFalse(appInfos.isEmpty());
    }
}
