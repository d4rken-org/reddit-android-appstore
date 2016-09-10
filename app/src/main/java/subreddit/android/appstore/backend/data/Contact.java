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

    /**
     * @return can return NULL if type is UNKNOWN
     */
    @Nullable
    public Intent getContactIntent(@NonNull Context context) {
        switch (type) {
            case EMAIL:
                return new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", target, null));
            case WEBSITE:
                // TODO do reddit apps recognize a specific message intent?
                return new Intent(Intent.ACTION_VIEW, Uri.parse(target));
            case REDDIT_USERNAME:
                return new Intent(Intent.ACTION_VIEW, Uri.parse(String.format(Locale.US, "http://www.reddit.com/message/compose/?to=%s", target)));
            default:
                try {
                    return new Intent(Intent.ACTION_VIEW, Uri.parse(target));
                } catch (Exception e) {
                    Timber.e(e, "Failed to uri parse %s", target);
                    return null;
                }
        }
    }
}
