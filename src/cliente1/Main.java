package cliente1;

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
         g.setColor(miPelota.getColor());
         g.fillOval(Math.round(miPelota.getX()), Math.round(miPelota.getY()), Ctes.DIAMETRO, Ctes.DIAMETRO);
         for(Pelota pelotaTpPaint : pelotas){
             g.setColor(pelotaTpPaint.getColor());
             g.fillOval(Math.round(pelotaTpPaint.getX()), Math.round(pelotaTpPaint.getY()), Ctes.DIAMETRO, Ctes.DIAMETRO);
         }
     }

    public void cicloPrincipalJuego() throws Exception {  
        long tiempoViejo = System.nanoTime();
        while (true) {
            long tiempoNuevo = System.nanoTime();
            float dt = (tiempoNuevo - tiempoViejo) / 1000000000f;
            tiempoViejo = tiempoNuevo;
            miPelota.fisica(dt);
            dibuja();
            enviaPosiciones();
        }
    }

     private void dibuja() throws Exception {
        try{
         SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                paintImmediately(0, 0, Ctes.ANCHO, Ctes.ALTO);
            }
        });
        }catch(Exception e){
        } 
    }

    public void enviaPosiciones() throws Exception {
        DatagramSocket socket;
        DatagramPacket packet;

        InetAddress destination = null;
        byte datosEnviar[];
        System.out.println("[" + miPelota.getX() + "],[" + miPelota.getY() + "],[" + miPelota.getPuerto() + "]");
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
        ArrayList<Pelota> pelotasRespuesta = bytesParceArray(packet.getData());
        System.out.println(pelotasRespuesta.size());
        pelotas = pelotasRespuesta;
        socket.close();
    }


    public byte[] objParceBytes(Pelota pelota){
        byte[] bytes = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(pelota);
            objectOutputStream.close();
            bytes =  byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public ArrayList bytesParceArray(byte[] bytes){
        ArrayList<Pelota> arrayList = null;
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream;
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            arrayList = (ArrayList<Pelota>) objectInputStream.readObject();
            objectInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public static void main(String[] args) throws Exception {
        int puerto;

        if(args.length <= 0){
            System.out.print("Favor de especificar el puerto");
            return ;
        }
        puerto = Integer.parseInt(args[0]);


        JFrame jf = new JFrame("Cliente1...");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setResizable(false);
        Pelota pelota = new Pelota(puerto);
        Main demo1 = new Main(pelota);
        jf.getContentPane().add(demo1);
        //jf.addMouseListener(demo1);
        jf.pack();
        jf.setVisible(true);
        demo1.cicloPrincipalJuego();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        miPelota.setX((float) e.getX());
        miPelota.setY((float) e.getY());
        try {
            //enviaPosiciones();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

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
}