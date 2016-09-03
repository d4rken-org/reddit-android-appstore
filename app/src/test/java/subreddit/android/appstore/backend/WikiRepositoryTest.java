package subreddit.android.appstore.backend;


import org.junit.Test;

import java.io.IOException;
import java.util.Collection;

import static junit.framework.Assert.assertFalse;

public class WikiRepositoryTest {

    @Test
    public void testBodyParser() throws IOException {
        BodyParser bodyParser = new BodyParser();
        Collection<AppInfo> appInfos = bodyParser.parseBody(TestBody.HTMLBODY);
        assertFalse(appInfos.isEmpty());
    }
}
