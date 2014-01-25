package parlare.server.httpd;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import static junit.framework.Assert.*;

public class HttpDeleteRequestTest extends HttpServerTest {

    @Test
    public void testDeleteRequestThatDoesntSendBackResponseBody_EmptyString() throws Exception {
        testServer.response = new ParlareHTTPD.Response(ParlareHTTPD.Response.Status.NO_CONTENT, ParlareHTTPD.MIME_HTML, "");

        ByteArrayOutputStream outputStream = invokeServer("DELETE " + URI + " HTTP/1.1");

        String[] expected = {
                "HTTP/1.1 204 No Content",
                "Content-Type: text/html; charset=utf-8",
                "Date: .*",
                "Server: Tekno HTTPD/1.0.5 Java",
                "Connection: keep-alive",
                "Content-Length: 0",
                ""
        };

        assertResponse(outputStream, expected);
    }

    @Test
    public void testDeleteRequestThatDoesntSendBackResponseBody_NullString() throws Exception {
        testServer.response = new ParlareHTTPD.Response(ParlareHTTPD.Response.Status.NO_CONTENT, ParlareHTTPD.MIME_HTML, (String)null);

        ByteArrayOutputStream outputStream = invokeServer("DELETE " + URI + " HTTP/1.1");

        String[] expected = {
                "HTTP/1.1 204 No Content",
                "Content-Type: text/html; charset=utf-8",
                "Date: .*",
                "Server: Tekno HTTPD/1.0.5 Java",
                "Connection: keep-alive",
                "Content-Length: 0",
                ""
        };

        assertResponse(outputStream, expected);
    }

    @Test
    public void testDeleteRequestThatDoesntSendBackResponseBody_NullInputStream() throws Exception {
        testServer.response = new ParlareHTTPD.Response(ParlareHTTPD.Response.Status.NO_CONTENT, ParlareHTTPD.MIME_HTML, (InputStream)null);

        ByteArrayOutputStream outputStream = invokeServer("DELETE " + URI + " HTTP/1.1");

        String[] expected = {
                "HTTP/1.1 204 No Content",
                "Content-Type: text/html; charset=utf-8",
                "Date: .*",
                "Server: Tekno HTTPD/1.0.5 Java",
                "Connection: keep-alive",
                "Content-Length: 0",
                ""
        };

        assertResponse(outputStream, expected);
    }

    @Test
    public void testDeleteRequestThatSendsBackResponseBody_Success() throws Exception {
        testServer.response = new ParlareHTTPD.Response(ParlareHTTPD.Response.Status.OK, "application/xml", "<body />");

        ByteArrayOutputStream outputStream = invokeServer("DELETE " + URI + " HTTP/1.1");

        String[] expected = {
                "HTTP/1.1 200 OK",
                "Content-Type: application/xml",
                "Date: .*",
                "Server: Tekno HTTPD/1.0.5 Java",
                "Connection: keep-alive",
                "Content-Length: 8",
                "",
                "<body />"
        };

        assertResponse(outputStream, expected);
    }

    @Test
    public void testDeleteRequestThatSendsBackResponseBody_Accepted() throws Exception {
        testServer.response = new ParlareHTTPD.Response(ParlareHTTPD.Response.Status.ACCEPTED, "application/xml", "<body />");

        ByteArrayOutputStream outputStream = invokeServer("DELETE " + URI + " HTTP/1.1");

        String[] expected = {
                "HTTP/1.1 202 Accepted",
                "Content-Type: application/xml",
                "Date: .*",
                "Server: Tekno HTTPD/1.0.5 Java",
                "Connection: keep-alive",
                "Content-Length: 8",
                "",
                "<body />"
        };

        assertResponse(outputStream, expected);
    }
}
