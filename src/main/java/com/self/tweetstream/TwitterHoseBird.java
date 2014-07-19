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

    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterHoseBird.class);
    private static final BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<>(1000);

    private static final BlockingQueue<String> msgQueue = new LinkedBlockingQueue<>(100000);
    private static Client client;

    public static Client getInstance(final String searchTerm) {
        if (client == null) {
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
                    .processor(new StringDelimitedProcessor(msgQueue));

            synchronized (TwitterHoseBird.class) {
                client = builder.build();
            }
            client.connect();
        }
        return client;
    }

    public static BlockingQueue<String> getMsgQueue() {
        return msgQueue;
    }

    public static void main(final String[] args) throws InterruptedException {

        final Client client = getInstance("fifa");
        while (!client.isDone()) {
            LOGGER.debug(msgQueue.take());
        }
    }
}
