package subreddit.android.appstore.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.Loader;


public class ObjectRetainLoader<TypeT> extends Loader<TypeT> {

    private TypedObjectFactory<TypeT> typedObjectFactory;

    private TypeT objectToRetain;

    public ObjectRetainLoader(@NonNull Context context, @NonNull TypedObjectFactory<TypeT> typedObjectFactory) {
        super(context);
        this.typedObjectFactory = typedObjectFactory;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (objectToRetain != null) {
            deliverResult(objectToRetain);
        } else {
            forceLoad();
        }
    }

    @Override
    public void forceLoad() {
        createObjectToRetain();
        clearDataAfterCreation();
        deliverResult(objectToRetain);
    }

    public TypeT getObject() {
        return objectToRetain;
    }

    protected void createObjectToRetain() {
        objectToRetain = typedObjectFactory.create();
    }

    protected void clearDataAfterCreation() {
        typedObjectFactory = null;
    }
}
