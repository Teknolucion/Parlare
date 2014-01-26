/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parlare.application.server.controller;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import parlare.application.server.model.Database;

/**
 *
 * @author jesusrodriguez
 */
public class ControllerJSON implements Controller {

    public Map<String, String> httpParams;
    public Map<String, String> httpHeaders;
    private File httpFile;
    

    @Override
    public String toString() {
        try {
            return debugDatabase() + "\n" + debugHttpParams() + "\n" + debugHttpHeaders();
        } catch (IOException ex) {
            Logger.getLogger(ControllerJSON.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    @Override
    public Map<String, String> getHttpParams() {
        return httpParams;
    }

    @Override
    public void setHttpParams(Map<String, String> httpParams) {
        this.httpParams = httpParams;
    }

    @Override
    public Map<String, String> getHttpHeaders() {
        return httpHeaders;
    }

    @Override
    public void setHttpHeaders(Map<String, String> httpHeaders) {
        this.httpHeaders = httpHeaders;
    }
    
    
    @Override
    public File getHttpFile() {
        return httpFile;
    }

    @Override
    public void setHttpFile(File httpFile) {
        this.httpFile = httpFile;
    }

    private String debugHttpHeaders() throws IOException {

        
        String listCookies = httpHeaders.get("cookie");
        return ControllerFunctions.listCookies("javascript", listCookies) + "Cookies: " + listCookies;

    }

    private String debugHttpParams() {
        
        String params = "";
        
        if (httpParams != null) {
            Iterator<String> e = httpParams.keySet().iterator();
            while (e.hasNext()) {
                String value = e.next();
                params += value + "' = '" + httpParams.get(value) + "'<br>";
            }
        }

        return "Query String = " + params + "<br>";

    }

    public String debugDatabase() {

        Database db;
        db = new Database();
        return db.toString();
     
    }


}
