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
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class BodyParserTest {
    @Mock EncodingFixer encodingFixer;
    private BodyParser bodyParser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(encodingFixer.fixHtmlEscapes(anyString())).then(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArgument(0);
            }
        });

        //TODO: Mock out
        Set<AppParser> appParsers = new HashSet<>();
        appParsers.add(new NameColumnParser(encodingFixer));
        appParsers.add(new PriceColumnParser(encodingFixer));
        appParsers.add(new DeviceColumnParser(encodingFixer));
        appParsers.add(new DescriptionColumnParser(encodingFixer));
        appParsers.add(new ContactColumnParser(encodingFixer));
        CategoryParser categoryParser = new CategoryParser(encodingFixer);

        bodyParser = new BodyParser(categoryParser, appParsers);
    }

    @Test
    public void testBodyParser() throws IOException {
        Collection<AppInfo> appInfos = bodyParser.parseBody(TestBody.HTMLBODY);
        assertFalse(appInfos.isEmpty());
        assertEquals(41, appInfos.size());
    }
}
