package subreddit.android.appstore.backend.data;

import android.support.annotation.NonNull;

public class Contact {

    public enum Type {
        EMAIL, WEBSITE, REDDIT_USERNAME, UNKNOWN
    }

    private final Type type;
    private final String target;

    public Contact(Type type, String target) {
        this.type = type;
        this.target = target;
    }

    @NonNull
    public Type getType() {
        return type;
    }

    @NonNull
    public String getTarget() {
        return target;
    }
}
