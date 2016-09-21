package subreddit.android.appstore.util;

public class VersionHelper {

    // http://stackoverflow.com/a/6702029/1251958

    /**
     * Compares two version strings.
     * <p>
     * Use this instead of String.compareTo() for a non-lexicographical
     * comparison that works for version strings. e.g. "1.10".compareTo("1.6").
     * <p>
     *
     * @param current    a string of ordinal numbers separated by decimal points.
     * @param newVersion a string of ordinal numbers separated by decimal points.
     * @return The result is a negative integer if str1 is _numerically_ less than str2.
     * The result is a positive integer if str1 is _numerically_ greater than str2.
     * The result is zero if the strings are _numerically_ equal.
     * @note It does not work if "1.10" is supposed to be equal to "1.10.0".
     */
    public static int versionCompare(String current, String newVersion) {
        if (current.startsWith("v")) current = current.substring(1);
        if (current.contains("-")) current = current.substring(0, current.indexOf("-"));
        if (newVersion.startsWith("v")) newVersion = newVersion.substring(1);
        if (newVersion.contains("-")) newVersion = newVersion.substring(0, newVersion.indexOf("-"));
        String[] vals1 = current.split("\\.");
        String[] vals2 = newVersion.split("\\.");
        int i = 0;
        // set index to first non-equal ordinal or length of shortest version string
        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
            i++;
        }
        // compare first non-equal ordinal number
        if (i < vals1.length && i < vals2.length) {
            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
            return Integer.signum(diff);
        }
        // the strings are equal or one string is a substring of the other
        // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
        return Integer.signum(vals1.length - vals2.length);
    }


}
