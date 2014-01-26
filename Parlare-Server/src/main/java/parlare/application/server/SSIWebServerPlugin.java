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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import parlare.application.httpd.ParlareHTTPD;
import static parlare.application.httpd.ParlareHTTPD.MIME_HTML;
import static parlare.application.httpd.ParlareHTTPD.Response.Status.OK;

/**
 * @author Paul S. Hawke (paul.hawke@gmail.com) On: 9/13/13 at 4:03 AM
 */
public class SSIWebServerPlugin implements WebServerPlugin {

    private boolean enabled = false;
    private File workDir;

//    public SSIWebServerPlugin() {
//    }

    @Override
    public void initialize(Map<String, String> commandLineOptions) {
        String workDirPath = commandLineOptions.get("ssi_work_dir");
        if (workDirPath != null) {
            workDir = new File(workDirPath);
        } else {
            System.out.println("WARNING: Server side include plugin work directory not specified.  Use command-line option -X:ssi_work_dir=<path>");
            return;
        }
        enabled = (workDir.exists() && workDir.isDirectory());
        if (!enabled) {
            System.out.println("WARNING: Server side include plugin work directory invalid.  Check the value passed to -X:ssi_work_dir=<path>");
        }
    }

    @Override
    public boolean canServeUri(String uri, File rootDir) {
        if (!enabled) {
            return false;
        }
        File f = new File(rootDir, uri);
        return f.exists();
    }

    @Override
    public ParlareHTTPD.Response serveFile(String uri, Map<String, String> headers, Map<String, String> params, File file, String mimeType) {

        SSIParserFactory pf = new SSIParserFactory();
        SSIParser p = null;
        try {
            p = pf.create(uri, file);
        } catch (IOException ex) {
            Logger.getLogger(SSIWebServerPlugin.class.getName()).log(Level.SEVERE, null, ex);
        }
        String ssiSource = readSource(file);

        return ssiSource == null ? null
                : new ParlareHTTPD.Response(OK, MIME_HTML, p.parse(ssiSource, params, headers, file));

    }

    public String readSource(File file) {
        FileReader fileReader = null;
        BufferedReader reader = null;
        try {
            fileReader = new FileReader(file);
            reader = new BufferedReader(fileReader);
            String line;
            StringBuilder sb = new StringBuilder();
            do {
                line = reader.readLine();
                if (line != null) {
                    sb.append(line).append("\n");
                }
            } while (line != null);
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            return null;
        } finally {
            try {
                if (fileReader != null) {
                    fileReader.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ignored) {
            }
        }
    }
}
