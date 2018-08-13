package subreddit.android.appstore.backend.reddit.wiki.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import subreddit.android.appstore.backend.data.AppInfo;

public class BodyParser {
    Set<AppParser> appParsers;
    CategoryParser categoryParser;

    @Inject
    public BodyParser(CategoryParser categoryParser, Set<AppParser> appParsers) {
        this.appParsers = appParsers;
        this.categoryParser = categoryParser;
    }

    public Collection<AppInfo> parseBody(String bodyString) {
        List<String> lines = Arrays.asList(bodyString.split("\n"));

        Collection<AppInfo> parsedOutput = new ArrayList<>(parseCategoryBlocks(new ArrayList<>(), lines));

        return parsedOutput;
    }

    private static final Pattern CATEGORY_PATTERN = Pattern.compile("^(#+)(?:\\s*+)(.+?)$");

    Collection<AppInfo> parseCategoryBlocks(List<String> categories, List<String> block) {
        Collection<AppInfo> parsedOutput = new ArrayList<>();
        String currentCategory = null;
        int currentLevel = 0;
        int currentBlockStart = -1;
        for (int linePos = 0; linePos < block.size(); linePos++) {
            Matcher matcher = CATEGORY_PATTERN.matcher(block.get(linePos).trim());
            if (matcher.matches()) {
                int foundLevel = matcher.group(1).trim().length();
                String foundCategory = matcher.group(2).trim();
                if (currentCategory == null) {
                    currentCategory = foundCategory;
                    currentLevel = foundLevel;
                    currentBlockStart = linePos + 1;
                } else if (currentLevel == foundLevel) {
                    List<String> currentCategories = new ArrayList<>(categories);
                    currentCategories.add(currentCategory);
                    parsedOutput.addAll(parseCategoryBlocks(currentCategories, block.subList(currentBlockStart, linePos)));

                    currentCategory = foundCategory;
                    currentBlockStart = linePos + 1;
                }
            } else if (linePos == block.size() - 1) {
                if (currentCategory != null) {
                    List<String> newCategories = new ArrayList<>(categories);
                    newCategories.add(currentCategory);
                    parsedOutput.addAll(parseCategoryBlocks(newCategories, block.subList(currentBlockStart, block.size())));
                } else {
                    parsedOutput.addAll(parseAppBlock(categories, block));
                }
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
            columnMap.put(AppParser.Column.PRICE, split[1].trim());
            columnMap.put(AppParser.Column.DEVICE, split[2].trim());
            columnMap.put(AppParser.Column.DESCRIPTION, split[3].trim());
            columnMap.put(AppParser.Column.CONTACT, split[4].trim());

            for (AppParser parser : appParsers) parser.parse(app, columnMap);
            appInfos.add(app);
        }
        return appInfos;
    }
}