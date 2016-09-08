package subreddit.android.appstore.backend.data;


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

    public Type getType() {
        return type;
    }

    public String getTarget() {
        return target;
    }
}
