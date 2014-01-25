/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parlare.server.restful;

/**
 *
 * @author jesusrodriguez
 */
public interface WebServerPluginInfo {
    
     String[] getMimeTypes();

    String[] getIndexFilesForMimeType(String mime);

    WebServerPlugin getWebServerPlugin(String mimeType);
    
}
