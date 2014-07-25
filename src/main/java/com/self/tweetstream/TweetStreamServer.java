package com.self.tweetstream;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twitter.hbc.core.Client;

@ServerEndpoint("/tweets")
public class TweetStreamServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(TweetStreamServer.class);

    @OnOpen
    public void open(final Session session) {
        LOGGER.debug("new session {}", session.getId());
    }

    @OnMessage
    public void tweets(final String message, final Session session) throws IOException, InterruptedException {
        LOGGER.debug("new session id: {}", session.getId());

        final Client twitterClient = TwitterHoseBird.getInstance();
        while (!session.getOpenSessions().isEmpty()) {
            for (final Session s : session.getOpenSessions()) {
                if (twitterClient.isDone()) {
                    LOGGER.debug("Twitter Client Done, waiting ...");
                    TwitterHoseBird.getMsgQueue().poll(2, TimeUnit.SECONDS);
                }
                s.getAsyncRemote().sendText(TwitterHoseBird.getMsgQueue().take());
                TwitterHoseBird.getMsgQueue().clear();
            }
        }
    }

    @OnError
    public void onError(final Throwable t, final Session session) {
        LOGGER.warn("error:{}", t.getMessage());
        LOGGER.debug("closing session {}", session);
    }

    @OnClose
    public void onClose(final Session session, final CloseReason reason) throws IOException {
        LOGGER.warn("closing session: {}, remaining session: {}", session.getId(), session.getOpenSessions().size());
    }
}
