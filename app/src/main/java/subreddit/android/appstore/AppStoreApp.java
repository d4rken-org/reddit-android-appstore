package subreddit.android.appstore;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;


public class AppStoreApp extends Application {
    public static final String LOGPREFIX = "RAS:";
    private static RefWatcher refWatcher;
    private int theme = 0;

    public static RefWatcher getRefWatcher() {
        return refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree() {
                @Override
                protected void log(final int priority, final String tag, final String message,
                                   final Throwable t) {
                    super.log(priority, LOGPREFIX + tag, message, t);
                }
            });
        }
        clearCache();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        refWatcher = LeakCanary.install(this);
        Injector.INSTANCE.init(this);
        theme = Integer.parseInt(prefs.getString("theme", "0"));
    }

    private void clearCache() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getInt("APP_VERSION",0)<BuildConfig.VERSION_CODE) {
            Timber.e("New release on %s, clearing cache database", BuildConfig.VERSION_NAME);
            Realm.init(getApplicationContext());
            Realm realm = Realm.getDefaultInstance();
            realm.close();
            Realm.deleteRealm(new RealmConfiguration.Builder().build());
            prefs.edit().putInt("APP_VERSION",BuildConfig.VERSION_CODE).commit();
        }
    }

    public int getSetTheme() {
        switch (theme) {
            case 0:
                return R.style.AppTheme;
            case 1:
                return R.style.AppTheme_Dark;
            case 2:
                return R.style.AppTheme_Black;
        }
        return R.style.AppTheme;
    }

    public void restart() {
        theme = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("theme", "0"));
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
            RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                    .deleteRealmIfMigrationNeeded()
                    .build();
            Realm.setDefaultConfiguration(realmConfig);
            appComponent = DaggerAppComponent.builder()
                    .androidModule(new AndroidModule(app))
                    .build();
        }

        public AppComponent getAppComponent() {
            return appComponent;
        }
    }
}
