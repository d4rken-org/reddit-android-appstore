package subreddit.android.appstore.backend.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import subreddit.android.appstore.AppStoreApp;
import subreddit.android.appstore.backend.data.AppInfo;

public class BodyParser {
    private static final String TAG = AppStoreApp.LOGPREFIX + "BodyParser";
    private static final Pattern PRIMARY_CATEGORY_PATTERN = Pattern.compile("^(?:#(?!#+)\\s?)(.+?)$");
    private final List<AppParser> appParsers = new ArrayList<>();

    public BodyParser() {
        appParsers.add(new CategoryParser());
        appParsers.add(new NameColumnParser());
        appParsers.add(new PriceColumnParser());
        appParsers.add(new DeviceColumnParser());
        appParsers.add(new DescriptionColumnParser());
        appParsers.add(new ContactColumnParser());
    }

    public Collection<AppInfo> parseBody(ResponseBody responseBody) throws IOException {
        return parseBody(responseBody.string());
    }

    public Collection<AppInfo> parseBody(String bodyString) {
        Collection<AppInfo> parsedOutput = new ArrayList<>();
        int start = bodyString.indexOf("<textarea readonly class=\"source\"");
        int stop = bodyString.lastIndexOf("</textarea>");
        List<String> lines = Arrays.asList(bodyString.substring(start, stop).split("\n"));

        int lastPrimaryBlock = -1;
        String lastPrimaryCategory = null;
        for (int linePos = 0; linePos < lines.size(); linePos++) {
            String line = lines.get(linePos);
            Matcher matcher = PRIMARY_CATEGORY_PATTERN.matcher(line.trim());
            if (matcher.matches()) {
                String newPrimaryCategory = matcher.group(1).trim();
                if (lastPrimaryCategory == null) {
                    // First block
                    lastPrimaryCategory = newPrimaryCategory;
                    lastPrimaryBlock = lines.indexOf(line);
                } else if (!lastPrimaryCategory.equals(newPrimaryCategory)) {
                    // New block, we now know how long this block is
                    Collection<AppInfo> parsedBlock = parsePrimaryBlock(
                            lastPrimaryCategory,
                            lines.subList(lastPrimaryBlock, linePos - 1)
                    );
                    parsedOutput.addAll(parsedBlock);

                    lastPrimaryCategory = newPrimaryCategory;
                    lastPrimaryBlock = lines.indexOf(line);
                }
            }
        }
        if (lastPrimaryBlock != -1) {
            // End of output, is obviously also the end of the last block
            Collection<AppInfo> parsedBlock = parsePrimaryBlock(
                    lastPrimaryCategory,
                    lines.subList(lastPrimaryBlock, lines.size())
            );
            parsedOutput.addAll(parsedBlock);
        }
        return parsedOutput;
    }

    static final Pattern SECONDARY_CATEGORY_PATTERN = Pattern.compile("^^(?:##(?!#+)\\s?)(.+?)$");

    // i.e. "# Games" or "# Apps" blocks
    Collection<AppInfo> parsePrimaryBlock(String primaryCategory, List<String> primaryBlock) {
        Collection<AppInfo> parsedOutput = new ArrayList<>();
        int lastSecondaryBlock = -1;
        String lastSecondaryCategory = null;
        for (int linePos = 0; linePos < primaryBlock.size(); linePos++) {
            String line = primaryBlock.get(linePos);
            Matcher matcher = SECONDARY_CATEGORY_PATTERN.matcher(line.trim());
            if (matcher.matches()) {
                String newSecondaryCategory = matcher.group(1).trim();
                if (lastSecondaryCategory == null) {
                    // First block
                    lastSecondaryCategory = newSecondaryCategory;
                    lastSecondaryBlock = primaryBlock.indexOf(line);
                } else if (!lastSecondaryCategory.equals(newSecondaryCategory)) {
                    // New block, we now know how long this block is
                    Collection<AppInfo> parsedBlock = parseSecondaryBlock(
                            primaryCategory,
                            lastSecondaryCategory,
                            primaryBlock.subList(lastSecondaryBlock, linePos - 1)
                    );
                    parsedOutput.addAll(parsedBlock);

                    lastSecondaryCategory = newSecondaryCategory;
                    lastSecondaryBlock = primaryBlock.indexOf(line);
                }
            }
        }
        return parsedOutput;
    }

    static final Pattern COLUMN_LINE_PATTERN = Pattern.compile("^(?:\\:-+\\|){5}$");

    // i.e. "## Action" blocks
    Collection<AppInfo> parseSecondaryBlock(String primaryCategory, String secondaryCategory, List<String> secondaryBlock) {
        Collection<AppInfo> appInfos = new ArrayList<>();
        int cleanStart = 0;
        // Skip table headers.
        for (int i = 0; i < secondaryBlock.size(); i++) {
            cleanStart = i;
            String line = secondaryBlock.get(i).trim();
            if (COLUMN_LINE_PATTERN.matcher(line).matches()) break;
        }

        // One more so we skip what we matched (the table headers)
        cleanStart++;

        // Empty block?
        if (cleanStart == secondaryBlock.size()) return appInfos;

        // Now these should be the apps followed by a few empty lines.
        for (int i = cleanStart; i < secondaryBlock.size(); i++) {
            String line = secondaryBlock.get(i);
            String[] split = line.split("\\|");
            if (split.length != 5) continue;

            AppInfo app = new AppInfo();
            Map<AppParser.Column, String> columnMap = new HashMap<>();
            columnMap.put(AppParser.Column.PRIMARY_CATEGORY, primaryCategory);
            columnMap.put(AppParser.Column.SECONDARY_CATEGORY, secondaryCategory);
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
