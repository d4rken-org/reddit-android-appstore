package subreddit.android.appstore.backend;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import subreddit.android.appstore.AppStoreApp;
import timber.log.Timber;

public class BodyParser {
    static final String TAG = AppStoreApp.LOGPREFIX + "BodyParser";
    static final Pattern PRIMARY_CATEGORY_PATTERN = Pattern.compile("^(?:#(?!#+)\\s?)(.+?)$");

    Collection<AppInfo> parseBody(ResponseBody responseBody) throws IOException {
        return parseBody(responseBody.string());
    }

    Collection<AppInfo> parseBody(String bodyString) {
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
            parseNameField(app, split[0].trim());
            parsePriceField(app, split[1].trim());
            parseFormatField(app, split[2].trim());
            parseDescriptionField(app, split[3].trim());
            parseContactField(app, split[4].trim());

            if (primaryCategory.equals("Apps")) app.getTags().add(AppInfo.Tag.APP);
            else if (primaryCategory.equals("Games")) app.getTags().add(AppInfo.Tag.GAME);

            // TODO turn secondaryCategory into tags

            appInfos.add(app);
        }
        return appInfos;
    }

    static final Pattern NAME_PATTERN = Pattern.compile("^(?:\\[(.+)\\]\\((.+)\\))$");

    void parseNameField(AppInfo appInfo, String rawNameString) {
        Matcher matcher = NAME_PATTERN.matcher(rawNameString);
        if (matcher.matches()) {
            appInfo.appName = matcher.group(1);
            try {
                appInfo.targetLink = new URL(matcher.group(2));
            } catch (MalformedURLException e) {
                Timber.tag(TAG).e(e, "Can't parse target link: %s", rawNameString);
            }
        } else {
            appInfo.appName = rawNameString;
            Timber.tag(TAG).w("parseNameField(%s) failed", rawNameString);
        }
    }

    void parsePriceField(AppInfo appInfo, String rawPriceString) {
        // TODO
        if (rawPriceString.toLowerCase().contains("free")) appInfo.getTags().add(AppInfo.Tag.FREE);
        if (rawPriceString.toLowerCase().contains("paid")) appInfo.getTags().add(AppInfo.Tag.PAID);
        if (rawPriceString.toLowerCase().contains("iap")) appInfo.getTags().add(AppInfo.Tag.IAP);
    }

    void parseFormatField(AppInfo appInfo, String rawFormatString) {
        // TODO
        if (rawFormatString.toLowerCase().contains("wear")) appInfo.getTags().add(AppInfo.Tag.WEAR);
        if (rawFormatString.toLowerCase().contains("phone")) appInfo.getTags().add(AppInfo.Tag.PHONE);
        if (rawFormatString.toLowerCase().contains("tablet")) appInfo.getTags().add(AppInfo.Tag.TABLET);
        if (rawFormatString.toLowerCase().contains("both")) {
            appInfo.getTags().add(AppInfo.Tag.PHONE);
            appInfo.getTags().add(AppInfo.Tag.TABLET);
        }
    }

    void parseDescriptionField(AppInfo appInfo, String rawDescriptionString) {
        // TODO
        appInfo.description = rawDescriptionString;
    }

    void parseContactField(AppInfo appInfo, String rawContactString) {
        // TODO
    }
}
