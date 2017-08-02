package subreddit.android.appstore.backend.reddit.wiki.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class ParserSuite implements Iterable<AppParser> {
    private CategoryParser categoryParser;
    private List<AppParser> appParsers;

    public ParserSuite(EncodingFixer encodingFixer) {
        categoryParser = new CategoryParser(encodingFixer);
        appParsers = new ArrayList<AppParser>();
        appParsers.add(new NameColumnParser(encodingFixer));
        appParsers.add(new PriceColumnParser(encodingFixer));
        appParsers.add(new DeviceColumnParser(encodingFixer));
        appParsers.add(new DescriptionColumnParser(encodingFixer));
        appParsers.add(new ContactColumnParser(encodingFixer));
    }

    public CategoryParser getCategoryParser() {
        return categoryParser;
    }

    @Override
    public Iterator<AppParser> iterator() {
        return new AppParserIterator<AppParser>();
    }

    private class AppParserIterator<T> implements Iterator<AppParser> {
        private int i = 0;

        @Override
        public boolean hasNext() {
            return i < appParsers.size();
        }

        @Override
        public AppParser next() {
            if (!(hasNext())) {
                throw new NoSuchElementException();
            }
            AppParser parser = appParsers.get(i);
            i++;

            return parser;
        }
    }
}
