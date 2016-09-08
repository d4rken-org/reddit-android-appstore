package subreddit.android.appstore.screens.list;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.runner.RunWith;

import subreddit.android.appstore.screens.MainActivity;


@RunWith(AndroidJUnit4.class)
public class AppListTest {
    @Rule
    public ActivityTestRule<MainActivity> testRule = new ActivityTestRule<>(MainActivity.class);

}
