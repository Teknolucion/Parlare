package parlare.server.httpd.integration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.http.client.CookieStore;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.cookie.BasicClientCookie;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import parlare.server.httpd.ParlareHTTPD;

public class CookieIntegrationTest extends IntegrationTestBase<CookieIntegrationTest.CookieTestServer> {

    @Test
    public void testNoCookies() throws Exception {
        HttpGet httpget = new HttpGet("http://localhost:8192/");
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        httpclient.execute(httpget, responseHandler);

        CookieStore cookies = httpclient.getCookieStore();
        assertEquals(0, cookies.getCookies().size());
    }

    @Test
    public void testCookieSentBackToClient() throws Exception {
        testServer.cookiesToSend.add(new ParlareHTTPD.Cookie("name", "value", 30));
        HttpGet httpget = new HttpGet("http://localhost:8192/");
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        httpclient.execute(httpget, responseHandler);

        CookieStore cookies = httpclient.getCookieStore();
        assertEquals(1, cookies.getCookies().size());
        assertEquals("name", cookies.getCookies().get(0).getName());
        assertEquals("value", cookies.getCookies().get(0).getValue());
    }

    @Test
    public void testServerReceivesCookiesSentFromClient() throws Exception {
        BasicClientCookie clientCookie = new BasicClientCookie("name", "value");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 100);
        clientCookie.setExpiryDate(calendar.getTime());
        clientCookie.setDomain("localhost");
        httpclient.getCookieStore().addCookie(clientCookie);
        HttpGet httpget = new HttpGet("http://localhost:8192/");
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        httpclient.execute(httpget, responseHandler);

        assertEquals(1, testServer.cookiesReceived.size());
        assertTrue(testServer.cookiesReceived.get(0).getHTTPHeader().contains("name=value"));
    }

    @Override public CookieTestServer createTestServer() {
        return new CookieTestServer();
    }

    public static class CookieTestServer extends ParlareHTTPD {
        List<Cookie> cookiesReceived = new ArrayList<>();
        List<Cookie> cookiesToSend = new ArrayList<>();

        public CookieTestServer() {
            super(8192);
        }

        @Override public Response serve(HTTPSession session) {
            CookieHandler cookies = session.getCookies();
            for (String cookieName : cookies) {
                cookiesReceived.add(new Cookie(cookieName, cookies.read(cookieName)));
            }
            for (Cookie c : cookiesToSend) {
                cookies.set(c);
            }
            return new Response("Cookies!");
        }
    }
}
