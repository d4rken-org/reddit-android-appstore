package subreddit.android.appstore;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import dagger.Module;
import dagger.Provides;
import subreddit.android.appstore.util.dagger.ApplicationScope;


@Module
public class AndroidModule {
    private final AppStoreApp app;

    public AndroidModule(AppStoreApp app) {this.app = app;}

    @Provides
    @ApplicationScope
    public Context provideContext() {
        return app.getApplicationContext();
    }

    @Provides
    @ApplicationScope
    public SharedPreferences providePreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }


}
