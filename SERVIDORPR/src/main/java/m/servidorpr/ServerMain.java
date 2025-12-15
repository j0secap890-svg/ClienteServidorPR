package m.servidorpr;

public class ServerMain {

    public static void main(String[] args) {
        System.out.println("=== Servidor Iniciado ===");

        // TCP
        int puertoTCP = 5001;
        TCPServer tcpServer = new TCPServer(puertoTCP);
        new Thread(tcpServer).start();
        System.out.println("[Main] TCP en " + puertoTCP);

        // UDP
        int puertoUDP=5002;
        UDPServer udpServer = new UDPServer(puertoUDP);
        new Thread(udpServer).start();
        System.out.println("[Main] UDP en "+puertoUDP);

        // REST
        RestServer rest = new RestServer(8080);
        rest.start();
        System.out.println("[Main] REST en http://localhost:8080");

        // Guardar arranque en BD (opcional)
        MessageDAO.saveMessage("servidor","Servidor arrancado");

        System.out.println("=== Servicios en ejecucion ===");
    }
}
