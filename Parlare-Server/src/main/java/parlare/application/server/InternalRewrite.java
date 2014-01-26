/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parlare.application.server;

import java.util.Map;
import parlare.application.httpd.ParlareHTTPD.Response;

/**
 *
 * @author jesusrodriguez
 */
public class InternalRewrite extends Response {
    
    private final String uri;
    private final Map<String, String> headers;

    public InternalRewrite(Map<String, String> headers, String uri) {
        super(null);
        this.headers = headers;
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
    
}
