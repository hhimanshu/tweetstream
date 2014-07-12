package com.self.tweetstream;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterFilter {
    private static final String CONSUMER_KEY = "pQ72WX3zCFTw9bcJw6EgqZF6w";
    private static final String CONSUMER_SECRET = "YWsY1i684Nw3NdL27ixUQIwMoIx2SV7i9TwUdQrNKNJB98fVjz";
    private static final String ACCESS_TOKEN = "2618859422-Yof0JYLSEO8reaxLCCUI38lENldnM9NvjHWgpK9";
    private static final String ACCESS_TOKEN_SECRET = "3fdIW91cxFvY80Ci90HWrgC3dfDKxudPsUO9cnvnPeaAh";

    public static void main(String[] args) {
        TwitterFilter twitterFilter = new TwitterFilter();
    }

    public TwitterFilter() {
        final ConfigurationBuilder configurationBuilder = getConfigurationBuilder();

        final TwitterStream twitterStream = new TwitterStreamFactory(configurationBuilder.build()).getInstance();
        final StatusListener statusListener = getStatusListener();

        final FilterQuery filterQuery = new FilterQuery();
        filterQuery.track(new String[] { "fifa" });
        filterQuery.language(new String[] { "en" });


        twitterStream.addListener(statusListener);
        twitterStream.filter(filterQuery);
    }

    private static StatusListener getStatusListener() {
        return new StatusListener() {
            @Override
            public void onStatus(final Status status) {
                System.out.println(status.getText());
            }

            @Override
            public void onDeletionNotice(final StatusDeletionNotice statusDeletionNotice) {

            }

            @Override
            public void onTrackLimitationNotice(final int i) {

            }

            @Override
            public void onScrubGeo(final long userId, final long upToStatusId) {
            }

            @Override
            public void onStallWarning(final StallWarning stallWarning) {

            }

            @Override
            public void onException(final Exception e) {
                e.printStackTrace();
            }
        };
    }

    private static ConfigurationBuilder getConfigurationBuilder() {
        final ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setDebugEnabled(true);
        configurationBuilder.setOAuthConsumerKey(CONSUMER_KEY);
        configurationBuilder.setOAuthConsumerSecret(CONSUMER_SECRET);
        configurationBuilder.setOAuthAccessToken(ACCESS_TOKEN);
        configurationBuilder.setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);
        return configurationBuilder;
    }
}
