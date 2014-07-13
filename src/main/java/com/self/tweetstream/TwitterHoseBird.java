package com.self.tweetstream;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

/**
 * Reference: https://github.com/twitter/hbc
 */
public class TwitterHoseBird {
    private static final String CONSUMER_KEY = "pQ72WX3zCFTw9bcJw6EgqZF6w";
    private static final String CONSUMER_SECRET = "YWsY1i684Nw3NdL27ixUQIwMoIx2SV7i9TwUdQrNKNJB98fVjz";
    private static final String ACCESS_TOKEN = "2618859422-Yof0JYLSEO8reaxLCCUI38lENldnM9NvjHWgpK9";
    private static final String ACCESS_TOKEN_SECRET = "3fdIW91cxFvY80Ci90HWrgC3dfDKxudPsUO9cnvnPeaAh";

    private final BlockingQueue<String> msgQueue;
    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterHoseBird.class);
    private static final BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<>(1000);

    public TwitterHoseBird() {
        msgQueue = new LinkedBlockingQueue<>(100000);
    }

    public Client getInstance(final String searchTerm) {
        final Hosts hoseBirdHosts = new HttpHosts(Constants.STREAM_HOST);
        final StatusesFilterEndpoint hoseBirdEndpoint = new StatusesFilterEndpoint();


        final List<String> terms = Lists.newArrayList(searchTerm);
        hoseBirdEndpoint.trackTerms(terms);
        hoseBirdEndpoint.languages(Lists.newArrayList("en"));

        final Authentication hoseBirdAuth = new OAuth1(CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN, ACCESS_TOKEN_SECRET);

        final ClientBuilder builder = new ClientBuilder()
                .name("tweetStream-client")                              // optional: mainly for the logs
                .hosts(hoseBirdHosts)
                .authentication(hoseBirdAuth)
                .endpoint(hoseBirdEndpoint)
                .processor(new StringDelimitedProcessor(msgQueue))
                .eventMessageQueue(eventQueue);                          // optional: use this if you want to process client events

        final Client hoseBirdClient = builder.build();
        hoseBirdClient.connect();
        return hoseBirdClient;
    }

    public BlockingQueue<String> getMsgQueue() {
        return msgQueue;
    }

    public static void main(final String[] args) throws InterruptedException {
        final TwitterHoseBird twitterHoseBird = new TwitterHoseBird();
        final Client client = twitterHoseBird.getInstance("fifa");
        while (!client.isDone()) {
            LOGGER.debug(twitterHoseBird.msgQueue.take());
        }
    }
}
