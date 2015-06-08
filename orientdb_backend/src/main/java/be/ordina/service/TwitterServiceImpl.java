package be.ordina.service;

import be.ordina.dto.PersonDTO;
import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ToVn on 05/06/15.
 */
public class TwitterServiceImpl implements TwitterService {

    private String twitterConsumerKey;
    private String twitterConsumerSecret;
    private String twitterAccessToken;
    private String twitterAccessTokenSecret;

    private ConfigurationBuilder cb;
    private Configuration twitterConfiguration;

    public TwitterServiceImpl (String twitterConsumerKey, String twitterConsumerSecret, String twitterAccessToken
            , String twitterAccessTokenSecret) {

        this.twitterAccessToken = twitterAccessToken;
        this.twitterAccessTokenSecret = twitterAccessTokenSecret;
        this.twitterConsumerKey = twitterConsumerKey;
        this.twitterConsumerSecret = twitterConsumerSecret;

        cb = initializeConfigurationBuilderTwitter();


    }

    private ConfigurationBuilder initializeConfigurationBuilderTwitter() {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setDebugEnabled(true);
        configurationBuilder.setOAuthConsumerKey(twitterConsumerKey);
        configurationBuilder.setOAuthConsumerSecret(twitterConsumerSecret);
        configurationBuilder.setOAuthAccessToken(twitterAccessToken);
        configurationBuilder.setOAuthAccessTokenSecret(twitterAccessTokenSecret);

        return configurationBuilder;
    }


    public List<Long> getConnections(String userId) {
        List<Long> connections = new ArrayList<Long>();


        if (twitterConfiguration == null) {
            twitterConfiguration = cb.build();
        }

        TwitterFactory tf = new TwitterFactory(twitterConfiguration);
        Twitter twitter = tf.getInstance();

        try {


            IDs ids = twitter.getFollowersIDs(userId, -1);

            for (Long id : ids.getIDs()) {
                connections.add(id);
            }

            System.out.println("connections ======>  " + connections);

            // Printing fetched records from DB.
        } catch (TwitterException te) {
            handleTwitterException(te);
        }



        return connections;


    }

    public List<Long> getConnectionsById(Long userId) {
        List<Long> connections = new ArrayList<Long>();


        if (twitterConfiguration == null) {
            twitterConfiguration = cb.build();
        }

        TwitterFactory tf = new TwitterFactory(twitterConfiguration);
        Twitter twitter = tf.getInstance();

        try {
            IDs ids = twitter.getFollowersIDs(userId, -1);

            for (Long id : ids.getIDs()) {
                connections.add(id);
            }

            System.out.println("connections ======>  " + connections);

            // Printing fetched records from DB.
        } catch (TwitterException te) {
            handleTwitterException(te);
        }



        return connections;


    }

    public User getPersonDetails(Long id) {
        User user = null;

        if (twitterConfiguration == null) {
            twitterConfiguration = cb.build();
        }

        TwitterFactory tf = new TwitterFactory(twitterConfiguration);
        Twitter twitter = tf.getInstance();

        try {

            user = twitter.showUser(id);

            System.out.println("person details ======>  " + user);
        } catch (TwitterException te) {
            handleTwitterException(te);
        }

        return user;
    }


    public User getPersonDetails(String screenName) {
        User user = null;

        if (twitterConfiguration == null) {
            twitterConfiguration = cb.build();
        }

        TwitterFactory tf = new TwitterFactory(twitterConfiguration);
        Twitter twitter = tf.getInstance();

        try {

            user = twitter.showUser(screenName);

            System.out.println("person details ======>  " + user);
        } catch (TwitterException te) {
            handleTwitterException(te);
        }

        return user;
    }

    private void handleTwitterException(TwitterException te) {
        System.out.println("te.getErrorCode() " + te.getErrorCode());
        System.out.println("te.getExceptionCode() "
                + te.getExceptionCode());
        System.out.println("te.getStatusCode() " + te.getStatusCode());
        if (te.getStatusCode() == 401) {
            System.out
                    .println("Twitter Error : \nAuthentication "
                            + "credentials (https://dev.twitter.com/pages/auth) "
                            + "were missing or incorrect.\nEnsure that you have "
                            + "set valid consumer key/secret, access "
                            + "token/secret, and the system clock is in sync.");
        } else {
            System.out.println("Twitter Error : " + te.getMessage());
        }

        te.printStackTrace();
    }



    public PersonDTO transformToPersonDTO(User user) {
        return  new PersonDTO(user.getId(), user.getScreenName(), user.getName(), user.getDescription(), user.getFollowersCount());
    }

}
