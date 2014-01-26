/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parlare.application.server;

/**
 *
 * @author jesusrodriguez
 */
import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandExec {

    public String exec(String command) {
        return "";
    }

    public String execJava(Class command, Map<String, List<String>> params, Map<String, String> getParams, Map<String, String> headers, File file) {
        if (params == null || params.isEmpty()) {
            return execJavaWithoutParams(command);
        }
        return execJavaWithParams(command, params, getParams, headers, file);
    }

    private String execJavaWithoutParams(Class command) {
        String returnValue = "";
        try {

            String returnObj = command.newInstance().toString();

            returnValue = returnObj;
        } catch (InstantiationException | IllegalAccessException e) {
        }
        return returnValue;
    }

    private String execJavaWithParams(Class command, Map<String, List<String>> params, Map<String, String> getParams, Map<String, String> headers, File file) {
        String returnValue = "";
        try {

            //            Date d = new Date();
//            Class cls = d.getClass();
//
//            Object obj1 = cls.newInstance();
//            System.out.println("Time = " + obj1);
            Object obj = command.newInstance();

            try {
                Field g = command.getField("httpParams");

                g.set(obj, getParams);

            } catch (NoSuchFieldException | SecurityException ex) {
                Logger.getLogger(CommandExec.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (params != null && params.size() > 0) {
                Field[] fields = command.getDeclaredFields();

                for (Field f : fields) {
                    List<String> values = params.get(f.getName());
                    if (values != null) {
                        String value = values.get(0);
                        try {
                            f.setAccessible(true);
                            f.set(obj, value);
                        } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
                        }
                    }
                }

            }
            
            try {
                Field g = command.getField("httpHeaders");
                g.set(obj, headers);

            } catch (NoSuchFieldException | SecurityException ex) {
                Logger.getLogger(CommandExec.class.getName()).log(Level.SEVERE, null, ex);
            }

            
            try {
                Field g;
                g = command.getField("httpFile");
                g.set(obj, file);
            } catch (    NoSuchFieldException | SecurityException ex) {
                Logger.getLogger(CommandExec.class.getName()).log(Level.SEVERE, null, ex);
            }
                
            
            returnValue = obj.toString();
        } catch (InstantiationException | IllegalAccessException | SecurityException e) {
        }
        return returnValue;
    }
}
