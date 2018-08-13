package subreddit.android.appstore;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

public class AppStoreApp extends Application {
    public static final String LOGPREFIX = "RAS:";
    public static final String GITHUB_SIGNATURE = "FC4E2523E3509BA56E6AFDC36004958E2E94EAB2";
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
                protected void log(final int priority, final String tag, @NonNull final String message,
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
            prefs.edit().putInt("APP_VERSION", BuildConfig.VERSION_CODE).apply();
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
        assert i != null;
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public enum Injector {
        INSTANCE;
        AppComponent appComponent;

        Injector() {
        }

        void init(AppStoreApp app) {
            Realm.init(app);
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

    public static List<String> getSignatures(Context context, String packageName) {
        List<String> foundSignatures = new ArrayList<>();
        try {
            final Signature[] signatures = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures;
            for (final Signature sig : signatures) {
                final byte[] rawCert = sig.toByteArray();
                InputStream certStream = new ByteArrayInputStream(rawCert);
                CertificateFactory certFactory = CertificateFactory.getInstance("X509");
                X509Certificate x509Cert = (X509Certificate) certFactory.generateCertificate(certStream);

                MessageDigest md = MessageDigest.getInstance("SHA1");
                byte[] publicKey = md.digest(x509Cert.getEncoded());
                foundSignatures.add(bytesToHex(publicKey).toUpperCase());
            }
        } catch (Exception e) { e.printStackTrace(); }
        return foundSignatures;
    }

    private static String bytesToHex(byte[] in) {
        final StringBuilder out = new StringBuilder();
        for (byte b : in) out.append(String.format("%02x", b));
        return out.toString();
    }
}
