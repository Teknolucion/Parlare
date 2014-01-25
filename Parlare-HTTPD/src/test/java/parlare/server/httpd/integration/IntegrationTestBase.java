package parlare.server.httpd.integration;

import java.io.IOException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.After;
import org.junit.Before;
import parlare.server.httpd.ParlareHTTPD;

/**
 * @author Paul S. Hawke (paul.hawke@gmail.com)
 *         On: 9/2/13 at 10:02 PM
 * @param <T>
 */
public abstract class IntegrationTestBase<T extends ParlareHTTPD> {
    protected DefaultHttpClient httpclient;
    protected T testServer;

    @Before
    public void setUp() {
        testServer = createTestServer();
        httpclient = new DefaultHttpClient();
        try {
            testServer.start();
        } catch (IOException e) {
        }
    }

    @After
    public void tearDown() {
        httpclient.getConnectionManager().shutdown();
        testServer.stop();
    }

    public abstract T createTestServer();
}
