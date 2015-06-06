package be.ordina.orientdb.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by ToVn on 05/06/15.
 */
@Component
@ConfigurationProperties(prefix="twitterConnection")
public class ConnectionSettings {

    private String consumerKey;
    private String consumerSecret;
    private String accessToken;
    private String accessTokenSecret;


    public String getConsumerKey() {
        return consumerKey;
    }
    public String getConsumerSecret() {
        return consumerSecret;
    }
    public String getAccessToken() {
        return accessToken;
    }
    public String getAccessTokenSecret() {
        return accessTokenSecret;
    }
    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }
    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    public void setAccessTokenSecret(String accessTokenSecret) {
        this.accessTokenSecret = accessTokenSecret;
    }



}
