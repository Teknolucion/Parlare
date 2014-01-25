/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.web.httpd;

/**
 *
 * @author jesusrodriguez
 */
public class VideoWebServerPluginInfo implements WebServerPluginInfo {
    
    @Override public String[] getMimeTypes() {
        return new String[]{"text/html"};
    }

    @Override public String[] getIndexFilesForMimeType(String mime) {
        return new String[]{"index.mkv"};
    }

    @Override public WebServerPlugin getWebServerPlugin(String mimeType) {
        return new VideoWebServerPlugin();
    }
    
}
