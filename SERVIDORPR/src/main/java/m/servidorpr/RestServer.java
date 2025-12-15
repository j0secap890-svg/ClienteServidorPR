package m.servidorpr;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class RestServer {
    private int puerto;
    private static final String TOKEN = "mi_token_123";
    public RestServer(int puerto) { this.puerto = puerto; }

    public void start() {
        try {
            HttpServer s = HttpServer.create(new InetSocketAddress(puerto), 0);
            s.createContext("/saludo", this::handleSaludo);
            s.createContext("/mensajes", this::handleMensajes);
            s.setExecutor(null);
            s.start();
            System.out.println("[REST] listo en " + puerto);
        } catch (Exception e) { System.out.println("[REST] Error: " + e.getMessage()); }
    }

    private void handleSaludo(HttpExchange ex) throws IOException {
        if (!checkToken(ex)) { send(ex, 401, "{\"error\":\"Unauthorized\"}"); return; }
        send(ex,200,"{\"msg\":\"Hola desde REST\"}");
    }

    private void handleMensajes(HttpExchange ex) throws IOException {
        if (!checkToken(ex)) { send(ex, 401, "{\"error\":\"Unauthorized\"}"); return; }
        String method = ex.getRequestMethod();
        if ("GET".equalsIgnoreCase(method)) {
            List<String> msgs = MessageDAO.getAllMessages();
            String json = msgs.stream().map(m->"\""+m.replace("\"","\\\"")+"\"").collect(Collectors.joining(", ", "[", "]"));
            send(ex,200,json);
            return;
        }
        if ("POST".equalsIgnoreCase(method)) {
            String body = new BufferedReader(new InputStreamReader(ex.getRequestBody(), StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));
            MessageDAO.saveMessage("rest-client", body);
            send(ex,201,"{\"status\":\"saved\"}");
            return;
        }
        if ("DELETE".equalsIgnoreCase(method)) {
            String query = ex.getRequestURI().getQuery(); int id = 0;
            if (query!=null && query.startsWith("id=")) id = Integer.parseInt(query.substring(3));
            boolean ok = MessageDAO.deleteMessage(id);
            send(ex, ok?200:400, "{\"deleted\":"+ok+"}");
            return;
        }
        send(ex,405,"{\"error\":\"Method not allowed\"}");
    }

    private boolean checkToken(HttpExchange ex) {
        String q = ex.getRequestURI().getQuery();
        return q!=null && q.contains("token="+TOKEN);
    }

    private void send(HttpExchange ex, int code, String body) throws IOException {
        byte[] b = body.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().add("Content-Type","application/json; charset=UTF-8");
        ex.sendResponseHeaders(code, b.length);
        try (OutputStream os = ex.getResponseBody()) { os.write(b); }
    }
}
