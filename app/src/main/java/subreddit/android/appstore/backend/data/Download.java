package subreddit.android.appstore.backend.data;


import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Locale;

public class Download {

    public enum Type {
        GPLAY, FDROID, WEBSITE, UNKNOWN
    }

    private final Type type;
    private final String target;

    public Download(Type type, String target) {
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
    public Uri getDownloadUri() {
        return Uri.parse(target);
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "Download(type=%s, target=%s)", type, target);
    }
}
