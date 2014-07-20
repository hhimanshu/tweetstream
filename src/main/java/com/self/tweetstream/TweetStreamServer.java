package com.self.tweetstream;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
    private static final Map<Session, Object> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void open(final Session session) {
        LOGGER.debug("new session {}", session.getId());
        sessions.put(session, new Object());
    }

    @OnMessage
    public void tweets(final String message, final Session session) throws IOException, InterruptedException {
        LOGGER.debug("new session id: {}", session.getId());
        sessions.put(session, new Object());

        final Client twitterClient = TwitterHoseBird.getInstance();
        while (!session.getOpenSessions().isEmpty()) {
            for (final Session s : session.getOpenSessions()) {
                if (twitterClient.isDone()) {
                    LOGGER.debug("Twitter Client Done, waiting ...");
                    TwitterHoseBird.getMsgQueue().poll(2, TimeUnit.SECONDS);
                }
                s.getAsyncRemote().sendText(TwitterHoseBird.getMsgQueue().take());
            }
        }
    }

    @OnError
    public void onError(final Throwable t, final Session session) {
        LOGGER.warn("error:{}", t.getMessage());
        sessions.remove(session);
        LOGGER.debug("closing session {}", session);
    }

    @OnClose
    public void onClose(final Session session, final CloseReason reason) throws IOException {
        sessions.remove(session);
        LOGGER.warn("closing session: {}, remaining session: {}", session.getId(), session.getOpenSessions().size());
    }
}
