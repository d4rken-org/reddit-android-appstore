package subreddit.android.appstore.backend.data;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.number.OrderingComparison.lessThan;
import static org.junit.Assert.assertThat;

/**
 * Created by Kristian 'krissrex' Rekstad on 8/7/2017.
 *
 * @author Kristian 'krissrex' Rekstad
 */
public class AppInfoTest {

    @Test
    public void testCompareSortsIgnoringCase() {
        // Given
        AppInfo lowerCaseNameApp = new AppInfo();
        lowerCaseNameApp.setAppName("apple");

        AppInfo upperCaseNameApp = new AppInfo();
        upperCaseNameApp.setAppName("Zombie");

        // When
        final int difference = lowerCaseNameApp.compareTo(upperCaseNameApp);

        // Then
        assertThat("Sorting places lowercase letters after uppercase", difference, is(lessThan(0)));
    }

}