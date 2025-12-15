package m.servidorpr;

import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer implements Runnable {
    private int puerto;
    public TCPServer(int puerto) { this.puerto = puerto; }
    @Override
    public void run() {
        try (ServerSocket server = new ServerSocket(puerto)) {
            while (true) {
                Socket s = server.accept();
                System.out.println("[TCP] Cliente conectado: " + s.getRemoteSocketAddress());
                new Thread(new TCPWorker(s)).start();
            }
        } catch (Exception e) {
            System.out.println("[TCP] Error: " + e.getMessage());
        }
    }
}

