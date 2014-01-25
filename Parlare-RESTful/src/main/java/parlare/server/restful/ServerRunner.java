package parlare.server.restful;

import parlare.server.httpd.ParlareHTTPD;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLServerSocketFactory;

public class ServerRunner {
    public static void run(Class serverClass) {
        try {
            executeInstance((ParlareHTTPD) serverClass.newInstance());
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (KeyStoreException ex) {
            Logger.getLogger(ServerRunner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void executeInstance(ParlareHTTPD server) throws KeyStoreException {
        try {
            
            char[] storepass = "d13tp3ps1".toCharArray();
            
            SSLServerSocketFactory sslSocketFactory = ParlareHTTPD.makeSSLSocketFactory("teknohttpd.jks", storepass);
            
            server.makeSecure(sslSocketFactory);
            server.start();
            
            System.out.println(sslSocketFactory.toString());
            
            final File f = new File(SimpleWebServer.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            
            String classpath = System.getProperty("java.class.path");
            
            String absolutePath = f.getAbsolutePath();
            System.out.println(absolutePath);
            
            System.out.println(classpath);
            
        } catch (IOException ioe) {
            System.err.println( "Couldn't start server:\n" + ioe);
            System.exit(-1);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ServerRunner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CertificateException ex) {
            Logger.getLogger(ServerRunner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnrecoverableKeyException ex) {
            Logger.getLogger(ServerRunner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            Logger.getLogger(ServerRunner.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        System.out.println("Server started, Hit Enter to stop.\n");

        try {
            System.in.read();
        } catch (IOException ignored) {
        }

        server.stop();
        System.out.println("Server stopped.\n");
    }
}
