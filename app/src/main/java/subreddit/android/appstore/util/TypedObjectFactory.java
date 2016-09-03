package subreddit.android.appstore.util;

public interface TypedObjectFactory<TypeT> {
    TypeT create();

    Class<? extends TypeT> getTypeClazz();
}
