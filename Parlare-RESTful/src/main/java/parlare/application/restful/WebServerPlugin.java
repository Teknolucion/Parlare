/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parlare.application.restful;

import parlare.application.httpd.ParlareHTTPD;
import java.io.File;
import java.util.Map;


/**
 *
 * @author jesusrodriguez
 */
public interface WebServerPlugin {
    
    void initialize(Map<String, String> commandLineOptions);

    boolean canServeUri(String uri, File rootDir);

    ParlareHTTPD.Response serveFile(String uri, Map<String, String> headers, File file, String mimeType);
    
}
