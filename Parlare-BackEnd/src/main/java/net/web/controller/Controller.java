/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.web.controller;

import java.io.File;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 *
 * @author jesusrodriguez
 */
public interface Controller {
    
    static final Locale currentLocale = new Locale("es");
    static final ResourceBundle settings = ResourceBundle.getBundle("settings");
    static final ResourceBundle lang = ResourceBundle.getBundle("lang", currentLocale);

    @Override
    String toString();
    
    Map<String, String> getHttpHeaders();    
    void setHttpHeaders(Map<String, String> httpHeaders);
    
    Map<String, String> getHttpParams();    
    void setHttpParams(Map<String, String> httpParams);
    
    File getHttpFile();
    void setHttpFile(File file);
    
}
