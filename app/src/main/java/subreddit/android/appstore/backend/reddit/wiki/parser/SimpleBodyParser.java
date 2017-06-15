package subreddit.android.appstore.backend.reddit.wiki.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import subreddit.android.appstore.backend.data.AppInfo;

public class SimpleBodyParser {
    private final List<AppParser> appParsers = new ArrayList<>();
    private final CategoryParser categoryParser;

    public SimpleBodyParser(EncodingFixer encodingFixer) {
        categoryParser = new CategoryParser(encodingFixer);
        appParsers.add(new NameColumnParser(encodingFixer));
    }

    public Collection<AppInfo> parseBody(String bodyString) {
        Collection<AppInfo> parsedOutput = new ArrayList<>();
        List<String> lines = Arrays.asList(bodyString.split("\n"));

        parsedOutput.addAll(parseCategoryBlocks(new ArrayList<>(), lines));

        return parsedOutput;
    }

    private static final Pattern CATEGORY_PATTERN = Pattern.compile("^(#+)(?:\\s*+)(.+?)$");

    Collection<AppInfo> parseCategoryBlocks(List<String> categories, List<String> block) {
        Collection<AppInfo> parsedOutput = new ArrayList<>();
        for (int linePos = 0; linePos < block.size(); linePos++) {
            Matcher matcher = CATEGORY_PATTERN.matcher(block.get(linePos).trim());
            if (matcher.matches()) {

            } else if (linePos == block.size() - 1) {
                parsedOutput.addAll(parseAppBlock(categories, block));
            }
        }
        return parsedOutput;
    }

    private static final Pattern COLUMN_LINE_PATTERN = Pattern.compile("^(?:\\:-+\\|){5}$");

    // i.e. "## Action" blocks
    private Collection<AppInfo> parseAppBlock(List<String> categories, List<String> block) {
        Collection<AppInfo> appInfos = new ArrayList<>();
        int cleanStart = 0;
        // Skip table headers.
        for (int i = 0; i < block.size(); i++) {
            cleanStart = i;
            String line = block.get(i).trim();
            if (COLUMN_LINE_PATTERN.matcher(line).matches()) break;
        }

        // One more so we skip what we matched (the table headers)
        cleanStart++;

        // Empty block?
        if (cleanStart == block.size()) return appInfos;

        // Now these should be the apps followed by a few empty lines.
        for (int i = cleanStart; i < block.size(); i++) {
            String line = block.get(i);
            String[] split = line.split("\\|");
            if (split.length != 5) continue;

            AppInfo app = new AppInfo();
            categoryParser.parse(app, categories);
            Map<AppParser.Column, String> columnMap = new HashMap<>();
            columnMap.put(AppParser.Column.NAME, split[0].trim());

            for (AppParser parser : appParsers) parser.parse(app, columnMap);
            appInfos.add(app);
        }
        return appInfos;
    }

}
