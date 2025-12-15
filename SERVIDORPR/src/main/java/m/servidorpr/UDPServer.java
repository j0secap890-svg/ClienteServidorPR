package m.servidorpr;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServer implements Runnable {
    private int puerto;

    public UDPServer(int puerto) {
        this.puerto = puerto;
    }

    @Override
    public void run() {
        try (DatagramSocket socket = new DatagramSocket(puerto)) {
            System.out.println("[UDPServer] Escuchando en puerto " + puerto);

            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket p = new DatagramPacket(buffer, buffer.length);
                socket.receive(p);

                String msg = new String(p.getData(), 0, p.getLength());
                System.out.println("[UDPServer] Recibido: " + msg);

                // Guardar en BD
                MessageDAO.saveMessage("udp", msg);
            }

        } catch (Exception e) {
            System.out.println("[UDPServer] Error: " + e.getMessage());
        }
    }
}
