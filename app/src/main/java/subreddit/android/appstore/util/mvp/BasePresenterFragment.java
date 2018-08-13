package subreddit.android.appstore.util.mvp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.Objects;

import subreddit.android.appstore.util.ui.BaseFragment;

/**
 * Forked from https://github.com/michal-luszczuk/tomorrow-mvp
 */
public abstract class BasePresenterFragment<PresenterT extends BasePresenter<ViewT>,
        ViewT extends BaseView> extends BaseFragment
        implements LoaderHelper.Callback<PresenterT> {

    private static final int LOADER_ID = 2048;

    PresenterT presenter;

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new LoaderHelper<PresenterT, ViewT>(Objects.requireNonNull(getContext()), getLoaderManager(), LOADER_ID)
                .retrievePresenter(getPresenterFactory(), savedInstanceState, new LoaderHelper.Callback<PresenterT>() {
                    @Override
                    public void onPresenterReady(@NonNull PresenterT presenter) {
                        BasePresenterFragment.this.presenter = presenter;
                        BasePresenterFragment.this.onPresenterReady(presenter);
                    }

                    @Override
                    public void onPresenterDestroyed() {
                        BasePresenterFragment.this.presenter = null;
                        BasePresenterFragment.this.onPresenterDestroyed();
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        presenter.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        final Activity activity = getActivity();
        if (activity != null && activity.isFinishing()) {
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
