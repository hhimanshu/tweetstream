package com.self.tweetstream;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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

public class TwitterFilterHbc {
    private static final String CONSUMER_KEY = "pQ72WX3zCFTw9bcJw6EgqZF6w";
    private static final String CONSUMER_SECRET = "YWsY1i684Nw3NdL27ixUQIwMoIx2SV7i9TwUdQrNKNJB98fVjz";
    private static final String ACCESS_TOKEN = "2618859422-Yof0JYLSEO8reaxLCCUI38lENldnM9NvjHWgpK9";
    private static final String ACCESS_TOKEN_SECRET = "3fdIW91cxFvY80Ci90HWrgC3dfDKxudPsUO9cnvnPeaAh";

    public static void main(String[] args) throws InterruptedException {
        /** Set up your blocking queues: Be sure to size these properly based on expected TPS of your stream */
        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(100000);
        BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<Event>(1000);

        /** Declare the host you want to connect to, the endpoint, and authentication (basic auth or oauth) */
        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
        // Optional: set up some followings and track terms
        List<Long> followings = Lists.newArrayList(1234L, 566788L);
        List<String> terms = Lists.newArrayList("twitter", "api");
        hosebirdEndpoint.followings(followings);
        hosebirdEndpoint.trackTerms(terms);

        // These secrets should be read from a config file
        Authentication hosebirdAuth = new OAuth1(CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN, ACCESS_TOKEN_SECRET);


        ClientBuilder builder = new ClientBuilder()
                .name("Hosebird-Client-01")                              // optional: mainly for the logs
                .hosts(hosebirdHosts)
                .authentication(hosebirdAuth)
                .endpoint(hosebirdEndpoint)
                .processor(new StringDelimitedProcessor(msgQueue))
                .eventMessageQueue(eventQueue);                          // optional: use this if you want to process client events

        Client hosebirdClient = builder.build();
        // Attempts to establish a connection.
        hosebirdClient.connect();


        while (!hosebirdClient.isDone()) {
            String msg = msgQueue.take();
            System.out.println(msg);
        }
    }

}
