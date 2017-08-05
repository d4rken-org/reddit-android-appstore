package subreddit.android.appstore.backend.reddit.wiki.parser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

public class PriceColumnParserTest {
    @Mock
    EncodingFixer encodingFixer;
    private PriceColumnParser parser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        parser = new PriceColumnParser(encodingFixer);
    }

    @Test
    public void testParse() throws IOException {

    }


}
