package cliente;

import constantes.Constantes;
import pelota.Ctes;
import pelota.Pelota;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class Main extends JComponent implements MouseListener{
    Pelota miPelota;
    ArrayList<Pelota> pelotas;
    
    public Main(Pelota pelota) throws InterruptedException {
        setPreferredSize(new Dimension(Ctes.ANCHO, Ctes.ALTO));
        this.miPelota = pelota;
        pelotas = new ArrayList<>();

    }

     public void paint(Graphics g) {
         g.setColor(Color.WHITE);
         g.fillRect(0, 0, Ctes.ANCHO, Ctes.ALTO);
         g.setColor(miPelota.getColor());
         g.fillOval(Math.round(miPelota.getX()), Math.round(miPelota.getY()), Ctes.DIAMETRO, Ctes.DIAMETRO);
         for(Pelota pelotaTpPaint : pelotas){
             g.setColor(pelotaTpPaint.getColor());
             g.fillOval(Math.round(pelotaTpPaint.getX()), Math.round(pelotaTpPaint.getY()), Ctes.DIAMETRO, Ctes.DIAMETRO);
         }
     }

    //Sin Fisica
    public void cicloPrincipalJuego() throws Exception {
        while (true) {
            enviaPosiciones();
            dibuja();
        }
    }

    //Con fisica
    /*public void cicloPrincipalJuego() throws Exception {
        long tiempoViejo = System.nanoTime();
        while (true) {
            long tiempoNuevo = System.nanoTime();
            float dt = (tiempoNuevo - tiempoViejo) / 1000000000f;
            tiempoViejo = tiempoNuevo;
            miPelota.fisica(dt);
            enviaPosiciones();
            dibuja();
        }
    }*/

     private void dibuja() throws Exception {
        try{
         SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                paintImmediately(0, 0, Ctes.ANCHO, Ctes.ALTO);
            }
        });
        }catch(Exception ignored){
        } 
    }

    public void enviaPosiciones() throws Exception {
        DatagramSocket socket;
        DatagramPacket packet;
        InetAddress destination = null;
        byte[] datosEnviar;
        datosEnviar = objParceBytes(miPelota);
        socket = new DatagramSocket(miPelota.getPuerto(), InetAddress.getByName(Constantes.HOST_DEL_CLIENTE));
        try {
            destination = InetAddress.getByName(Constantes.HOST_DEL_SERVIDOR);
        } catch (UnknownHostException uhe) {
            System.out.println("Host no encontrado: " + uhe);
        }
        packet = new DatagramPacket(datosEnviar, datosEnviar.length, destination, Constantes.PUERTO_DEL_SERVIDOR);
        socket.send(packet);
        System.out.println("Datos enviado.");

        //Recibe respuesta del server
        byte datosRecibir[] = new byte[1024];
        packet = new DatagramPacket(datosRecibir, datosRecibir.length);
        socket.receive(packet);
        ArrayList<Pelota> pelotasRespuesta;
        pelotasRespuesta = (ArrayList<Pelota>) bytesParceObj(packet.getData());
        pelotas = pelotasRespuesta;
        socket.close();
        ArrayList lol = new ArrayList();
        objParceBytes(lol);
    }


    public byte[] objParceBytes(Object object){
        byte[] bytes = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.close();
            bytes =  byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public Object bytesParceObj(byte[] bytes){
        Object object = null;
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream;
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            object = objectInputStream.readObject();
            objectInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return object;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        miPelota.setX((float) e.getX());
        miPelota.setY((float) e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public static void main(String[] args) throws Exception {
        int puerto;
        if(args.length <= 0){
            System.out.print("Favor de especificar el puerto");
            return ;
        }
        puerto = Integer.parseInt(args[0]);
        JFrame jFrame = new JFrame("Cliente: " + puerto);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setResizable(false);
        Pelota pelota = new Pelota(puerto);
        Main main = new Main(pelota);
        jFrame.getContentPane().add(main);
        jFrame.addMouseListener(main);
        jFrame.pack();
        jFrame.setVisible(true);
        main.cicloPrincipalJuego();
    }
}