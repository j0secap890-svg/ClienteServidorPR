package m.clientepr;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPClient implements Runnable {
    private int puerto;
    public UDPClient(int puerto) { this.puerto = puerto; }
    @Override
    public void run() {
        try (DatagramSocket s = new DatagramSocket(puerto)) {
            System.out.println("[UDPClient] Escuchando en " + puerto);
            byte[] buf = new byte[1024];
            while (true) {
                DatagramPacket p = new DatagramPacket(buf, buf.length);
                s.receive(p);
                String msg = new String(p.getData(), 0, p.getLength());
                System.out.println("[UDPClient] Recibido: " + msg);
            }
        } catch (Exception e) {
            System.out.println("[UDPClient] Err: " + e.getMessage());
        }
    }
}
