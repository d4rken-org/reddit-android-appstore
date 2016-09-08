package subreddit.android.appstore.backend.parser;


import org.junit.Test;

import java.io.IOException;
import java.util.Collection;

import subreddit.android.appstore.backend.data.AppInfo;

import static junit.framework.Assert.assertFalse;

public class BodyParserTest {

    @Test
    public void testBodyParser() throws IOException {
        BodyParser bodyParser = new BodyParser();
        Collection<AppInfo> appInfos = bodyParser.parseBody(TestBody.HTMLBODY);
        assertFalse(appInfos.isEmpty());
    }
}
