/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.web.test;

import java.io.IOException;
import java.util.Map;
import parlare.server.httpd.ParlareHTTPD;

/**
 *
 * @author jesusrodriguez
 */
public class ServerTest extends ParlareHTTPD {

    public ServerTest(int port) {
        super(8081);
    }
    
    @Override
    public ParlareHTTPD.Response serve(String uri, ParlareHTTPD.Method method, Map<String, String> header, Map<String, String> parms, Map<String, String> files) {
        System.out.println(method + " '" + uri + "' ");

        String msg = "<html><body><h1>Test server</h1>\n";
        if (parms.get("username") == null)
            msg +=
                    "<form action='?' method='post'>\n" +
                            "  <p>Your name: <input type='text' name='username'></p>\n" +
                            "</form>\n";
        else
            msg += "<p>Hello, " + parms.get("username") + "!</p>";

        msg += "</body></html>\n";

        return new ParlareHTTPD.Response(msg);
    }
    
    public static void main(String[] args) {

        //executeInstance((ParlareHTTPD) ServerTest.class.newInstance());
        
        //System.out.println(ServerTest.class.toString());
        LittleServerRunner.run(ServerTest.class);
    }

}

/**
 *
 * @author jesusrodriguez
 */
class LittleServerRunner {


            
    public static void run(Class serverClass) {
        try {
            
            System.out.println("Run: " + serverClass.toString());
            
            LittleServerRunner.executeInstance((ParlareHTTPD) serverClass.newInstance());
            
        } catch (InstantiationException | IllegalAccessException e) {
        }
    }
    
    public static void executeInstance(ParlareHTTPD server) {

        try {
            
            System.out.println("Server: "  + server.toString());
            
            server.start();
            
        } catch (IOException ioe) {
            System.err.println(" Error en el inicio del servidor: \n" + ioe);
            System.exit(-1);
        }

        System.out.println("Servidor Iniciado\n");

//        try {
//            System.in.read();
//        } catch (IOException ignored) {
//        }
//
//        //server.stop();
//        System.out.println("Servidro detenido\n");

    }


}


