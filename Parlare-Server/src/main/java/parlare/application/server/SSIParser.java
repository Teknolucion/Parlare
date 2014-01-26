/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parlare.application.server;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
/**
 *
 * @author jesusrodriguez
 */
public class SSIParser {
    private final Map<String, String> env;
    private final  CommandExec commandExec;

    public SSIParser(Map<String, String> env, CommandExec commandExec) {
        this.env = env;
        this.commandExec = commandExec;
    }

    public String parse(String source, Map<String, String> params, Map<String, String> headers, File file) {
        StringBuilder output = new StringBuilder();
        int start = 0;
        while (start < source.length()) {
            int command = source.indexOf("<!--#", start);
            if (command == -1) {
                command = source.length();
                output.append(source.substring(start, command));
                start = command;
                continue;
            }

            int endComment = source.indexOf("-->", command);
            if (endComment > command) {
                output.append(source.substring(start, command));
                SsiComment parsed = new SsiComment(source.substring(command+5, endComment));
                String commandString = parsed.getCommand();
                
                switch (commandString) {
                    case "exec":
                        String externalExecutable = parsed.getParam("cmd");
                        if (externalExecutable != null) {
                            output.append(commandExec.exec(externalExecutable));
                        } else {
                            String javaExecutable = parsed.getParam("java");
                            try {
                                output.append(commandExec.execJava(Class.forName(javaExecutable), parsed.getParameters(), params, headers, file));
                            } catch (ClassNotFoundException ignored) {}
                        }   break;
                    case "set":
                        {
                            String varname = parsed.getParam("var");
                            String varValue = parsed.getParam("value");
                            if (varname != null && varValue != null) {
                                env.put(varname, varValue);
                            }       break;
                        }
                    case "echo":
                        {
                            String varname = parsed.getParam("var");
                            String varValue = null;
                            if (varname != null) {
                                varValue = env.get(varname);
                            }       output.append(varValue != null ? varValue : "(none)");
                            break;
                        }
                }
                start = endComment + 3;
            }
        }
        return output.toString();
    }

    private class SsiComment {
        private String command;
        private Map<String, List<String>> parameters;

        public SsiComment(String text) {
            parameters = new HashMap<>();
            StringTokenizer tok = new StringTokenizer(text, " \t\n");
            if (tok.hasMoreTokens()) {
                command = tok.nextToken();
            }
            while (tok.hasMoreTokens()) {
                String kvp = tok.nextToken();
                int equals = kvp.indexOf('=');
                if (equals == -1) {
                    continue;
                }
                String key = kvp.substring(0,equals);
                String value = kvp.substring(equals+2, kvp.length()-1);
                List<String> paramValue = parameters.get(key);
                if (paramValue == null) {
                    paramValue = new ArrayList<>();
                    parameters.put(key, paramValue);
                }
                paramValue.add(value);
            }
        }

        private String getCommand() {
            return command;
        }

        public String getParam(String param) {
            List<String> value = parameters.get(param);
            if (value == null || value.isEmpty()) {
                return null;
            }
            return value.get(0);
        }

        public List<String> getParamValues(String param) {
            return parameters.get(param);
        }

        public Map<String, List<String>> getParameters() {
            return parameters;
        }
    }
}