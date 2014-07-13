package com.self.tweetstream;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class TweetStreamTest {

    @ArquillianResource
    URI base;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class).addClasses(TweetStreamServer.class, TweetStreamClient.class);
    }

    @Test
    public void test() throws URISyntaxException, IOException, DeploymentException, InterruptedException {
        System.out.println("URI: " + getEndpointUrl());
        TweetStreamClient.latch = new CountDownLatch(1);
        Session session = connectToServer(TweetStreamClient.class, "tweets");
        assertNotNull(session);
        // (todo: harit) assert correct things
//        assertTrue(TweetStreamClient.latch.await(10, TimeUnit.SECONDS));
//        assertEquals("Hello", TweetStreamClient.response);
    }

    private URI getEndpointUrl() throws URISyntaxException {
        return new URI("ws://" + base.getHost() +
                ":" + base.getPort() + base.getPath());
    }

    public Session connectToServer(Class<?> endpoint, String uriPart) throws DeploymentException, IOException, URISyntaxException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        URI uri = new URI("ws://"
                + base.getHost()
                + ":"
                + base.getPort()
                + base.getPath()
                + uriPart);
        System.out.println("Connecting to: " + uri);
        return container.connectToServer(endpoint, uri);
    }
}