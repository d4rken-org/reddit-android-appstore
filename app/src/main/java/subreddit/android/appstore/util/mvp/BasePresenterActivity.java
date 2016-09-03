package subreddit.android.appstore.util.mvp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import subreddit.android.appstore.util.ui.BaseActivity;

/**
 * Forked from https://github.com/michal-luszczuk/tomorrow-mvp
 */
public abstract class BasePresenterActivity<PresenterT extends BasePresenter<ViewT>, ViewT extends BaseView> extends BaseActivity
        implements LoaderHelper.Callback<PresenterT> {

    private static final int LOADER_ID = 2016;

    PresenterT presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new LoaderHelper<PresenterT, ViewT>(this, getSupportLoaderManager(), LOADER_ID)
                .retrievePresenter(getPresenterFactory(), savedInstanceState, new LoaderHelper.Callback<PresenterT>() {
                    @Override
                    public void onPresenterReady(@NonNull PresenterT presenter) {
                        BasePresenterActivity.this.presenter = presenter;
                        BasePresenterActivity.this.onPresenterReady(presenter);
                    }

                    @Override
                    public void onPresenterDestroyed() {
                        BasePresenterActivity.this.presenter = null;
                        BasePresenterActivity.this.onPresenterDestroyed();
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onAttachView(getPresenterView());
    }

    @Override
    public void onPause() {
        presenter.onDetachView();
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        presenter.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        if (isFinishing()) {
            presenter.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void onPresenterReady(@NonNull PresenterT presenter) {

    }

    @Override
    public void onPresenterDestroyed() {

    }

    @NonNull
    protected abstract PresenterFactory<PresenterT> getPresenterFactory();

    public PresenterT getPresenter() {
        return presenter;
    }

    // Override in case of fragment not implementing Presenter<View> interface
    protected ViewT getPresenterView() {
        //noinspection unchecked
        return (ViewT) this;
    }
}
