package parlare.application.frontend;

import java.util.Map;
import parlare.application.httpd.ParlareHTTPD;
import parlare.application.restful.ServerRunner;

public class DebugServer extends ParlareHTTPD {
    public DebugServer() {
        super(8082);
    }

    public static void main(String[] args) {
        ServerRunner.run(DebugServer.class);
    }

    @Override
    public Response serve(String uri, Method method, Map<String, String> header, Map<String, String> parms, Map<String, String> files) {
        
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<head><title>Debug Server</title></head>");
        sb.append("<body>");
        sb.append("<h1>Response</h1>");
        sb.append("<p><blockquote><b>URI -</b> ").append(String.valueOf(uri)).append("<br />");
        sb.append("<b>Method -</b> ").append(String.valueOf(method)).append("</blockquote></p>");
        sb.append("<h3>Headers</h3><p><blockquote>").append(String.valueOf(header)).append("</blockquote></p>");
        sb.append("<h3>Parms</h3><p><blockquote>").append(String.valueOf(parms)).append("</blockquote></p>");
        sb.append("<h3>Files</h3><p><blockquote>").append(String.valueOf(files)).append("</blockquote></p>");
        sb.append("</body>");
        sb.append("</html>");
        return new Response(sb.toString());
        
        
//        System.out.println(method + " '" + uri + "' ");
//        
//        StringBuilder sb = new StringBuilder();
//
//        sb.append("<html><body><h1>Hello server</h1>\n");
//        if (parms.get("username") == null)
//            sb.append("<form action='?' method='post'>\n" +
//                            "  <p>Your name: <input type='text' name='username'></p>\n" +
//                            "</form>\n");
//        else
//            sb.append("<p>Hello, ").append(parms.get("username")).append("!</p>)</body></html>\n");
//
//        return new Response(sb.toString());
    }
}
