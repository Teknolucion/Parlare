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
/**
 *
 * @author jesusrodriguez
 */
public final class ControllerLoadVideo implements Controller {
    
    public Map<String, String> httpParams;
    public Map<String, String> httpHeaders;
    public File httpFile;
    

    @Override
    public String toString() {
        return HelloWorld() + "<br>\n" + debugHttpHeaders() + "<br>\n" + debugHttpParams() + "<br>\n" + processVideo();
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
    
    private String debugHttpHeaders() {
        
        String params = "";
        
        if (httpHeaders != null) {
            Iterator<String> e = httpHeaders.keySet().iterator();
            while (e.hasNext()) {
                String value = e.next();
                params += value + "' = '" + httpHeaders.get(value) + "'<br>";
            }
        }

        return "Headers = " + params + "<br>";

    }  
        
    public String HelloWorld() {
        try {
            
            
            String text = null;
            
            text += httpFile.getCanonicalPath() + " - <br>";
            text += httpFile.getAbsolutePath() + " - <br>";
            text += httpFile.getPath() + " - <br>";
            text += httpFile.getParent() + " - <br><br>";
            
            
            
            return text + " <- Hello Universe, give me the power to go beyond good and evil";
        } catch (IOException ex) {
            Logger.getLogger(ControllerLoadVideo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
        
    
    }
    
    
    
    
    private String processVideo() {
        
        
        if ( httpParams.containsKey("v") ) {
            
            
           
            String nameFile = httpFile.getParent() + "/" +  httpParams.get("v");
            
            File f = new File(nameFile);
            
            if (!f.exists()) {
                
                return "Don't file exists";
                
            } else {
                
                return "See parameter v=" + httpParams.get("v") + " => " + nameFile;
                
            }
            
        
        } else {
        
            return "Don't see the parameter \"v\" ...";
        }
        
    
    }
    
}
