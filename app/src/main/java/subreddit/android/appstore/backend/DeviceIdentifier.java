package subreddit.android.appstore.backend;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.UUID;

public class DeviceIdentifier {
    static final String PREF_KEY = "device.uuid";
    final Context context;

    public DeviceIdentifier(Context context) {this.context = context;}

    public String getUUID() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String uuid = prefs.getString(PREF_KEY, null);
        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
            prefs.edit().putString(PREF_KEY, uuid).apply();
        }
        return uuid;
    }
}
