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
        System.out.println("Ha llegado una peticion \n");
        System.out.println("Procedente de: " + datagramPacket.getAddress());
        System.out.println("En el puerto: " + datagramPacket.getPort());
        Pelota pelotaUser = bytesParceObj(datagramPacket.getData());
        System.out.println("[" + pelotaUser.getX() + "],[" + pelotaUser.getY() + "],[" + pelotaUser.getPuerto() + "]");

        Pelota pelotaExistente = pelotasGlobales.get(pelotaUser.getPuerto());
        if(pelotaExistente==null){
            pelotasGlobales.put(pelotaUser.getPuerto(), pelotaUser);
        }else{
            pelotaExistente.setX(pelotaUser.getX());
            pelotaExistente.setY(pelotaUser.getY());
        }

        ArrayList<Pelota> pelotasEnviar = new ArrayList<Pelota>(pelotasGlobales.values());
        for(Pelota remover : pelotasEnviar){
            if(remover.equals(pelotaUser)){
                pelotasEnviar.remove(remover);
                break;
            }
        }


        System.out.println(pelotasEnviar.size());
        //Pelota pelota1 = new Pelota(pelota.getX()+100, pelota.getY()+100,5559);
        //pelotasEnviar.add(pelota1);
        /*for (int i = 0; i < pelotasEnviar.size(); i++){
            System.out.println("[" + pelotasEnviar.get(i).getX() + "],[" + pelotasEnviar.get(i).getY() + "],[" + pelotasEnviar.get(i).getPuerto() + "]");
        }*/
        byte datosEnviar[] = arrayParceBytes(pelotasEnviar);
        DatagramPacket packet = new DatagramPacket(datosEnviar, datosEnviar.length, datagramPacket.getAddress(),datagramPacket.getPort());
        try {
            datagramSocket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Pelota bytesParceObj(byte[] bytes){
        Pelota pelota = null;
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = null;
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            pelota = (Pelota) objectInputStream.readObject();
            objectInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return pelota;
    }

    public byte[] arrayParceBytes(ArrayList<Pelota> arrayList){
        byte[] bytes = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(arrayList);
            objectOutputStream.close();
            bytes =  byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }
}
