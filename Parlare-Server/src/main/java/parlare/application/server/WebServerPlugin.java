/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parlare.application.server;

import java.io.File;
import java.util.Map;
import parlare.application.httpd.ParlareHTTPD;


/**
 *
 * @author jesusrodriguez
 */
public interface WebServerPlugin {
    
    void initialize(Map<String, String> commandLineOptions);

    boolean canServeUri(String uri, File rootDir);

    ParlareHTTPD.Response serveFile(String uri, Map<String, String> headers, Map<String, String> params, File file, String mimeType);
    
}
