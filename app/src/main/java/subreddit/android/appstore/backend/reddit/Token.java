package subreddit.android.appstore.backend.reddit;


public class Token {
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
