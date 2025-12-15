package m.clientepr;

public class ClienteMain {

    public static void main(String[] args) {

        System.out.println("=== Cliente Iniciando ===");

        // 1. Enviar mensaje TCP al servidor
        TCPClient.send("localhost",5001,"Hola servidor");

        // 2. Recibir mensajes UDP
        int puertoUDPCliente=5003;
        new Thread(new UDPClient(puertoUDPCliente)).start();
        System.out.println("[Main] Cliente UDP escuchando en puerto " + puertoUDPCliente);

        // 3. Consumir REST
        RESTClient.getSaludo();

        System.out.println("=== Cliente ejecutandose correctamente ===");
    }
}

