package subreddit.android.appstore.backend.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class Contact {

    public enum Type {
        MAIL, WEBSITE, REDDIT, TWITTER, INSTAGRAM, UNKNOWN
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

    @Nullable
    public String getTarget() {
        return target;
    }
}
