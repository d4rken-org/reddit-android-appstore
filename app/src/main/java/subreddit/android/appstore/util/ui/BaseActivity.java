package subreddit.android.appstore.util.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import subreddit.android.appstore.AppStoreApp;
import subreddit.android.appstore.screens.MainActivity;
import timber.log.Timber;


public class BaseActivity extends AppCompatActivity {

    /**
     * Use {@code 2} as initial capacity for the navigation and tag drawers in {@link MainActivity},
     * as it's the most used case
     */
    private final List<OnBackKeyPressedListener> onBackKeyPressedListeners = new ArrayList<>(2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(((AppStoreApp) getApplication()).getSetTheme());
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        setTheme(((AppStoreApp) getApplication()).getSetTheme());
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppStoreApp.getRefWatcher().watch(this);
    }

    @Override
    public void onBackPressed() {
        boolean handled = false;
        for (OnBackKeyPressedListener listener : onBackKeyPressedListeners) {
            if (listener.onBackKeyPressed()) {
                handled = true;
                break;
            }
        }
        if (!handled) {
            super.onBackPressed();
        }
    }

    public void addOnBackKeyPressedListener(OnBackKeyPressedListener listener) {
        if (!onBackKeyPressedListeners.contains(listener)) {
            onBackKeyPressedListeners.add(listener);
        } else {
            Timber.w(new IllegalArgumentException("A back key listener was added twice"),
                    "A back key listener was added twice: %s", listener);
        }
    }

    public void removeOnBackKeyPressedListener(OnBackKeyPressedListener listener) {
        if (onBackKeyPressedListeners.contains(listener)) {
            onBackKeyPressedListeners.remove(listener);
        } else {
            Timber.w(new IllegalArgumentException("A back key listener was removed but never added"),
                    "A back key listener was removed but never added: %s", listener);
        }
    }

    public interface OnBackKeyPressedListener {
        /**
         * @return true if the event was handled, false otherwise
         */
        boolean onBackKeyPressed();
    }
}
