/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.web.controller;

import java.io.IOException;
import java.util.Calendar;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;

/**
 *
 * @author jesusrodriguez
 */
public class ControllerFunctions {

    public void getCookie() {
        
        
    
    }
    
    
    public static String listCookies(String method, String listCookies) throws IOException {
        
        
        String printHTML = "";
        
        switch (method) {
            
            case "javascript":
                
                printHTML += "<script>";
                printHTML += "function setCookie(name,value,days) { ";
                printHTML += "var expires = \"\"; if (days) { var date = new Date(); date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000)); expires = \"; expires=\" + date.toGMTString(); } ";
                printHTML += "document.cookie = name + \"=\" + value + expires + \"; path=/\";";
                printHTML += "} ";

                printHTML += "function getCookie(name) { var nameEQ = name + \"=\"; var ca = document.cookie.split(';'); for (var i = 0; i < ca.length; i++) { var c = ca[i]; ";
                printHTML += "while (c.charAt(0) === ' ') { c = c.substring(1, c.length); } if (c.indexOf(nameEQ) === 0) { return c.substring(nameEQ.length, c.length); } } return null; } ";

                printHTML += "function eraseCookie(name) { setCookie(name, \"\", -1); } ";
                printHTML += "</script>";

                if (listCookies != null) {
                    String[] tokens = listCookies.split(";");
                    printHTML += "<script>";
                    for (String token : tokens) {
                        String[] data = token.trim().split("=");
                        if (data.length == 2) {
                            printHTML += " setCookie('" + data[0] + "', '" + data[1] + "', 60);";
                        }
                    }
                    printHTML += "</script>";
                }
                break;
            case "java":
                
                DefaultHttpClient httpClient = new DefaultHttpClient();
                CookieStore cookieStore = httpClient.getCookieStore();
                BasicClientCookie cookie = new BasicClientCookie("HelpMeUniverse", "I need your help my dear universe");

                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, 100);
                cookie.setExpiryDate(calendar.getTime());
                cookie.setDomain("localhost");
                cookie.setPath("/");

                cookieStore.addCookie(cookie);
                httpClient.setCookieStore(cookieStore);

                // Prepare a request object
                HttpGet httpGet = new HttpGet("http://localhost/");

                // Execute the request
                HttpResponse httpResponse = httpClient.execute(httpGet);

                // Examine the response status
                System.out.println("Http request response is: " + httpResponse.getStatusLine());

                break;
        }
        
        return printHTML;
    
    }
    
}
