package subreddit.android.appstore.backend.data;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Locale;

import timber.log.Timber;


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
