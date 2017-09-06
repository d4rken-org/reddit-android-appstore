package subreddit.android.appstore.backend.reddit.wiki.parser;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class EncodingFixerTest {
    private String descriptionBoldMarkdown, descriptionLinkMarkdown, descriptionSubreddits;
    private EncodingFixer encodingFixer;

    @Before
    public void setup() {
        encodingFixer = new EncodingFixer();

        descriptionBoldMarkdown = "New ingenious full feature Reddit client. **Read AMA in magazine" +
                "style QA format.** Add upcoming AMA to your calendar. **Only app with Text to " +
                "Speech: Listen to the content.** Zero wait, streams all GIF instantly.";
        descriptionLinkMarkdown = "Tutorial screencast for [Propellerheads Reason](https://www" +
                ".propellerheads.se/products/reason/) and [Link 2](https://www.google.com)";
        descriptionSubreddits = "You can setup playlists, geofences, volume control, and share" +
                " playlists with others. Share playlists at /r/androidrrm and /r/android";
    }

    @Test
    public void testConvertMarkdownToHtml() {
        String output = encodingFixer.convertMarkdownToHtml("");
        assertEquals("", output);

        output = encodingFixer.convertMarkdownToHtml("This description has no markdown");
        assertEquals("This description has no markdown", output);
    }

    @Test
    public void testConvertMarkdownToHtml_bold() {
        String output = encodingFixer.convertMarkdownToHtml(descriptionBoldMarkdown);
        assertEquals("New ingenious full feature Reddit client. <b>Read AMA in magazine" +
                "style QA format.</b> Add upcoming AMA to your calendar. <b>Only app with Text to " +
                "Speech: Listen to the content.</b> Zero wait, streams all GIF instantly.", output);
    }

    @Test
    public void testConvertMarkdownToHtml_link() {
        String output = encodingFixer.convertMarkdownToHtml(descriptionLinkMarkdown);
        assertEquals("Tutorial screencast for " +
                "<a href=\"https://www.propellerheads.se/products/reason/\">" +
                "Propellerheads Reason</a> and <a href=\"https://www.google.com\">Link 2</a>", output);
    }

    @Test
    public void testConvertSubredditsToLinks() {
        String output = encodingFixer.convertSubredditsToLinks(descriptionSubreddits);
        assertEquals("You can setup playlists, geofences, volume control, and share " +
                "playlists with others. Share playlists at " +
                "<a href=\"https://www.reddit.com/r/androidrrm\">/r/androidrrm</a> " +
                "and <a href=\"https://www.reddit.com/r/android\">/r/android</a>", output);
    }
}
