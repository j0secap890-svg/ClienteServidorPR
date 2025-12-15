package m.clientepr;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RESTClient {

    public static void getSaludo() {
        try {
            URL url = new URL("http://localhost:8080/saludo?token=mi_token_123");
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");

            try (BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()))) {
                String line; 
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) sb.append(line);
                System.out.println("[REST] " + sb.toString());
            }

        } catch (Exception e) { 
            System.out.println("[RESTClient] Err: " + e.getMessage()); 
        }
    }

    public static List<String> getMensajes(String urlStr) {
        List<String> out = new ArrayList<>();
        try {
            URL url = new URL(urlStr);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");

            try (BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()))) {
                String json = br.readLine();
                if (json == null) return out;

                json = json.trim().replaceAll("^\\[|\\]$", "");
                if (json.isEmpty()) return out;

                String[] parts = json.split("\",\"");
                for (String p : parts) {
                    p = p.replaceAll("^\"|\"$", "");
                    out.add(p);
                }
            }

        } catch (Exception e) { 
            System.out.println("[RESTClient] GET err: " + e.getMessage()); 
        }

        return out;
    }

    public static void postMensaje(String urlStr, String mensaje) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("POST");
            c.setDoOutput(true);

            try (OutputStream os = c.getOutputStream()) {
                os.write(mensaje.getBytes("UTF-8"));
            }

            System.out.println("[RESTClient] POST code: " + c.getResponseCode());

        } catch (Exception e) { 
            System.out.println("[RESTClient] POST err: " + e.getMessage()); 
        }
    }
}
