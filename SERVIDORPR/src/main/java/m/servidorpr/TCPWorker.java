package m.servidorpr;

import java.io.*;
import java.net.Socket;

public class TCPWorker implements Runnable {
    private Socket socket;

    public TCPWorker(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("[TCPWorker] Cliente atendido.");

        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String mensaje;

            // 🔵 Comunicación simultánea básica:
            while ((mensaje = in.readLine()) != null) {
                System.out.println("[TCPWorker] Mensaje recibido: " + mensaje);

                // guardar en BD
                MessageDAO.saveMessage("cliente", mensaje);

                // respuesta del servidor
                out.println("Servidor recibió: " + mensaje);
            }

        } catch (Exception e) {
            System.out.println("[TCPWorker] Error: " + e.getMessage());
        }

        System.out.println("[TCPWorker] Cliente desconectado.");
    }
}
