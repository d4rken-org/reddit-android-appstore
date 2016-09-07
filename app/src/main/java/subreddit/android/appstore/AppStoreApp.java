package subreddit.android.appstore;

import android.app.Application;
import android.content.Intent;
import android.preference.PreferenceManager;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import timber.log.Timber;


public class AppStoreApp extends Application {
    public static final String LOGPREFIX = "RAS:";
    private static RefWatcher refWatcher;
    private boolean darkTheme=false;

    public static RefWatcher getRefWatcher() {
        return refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) Timber.plant(new Timber.DebugTree());
        refWatcher = LeakCanary.install(this);
        Injector.INSTANCE.init(this);
        darkTheme = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("night_mode",false);
    }

    public int getSetTheme() {
        return darkTheme ? R.style.AppTheme_Dark : R.style.AppTheme;
    }

    public void restart() {
        darkTheme = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("night_mode",false);
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public enum Injector {
        INSTANCE;
        AppComponent appComponent;

        Injector() {
        }

        void init(AppStoreApp app) {
            appComponent = DaggerAppComponent.builder()
                    .androidModule(new AndroidModule(app))
                    .build();
        }

        public AppComponent getAppComponent() {
            return appComponent;
        }
    }
}
