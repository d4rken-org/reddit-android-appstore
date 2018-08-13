package subreddit.android.appstore.backend.reddit;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import subreddit.android.appstore.backend.DeviceIdentifier;
import timber.log.Timber;

public class TokenRepository {
    private static final String CLIENT_ID = "8i-tKlCSV9P_fQ";
    private static final String PREF_KEY = "reddit.token.userlessauth";
    final Context context;
    final DeviceIdentifier deviceIdentifier;
    private final TokenApi tokenApi;
    private final String encodedCredentials;
    private final Gson gson;
    private final SharedPreferences preferences;

    public TokenRepository(Context context,
                           DeviceIdentifier deviceIdentifier,
                           TokenApi tokenApi,
                           Gson gson) {
        this.context = context;
        this.deviceIdentifier = deviceIdentifier;
        this.gson = gson;
        this.tokenApi = tokenApi;

        String credentials = CLIENT_ID + ":";
        encodedCredentials = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public Observable<TokenApi.Token> getUserlessAuthToken() {
        return Observable.<TokenApi.Token>create(
                emitter -> {
                    TokenApi.Token token = getToken();
                    if (token != null) emitter.onNext(token);
                    emitter.onComplete();
                })
                .switchIfEmpty(tokenApi.getUserlessAuthToken
                        (
                                encodedCredentials,
                                deviceIdentifier.getUUID(),
                                "https://oauth.reddit.com/grants/installed_client",
                                "wikiread"
                        )
                        .subscribeOn(Schedulers.io())
                        .doOnNext(this::storeToken));
    }

    @Nullable
    TokenApi.Token getToken() {
        TokenApi.Token token = null;
        try {
            token = gson.fromJson(preferences.getString(PREF_KEY, null), TokenApi.Token.class);
        } catch (JsonSyntaxException ignore) {}
        if (token != null && token.isExpired()) {
            Timber.d("TokenApi expired!");
            return null;
        }
        return token;
    }

    void storeToken(@NonNull TokenApi.Token token) {
        preferences.edit().putString(PREF_KEY, gson.toJson(token)).apply();
    }
}