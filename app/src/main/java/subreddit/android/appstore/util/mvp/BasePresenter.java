package subreddit.android.appstore.util.mvp;


import android.os.Bundle;

public interface BasePresenter<ViewType> {

    void onCreate(Bundle bundle);

    void onAttachView(ViewType view);

    void onDetachView();

    void onSaveInstanceState(Bundle bundle);

    void onDestroy();
}
