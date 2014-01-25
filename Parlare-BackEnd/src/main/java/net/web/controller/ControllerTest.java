/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.web.controller;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
/**
 *
 * @author jesusrodriguez
 */
public final class ControllerTest implements Controller {
    
        public Map<String, String> httpParams;
    public Map<String, String> httpHeaders;
    private File httpFile;
    

    @Override
    public String toString() {
        return HelloWorld() + "<br>\n" + debugHttpParams() + "<br>\n";
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
    
    
        
    public String HelloWorld() {
    
        return "Hello Universe, give me the power to go beyond good and evil";
        
    
    }
    
}
