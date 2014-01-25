package net.web.httpd;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLServerSocketFactory;
import parlare.server.httpd.ParlareHTTPD;

public class ServerRunner {
    
    private final static Locale currentLocale = new Locale("es");
    private static final ResourceBundle settings = ResourceBundle.getBundle("settings");
    private static final ResourceBundle lang = ResourceBundle.getBundle("lang", currentLocale);
    
    public static void run(Class serverClass) {
        try {
            executeInstance((ParlareHTTPD) serverClass.newInstance());
            executeInstanceSSL((ParlareHTTPD) serverClass.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
        } catch (KeyStoreException ex) {
            Logger.getLogger(ServerRunner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void executeInstanceSSL(ParlareHTTPD server) throws KeyStoreException {
        try {
            
            String keyPassword = settings.getString("KeyStorePassword");
            
            char[] storepass = keyPassword.toCharArray();
            
            //final File f = new File(SimpleWebServer.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            
            SSLServerSocketFactory sslSocketFactory = ParlareHTTPD.makeSSLSocketFactory(settings.getString("KeyStoreFile"), storepass);
  
            server.makeSecure(sslSocketFactory);
            server.start();
  
        } catch (IOException ioe) {
            System.err.println( lang.getString("TextCantStartServerSSL") + ":\n" + ioe);
            System.exit(-1);
        } catch (NoSuchAlgorithmException | CertificateException | UnrecoverableKeyException | KeyManagementException ex) {
            Logger.getLogger(ServerRunner.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.print(lang.getString("TextServerStartedSSL"));
        
    }
    
    public static void executeInstance(ParlareHTTPD server) {
        
        try {
            server.start();
        } catch (IOException ioe) {
            System.err.println(lang.getString("TextCantStartServer") + ": \n" + ioe);
            System.exit(-1);
        }

        System.out.println(lang.getString("TextServerStarted") + "\n");
        
        
        try {
            System.in.read();
        } catch (IOException ignored) {
        }

        server.stop();
        System.out.println(lang.getString("TextServerStopped") + "\n");

    }
}
