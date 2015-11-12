package kidouchi.noteit.api;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.User;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;

/**
 * Created by iuy407 on 9/22/15.
 * Singleton instance of TumblrApi
 */
public class TumblrApi {

    private static TumblrApi instance = null;

    private static final String REQUEST_TOKEN_URL = "https://www.tumblr.com/oauth/request_token";
    private static final String ACCESS_TOKEN_URL = "https://www.tumblr.com/oauth/access_token";
    private static final String AUTH_URL = "https://www.tumblr.com/oauth/authorize";

    private static final String CONSUMER_KEY = "y4bRHx5bLosTd2oJL0CQCjnfDYUZ2WjhySkuW3D8PtOV0gO9Fv";
    private static final String CONSUMER_SECRET = "kHxKDwIm6oPRwJWLpk4x4i5R4KEclB7A9WXFHsmCOSA1Vw7to2";

    public static final String CALLBACK_URL = "tumblrnoteit://tumblrnoteit.com/ok";

    private JumblrClient client = new JumblrClient(TumblrApi.CONSUMER_KEY,
                                                        TumblrApi.CONSUMER_SECRET);

    private CommonsHttpOAuthConsumer consumer =
            new CommonsHttpOAuthConsumer(TumblrApi.CONSUMER_KEY, TumblrApi.CONSUMER_SECRET);

    private CommonsHttpOAuthProvider provider = new CommonsHttpOAuthProvider(
            TumblrApi.REQUEST_TOKEN_URL,
            TumblrApi.ACCESS_TOKEN_URL,
            TumblrApi.AUTH_URL);

    private User user = null;

    private TumblrApi() {
        this.provider.setOAuth10a(true);
    }

    public static TumblrApi getInstance() {
        if (instance == null) {
            instance = new TumblrApi();
        }
        return instance;
    }

    public CommonsHttpOAuthConsumer getConsumer() {
        return this.consumer;
    }

    public void setConsumer(CommonsHttpOAuthConsumer consumer) {
        this.consumer = consumer;
    }

    public CommonsHttpOAuthProvider getProvider() {
        return this.provider;
    }

    public void setProvider(CommonsHttpOAuthProvider provider) {
        this.provider = provider;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public JumblrClient getClient() {
        return this.client;
    }

    public void setClient(JumblrClient client) {
        this.client = client;
    }
}
