package subreddit.android.appstore.backend.reddit;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface TokenApi {
    String BASEURL = "https://www.reddit.com/";

    @FormUrlEncoded
    @POST("api/v1/access_token")
    Observable<TokenApi.Token> getUserlessAuthToken(@Header("Authorization") String authentication,
                                                    @Field("device_id") String deviceId,
                                                    @Field("grant_type") String grant_type,
                                                    @Field("scope") String scope);

    class Token {
        String access_token;
        String token_type;
        long expires_in;
        String scope;
        final long issuedTime = System.currentTimeMillis();

        public boolean isExpired() {
            return System.currentTimeMillis() > issuedTime + expires_in * 1000;
        }

        public String getAuthorizationString() {
            return token_type + " " + access_token;
        }
    }
}
