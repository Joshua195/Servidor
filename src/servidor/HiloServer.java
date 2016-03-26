package servidor;

import pelota.Pelota;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Map;

public class HiloServer extends Thread{
    DatagramPacket datagramPacket;
    DatagramSocket datagramSocket;
    Map<Integer, Pelota> pelotasGlobales;


    public HiloServer(DatagramPacket datagramPacket, DatagramSocket datagramSocket, Map<Integer, Pelota> pelotas){
        this.datagramPacket = datagramPacket;
        this.datagramSocket = datagramSocket;
        pelotasGlobales = pelotas;
    }

    public void run(){
        System.out.println("Ha llegado una peticion");
        Pelota pelotaUser = (Pelota) bytesParceObj(datagramPacket.getData());
        pelotaNueva(pelotaUser);
        byte datosEnviar[] = datosEnviar(pelotaUser);
        DatagramPacket packet = new DatagramPacket(datosEnviar, datosEnviar.length, datagramPacket.getAddress(),datagramPacket.getPort());
        try {
            datagramSocket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Peticion Servida");
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

    public void pelotaNueva(Pelota pelota){
        Pelota pelotaExistente = pelotasGlobales.get(pelota.getPuerto());

        if(pelotaExistente == null){
            pelotasGlobales.put(pelota.getPuerto(), pelota);
        }else{
            pelotaExistente.setX(pelota.getX());
            pelotaExistente.setY(pelota.getY());
        }
    }

    public byte[] datosEnviar(Pelota pelota){
        ArrayList<Pelota> pelotasEnviar = new ArrayList<>(pelotasGlobales.values());
        for (int i = 0; i > pelotasEnviar.size(); i++){
            if (pelotasEnviar.get(i).equals(pelota)){
                pelotasEnviar.remove(pelotasEnviar.get(i));
                break;
            }
        }

        return objParceBytes(pelotasEnviar);
        /*for(Pelota remover : pelotasEnviar){
            if(remover.equals(pelotaUser)){
                pelotasEnviar.remove(remover);
                break;
            }
        }*/
    }
}
