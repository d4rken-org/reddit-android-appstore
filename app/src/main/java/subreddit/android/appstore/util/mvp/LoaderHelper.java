package subreddit.android.appstore.util.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

/**
 * Forked from https://github.com/michal-luszczuk/tomorrow-mvp
 */
public class LoaderHelper<PresenterT extends BasePresenter<ViewT>, ViewT extends BaseView> {
    private final int loaderId;
    private final LoaderManager loaderManager;
    final Context context;

    public LoaderHelper(@NonNull Context context, @NonNull LoaderManager manager, int loaderId) {
        this.context = context;
        this.loaderId = loaderId;
        this.loaderManager = manager;
    }

    public void retrievePresenter(
            @NonNull final PresenterFactory<PresenterT> factory,
            @Nullable Bundle savedState,
            @NonNull final Callback<PresenterT> presenterListener) {

        Loader<PresenterT> loader = loaderManager.getLoader(loaderId);

        if (loader instanceof PresenterLoader) {
            retrievePresenterFromExistingLoaderAndInformListener((PresenterLoader) loader, factory.getTypeClazz(), presenterListener);
        } else {
            loaderManager.initLoader(loaderId, savedState, new LoaderManager.LoaderCallbacks<PresenterT>() {
                @Override
                public Loader<PresenterT> onCreateLoader(int id, Bundle args) {
                    return new PresenterLoader<>(context, factory, args);
                }

                @Override
                public void onLoadFinished(Loader<PresenterT> loader, PresenterT presenter) {
                    presenterListener.onPresenterReady(presenter);
                }

                @Override
                public void onLoaderReset(Loader<PresenterT> loader) {
                    presenterListener.onPresenterDestroyed();
                }
            });
        }
    }

    protected void retrievePresenterFromExistingLoaderAndInformListener(
            @NonNull PresenterLoader loader,
            @NonNull Class<? extends PresenterT> presenterClazz,
            @NonNull Callback<PresenterT> presenterListener) {

        PresenterT presenter = retrievePresenterFromExistingLoader(loader, presenterClazz);
        if (presenter != null) {
            presenterListener.onPresenterReady(presenter);
        } else {
            throw new IllegalStateException("Loader presenter not of expected type");
        }
    }

    /**
     * Method get retained Presenter object from PresenterRetainLoader
     *
     * @param loader         Loader retaining presenter object
     * @param presenterClazz Type of presenter we are expecting to get
     * @return Presenter object retained in PresenterRetainLoader or null if presenter is not retained or
     * type of presenter is other than expected
     */
    @Nullable
    private PresenterT retrievePresenterFromExistingLoader(PresenterLoader loader, Class<? extends PresenterT> presenterClazz) {
        Object presenter = loader.getPresenter();
        if (presenterClazz.isInstance(presenter)) {
            return presenterClazz.cast(presenter);
        } else {
            return null;
        }
    }

    public interface Callback<PresenterT extends BasePresenter<?>> {
        void onPresenterReady(@NonNull PresenterT presenter);

        void onPresenterDestroyed();
    }
}
