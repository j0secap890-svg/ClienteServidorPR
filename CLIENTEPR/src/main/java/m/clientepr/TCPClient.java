package m.clientepr;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPClient {

    public static void send(String host, int port, String msg) {
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println(msg);  
            String respuesta = in.readLine();  
            System.out.println("[TCPClient] Respuesta del servidor: " + respuesta);

        } catch (Exception e) {
            System.out.println("[TCPClient] Error: " + e.getMessage());
        }
    }
}

