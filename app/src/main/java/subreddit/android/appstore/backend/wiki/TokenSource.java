package subreddit.android.appstore.backend.wiki;

import android.content.Context;
import android.util.Base64;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Header;
import retrofit2.http.POST;

public class TokenSource {
    static final String BASEURL = "https://www.reddit.com";
    final Context context;
    final DeviceIdentifier deviceIdentifier;

    public TokenSource(Context context, DeviceIdentifier deviceIdentifier) {
        this.context = context;
        this.deviceIdentifier = deviceIdentifier;
    }

    public Observable<String> getToken() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASEURL)
                .build();
        String credentials = ":";
        final String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        TokenApi tokenApi = retrofit.create(TokenApi.class);
        return tokenApi.getNewToken
                (
                        "https://oauth.reddit.com/grants/installed_client",
                        deviceIdentifier.getUUID(),
                        basic
                )
                .subscribeOn(Schedulers.io())
                .map(new Function<TokenResponse, String>() {
                    @Override
                    public String apply(TokenResponse tokenResponse) throws Exception {
                        return null;
                    }
                });
    }

    interface TokenApi {
        @POST("api/v1/access_token")
        Observable<TokenResponse> getNewToken(
                @Header("grant_type") String grant_type,
                @Header("device_id") String deviceId,
                @Header("Authorization") String authentication
        );
    }

    class TokenResponse {
        String access_token;
        String token_type;
        String expires_in;
        String scope;
    }
}
