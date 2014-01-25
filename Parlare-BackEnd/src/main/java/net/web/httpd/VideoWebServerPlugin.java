/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.web.httpd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.uadetector.ReadableUserAgent;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;
import parlare.server.httpd.ParlareHTTPD;
import static parlare.server.httpd.ParlareHTTPD.Response.Status.OK;
import static parlare.server.httpd.ParlareHTTPD.MIME_HTML;

/**
 *
 * @author jesusrodriguez
 */
public class VideoWebServerPlugin implements WebServerPlugin {
    private File workDir;
    private boolean enabled;
    private FileInputStream stream;
    
    @Override
    public void initialize(Map<String, String> commandLineOptions) {
        String workDirPath = commandLineOptions.get("mkv_work_dir");
        if (workDirPath != null) {
            workDir = new File(workDirPath);
        } else {
            System.out.println("ADVERTENCIA: Directorio para el plugin \"Video de MiKnal\" no ha especificado.  Usar la siguiente opción en la línea de comandos: -X:mkv_work_dir=<ruta>");
            return;
        }
        enabled = (workDir.exists() && workDir.isDirectory());
        if (!enabled) {
            System.out.println("ADVERTENCIA: Directorio para el plugin \"Video de MiKnal\" es inválido.  Revise el valor del parametro en -X:mkv_work_dir=<ruta>");
        }
    }

    @Override
    public boolean canServeUri(String uri, File rootDir) {
        return true;
    }

    @Override
    public ParlareHTTPD.Response serveFile(String uri, Map<String, String> headers, Map<String, String> params, File file, String mimeType) {
        
        String fileVideo = params.get("v");
        
        UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();
        
        ReadableUserAgent agent = parser.parse(headers.get("user-agent"));
        
        String operatingSystem = agent.getOperatingSystem().getName();
        String deviceCategory = agent.getDeviceCategory().getName();
        String userAgent = agent.getFamily().getName();
        
        String pathRelative;
        String mime;
        String extension;
        Integer vHtml = 0;
        Boolean redirect = false;
        
//        if (deviceCategory.equals("Personal computer") && userAgent.equals("Chrome")) {
//            pathRelative = "videos/mp4/ios";
//            mime = "video/mp4";
//            extension = "mp4";
//            vHtml = 5;
//        } else if (deviceCategory.equals("Personal computer") && userAgent.equals("Firefox")) {
//            pathRelative = "videos/webm/hd";
//            mime = "video/webm";
//            extension = "webm";
//            vHtml = 5;
//        } else if (deviceCategory.equals("Personal computer") && userAgent.equals("Safari")) {
//            pathRelative = "videos/mp4/ios";
//            mime = "video/mp4";
//            extension = "mp4";
//            vHtml = 5;
//        } else if (deviceCategory.equals("Tablet") && userAgent.equals("Mobile Safari")) {
//            pathRelative = "videos/mp4/ios";
//            mime = "video/mp4";
//            extension = "mp4";
//            vHtml = 5;
//        } else if (deviceCategory.equals("Smartphone") && userAgent.equals("Mobile Safari")) {
//            pathRelative = "videos/mp4/ios";
//            mime = "video/mp4";
//            extension = "mp4";
//            vHtml = 5;
//        } else if (deviceCategory.equals("Smartphone") && userAgent.equals("BlackBerry Browser")) {
//            pathRelative = "videos/mp4/3gpp";
//            mime = "video/mp4";
//            extension = "mp4";
//            vHtml = 4;
//        } 
//        else if ( operatingSystem.indexOf("Android 2.") > -1 ) {
//            pathRelative = "videos/mp4/android";
//            mime = "video/mp4";
//            extension = "mp4";
//            vHtml = 4; 
//        } 
//        else if ( operatingSystem.indexOf("Android 4.") > -1 ) {
//            pathRelative = "videos/mp4/android";
//            mime = "video/mp4";
//            extension = "mp4";
//            vHtml = 5;
//        } 
//        else {
//            pathRelative = "videos/mp4/3gpp";
//            mime = "video/mp4";
//            extension = "mp4";
//            vHtml = 4;
//        }
        
        if (operatingSystem.indexOf("Android 2.") > -1 || (deviceCategory.equals("Smartphone") && userAgent.equals("BlackBerry Browser")) ) {
            
            pathRelative = "videos/mp4/small";
            mime = "video/mp4";
            extension = "mp4";
            vHtml = 4;
            
        } else if (deviceCategory.equals("Tablet") && userAgent.equals("Mobile Safari")) {
            pathRelative = "videos/mp4/small";
            mime = "video/mp4";
            extension = "mp4";
            vHtml = 5;
        } else if (deviceCategory.equals("Smartphone") && userAgent.equals("Mobile Safari")) {
            pathRelative = "videos/mp4/small";
            mime = "video/mp4";
            extension = "mp4";
            vHtml = 5;
        } else {

            pathRelative = "videos/mp4/small";
            mime = "video/mp4";
            extension = "mp4";
            vHtml = 5;

        }
        
        String pathVideo;
        
        pathVideo = file.getParent() + "/" + pathRelative + "/" + fileVideo + "." + extension;
        
        System.out.println("Path is " + pathVideo);
        System.out.println("Operating System is " + operatingSystem);
        System.out.println("Device Category is " + deviceCategory);
        System.out.println("User Agent is " + userAgent);

        try {
            stream = new FileInputStream(pathVideo);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VideoWebServerPlugin.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String currentURL = "http://" + headers.get("host") + "/" + pathRelative + "/" + fileVideo + "." + extension;
        
        System.out.println("Current URL is " + currentURL);
        
        String html51 = "<!doctype html>";
        html51 += "<head>"
                + "<!-- optimize mobile versions -->" 
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=2.0\">"
                + "<!-- player skin -->"
                + "<link rel=\"stylesheet\" type=\"text/css\" href=\"http://media.miknal.com/skin/minimalist.css\">"
                + "<!-- site specific styling -->"
                + "<style type=\"text/css\">"
                + "body { font: 12px \"Myriad Pro\", \"Lucida Grande\", sans-serif; text-align: center; padding-top: 1%; }"
                + ".flowplayer { width: 100%; }"
                + "html{"
                + "height: 100%;"
                + "}"
                + "body {"
                + "min-height: 100%;"
                + "}"
                + "</style>"
                + "<!-- flowplayer depends on jQuery 1.7.1+ (for now) -->"
                + "<script type=\"text/javascript\" src=\"http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js\"></script>"
                + "<!-- include flowplayer -->"
                + "<script type=\"text/javascript\" src=\"http://media.miknal.com/fp/flowplayer.min.js\"></script>"
                + "</head>"
                + "<body>"
                + "<!-- the player -->"
                + "<div class=\"flowplayer\" data-swf=\"http://media.miknal.com/fp/flowplayer.swf\" data-ratio=\"0.417\">"
                + "<video>"
                + "<source type=\"" + mime + "\" src=\"" + currentURL + "\">"
                + "</video>"
                + "</div>"
                + "</body>";
        
        String html5 = "<!doctype html>";
        html5 += "<html>"
                + "<head>"
                + "<title>MiKnal Player</title>"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                + "<!-- Chang URLs to wherever Video.js files will be hosted -->"
                + "<link href=\"http://media.miknal.com/videojs/video-js.css\" rel=\"stylesheet\" type=\"text/css\">"
                + "<!-- video.js must be in the <head> for older IEs to work. -->"
                + "<script src=\"http://media.miknal.com/videojs/video.js\"></script>"
                + "<!-- Unless using the CDN hosted version, update the URL to the Flash SWF -->"
                + "<script>"
                + "videojs.options.flash.swf = \"http://media.miknal.com/videojs/video-js.swf\";"
                + "</script>";
                if ( redirect == true ) {
                    
                    html5 += "<script language=\"Javascript\">"
                        + "if(this != top){"
                        //+ "top.location.href = this.location.href;"
                        + "top.location.href = \"http://media.miknal.com/index.mkv?v=" + fileVideo + "&s=1\";"
                        + "}"
                        + "</script>";
                    
                }
                html5 += "<style type=\"text/css\">"
                + "body { font: 12px \"Myriad Pro\", \"Lucida Grande\", sans-serif; text-align: center; background-color:#000; min-height: 100%;}"
                + "html{"
                + "height: 100%;"
                + "}"
                + "</style>"
                + "</head>"
                + "<body>";
                
                if ( params.containsKey("s") ) {
                    html5 += "<video id=\"current\" class=\"video-js vjs-default-skin\" fullscreen autoplay controls preload=\"true\" width=\"100%\" height=\"320\" data-setup=\"{}\">"
                    + "<source type=\"" + mime + "\" src=\"" + currentURL + "\">";
                    html5 += "</video>";
                } else if ( redirect == false ) {
                    html5 += "<video id=\"current\" class=\"video-js vjs-default-skin\" fullscreen autoplay controls preload=\"true\" width=\"100%\" height=\"320\" data-setup=\"{}\">"
                    + "<source type=\"" + mime + "\" src=\"" + currentURL + "\">";
                    html5 += "</video>";
                }
                
                html5 += "</body>"
                + "</html>";

        
        
        //String html4 = "<meta http-equiv=\"Refresh\" content=\"0; url=" + currentURL + "\">";
        
        String html4 = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">";
        html4 += "<html>";
        html4 += "<head>"
        + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">";
        html4 += "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">";
        html4 += "<title>MiKnal</title>";
        html4 += "<script language=\"Javascript\">"
        + "if(this != top){"
        + "top.location.href = \"http://media.miknal.com/index.mkv?v=" + fileVideo + "&s=1\";"
        + "}"
        + "</script>"
        + "<style type=\"text/css\">"
        + "body { font: 12px \"Myriad Pro\", \"Lucida Grande\", sans-serif; text-align: center; background-color:#000; min-height: 100%;}"
        + "A:link {text-decoration: none; color: white;}"
        + "A:visited {text-decoration: none}"
        + "A:active {text-decoration: none}"
        + "A:hover {text-decoration: underline; color: white;} "
        
        + "</style>";
        html4 += "</head>";
        html4 += "<body>";
        
        if ( params.containsKey("s") ) {
            html4 += "<object width=\"100%\" height=\"100%\" data=\"" + currentURL + "\"></object>"
                    + "<br><a href=\"javascript:history.back(3);\">Volver</a>";
            
        }
        
        html4 += "</body>";
        html4 += "</html>";
        
        
        String html;
        if ( vHtml == 5 ) {
            
            html = html5;
            
        } else {
            
            html = html4;
            
        }
        
        
        System.out.println("HTML is " + html);
        
//        <video width="320" height="240" controls>
//  <source src="movie.mp4" type="video/mp4">
//  <source src="movie.ogg" type="video/ogg">
//  Your browser does not support the video tag.
//</video>
        
        
//        URLConnection connection = null;
//        try {
//            connection = new URL(currentURL).openConnection();
//        } catch (IOException ex) {
//            Logger.getLogger(VideoWebServerPlugin.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        connection.setRequestProperty("Accept-Charset", "UTF-8");
//        
//        try {
//            InputStream response = connection.getInputStream();
//        } catch (IOException ex) {
//            Logger.getLogger(VideoWebServerPlugin.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
        return stream == null ? null
                : new ParlareHTTPD.Response(OK, MIME_HTML, html);
        
    }
    
}
