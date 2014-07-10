package com.self.tweetstream;

import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;

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
    URI uri;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class).addClasses(TweetStreamServer.class, TweetStreamClient.class);
    }

    @Test
    public void test() throws URISyntaxException {
        System.out.println("URI: " + getEndpointUrl());
        assertTrue(true);
    }

    private URI getEndpointUrl() throws URISyntaxException {
        return new URI("ws://" + uri.getHost() +
                ":" + uri.getPort() + uri.getPath());
    }
}