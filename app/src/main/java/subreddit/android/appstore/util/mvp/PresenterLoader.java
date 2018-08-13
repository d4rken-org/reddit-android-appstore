package subreddit.android.appstore.util.mvp;

import android.content.Context;
import android.os.Bundle;

import subreddit.android.appstore.util.ObjectRetainLoader;

public class PresenterLoader<PresenterT extends BasePresenter<ViewT>, ViewT> extends ObjectRetainLoader<PresenterT> {

    private Bundle savedState;

    PresenterLoader(Context context, PresenterFactory<PresenterT> factory, Bundle savedState) {
        super(context, factory);
        this.savedState = savedState;
    }

    public PresenterT getPresenter() {
        return getObject();
    }

    @Override
    protected void createObjectToRetain() {
        super.createObjectToRetain();
        getPresenter().onCreate(savedState);
    }

    @Override
    protected void clearDataAfterCreation() {
        super.clearDataAfterCreation();
        savedState = null;
    }
}
