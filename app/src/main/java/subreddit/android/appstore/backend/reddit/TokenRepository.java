package subreddit.android.appstore.backend.reddit;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import subreddit.android.appstore.BuildConfig;
import subreddit.android.appstore.backend.DeviceIdentifier;
import subreddit.android.appstore.backend.UserAgentInterceptor;
import timber.log.Timber;

public class TokenRepository {
    private static final String BASEURL = "https://www.reddit.com/";
    private static final String CLIENT_ID = "8i-tKlCSV9P_fQ";
    private static final String PREF_KEY = "reddit.token.userlessauth";
    final Context context;
    final DeviceIdentifier deviceIdentifier;
    private final TokenApi tokenApi;
    private final String encodedCredentials;
    private final Gson gson;
    private final SharedPreferences preferences;

    public TokenRepository(Context context, DeviceIdentifier deviceIdentifier, UserAgentInterceptor userAgent) {
        this.context = context;
        this.deviceIdentifier = deviceIdentifier;

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }
        builder.addInterceptor(userAgent);
        OkHttpClient client = builder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASEURL)
                .build();
        tokenApi = retrofit.create(TokenApi.class);

        String credentials = CLIENT_ID + ":";
        encodedCredentials = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        gson = new GsonBuilder().create();

        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public Observable<Token> getUserlessAuthToken() {
        return Observable.<Token>create(
                emitter -> {
                    Token token = getToken();
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
    Token getToken() {
        Token token = null;
        try {
            token = gson.fromJson(preferences.getString(PREF_KEY, null), Token.class);
        } catch (JsonSyntaxException ignore) {}
        if (token != null && token.isExpired()) {
            Timber.d("Token expired!");
            return null;
        }
        return token;
    }

    void storeToken(@NonNull Token token) {
        preferences.edit().putString(PREF_KEY, gson.toJson(token)).apply();
    }

    interface TokenApi {
        @FormUrlEncoded
        @POST("api/v1/access_token")
        Observable<Token> getUserlessAuthToken(
                @Header("Authorization") String authentication,
                @Field("device_id") String deviceId,
                @Field("grant_type") String grant_type,
                @Field("scope") String scope
        );
    }

}
