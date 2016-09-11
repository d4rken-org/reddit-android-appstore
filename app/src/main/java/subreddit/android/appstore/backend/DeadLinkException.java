package subreddit.android.appstore.backend;

import java.util.Locale;

public class DeadLinkException extends Exception {
    public DeadLinkException(String url) {
        super(String.format(Locale.US, "Link 404ed: %s", url));
    }
}
