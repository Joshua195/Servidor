package servidor;

import constantes.Constantes;
import pelota.Pelota;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Map;

public class servidor_udp {
    public static void main(String[] args) throws IOException {
        Map<Integer, Pelota> pelotas = new HashMap<>();
        DatagramSocket s = new DatagramSocket(Constantes.PUERTO_DEL_SERVIDOR);
        System.out.println("Servidor activo.");
        while(true){
            DatagramPacket recibido = new DatagramPacket(new byte[1024], 1024);
            System.out.println("Esperando...");
            s.receive(recibido);
            HiloServer hiloServer = new HiloServer(recibido,s,pelotas);
            hiloServer.start();
        }
    }
}
