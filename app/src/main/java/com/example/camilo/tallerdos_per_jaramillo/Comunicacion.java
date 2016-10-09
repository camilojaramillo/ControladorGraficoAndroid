package com.example.camilo.tallerdos_per_jaramillo;

/**
 * Created by CAMILO on 02/10/2016.
 */
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Observable;

import mensaje.Mensaje;

public class Comunicacion extends Observable implements Runnable {


    private static final String TAG = "Comunicacion";
    private static Comunicacion ref;
    public static final String DEFAULT_ADDRESS = "10.0.2.2";
    public static final String MULTI_GROUP_ADDRESS = "224.20.21.22";
    public static final int DEFAULT_PORT = 5000;

    private MulticastSocket ms;
    private boolean running;
    private boolean connecting;
    private boolean reset;
    private boolean errorNotified;

    private Comunicacion() {

        running = true;
        connecting = true;
        reset = false;
        errorNotified = false;
    }

    public static Comunicacion getInstance() {
        if (ref == null) {
            ref = new Comunicacion();
            Thread runner = new Thread(ref);
            runner.start();
        }

        return ref;
    }


    @Override
    public void run() {
        while (running) {
            if (connecting) {
                if (reset) {
                    if (ms != null) {
                        ms.close();
                    }
                    reset = false;
                }
                connecting = !attemptConnection();
            } else {
                if (ms != null) {
                    DatagramPacket p = receiveMessage();

                    // Validate that there are no errors with the data
                    if (p != null) {
                        // Transform packet bytes to understandable data
                        String message = new String(p.getData(), 0, p.getLength());

                        // Notify the observers that new data has arrived and pass the data to them
                        setChanged();
                        notifyObservers(message);
                        clearChanged();
                    }
                }
            }
        }
        ms.close();

    }


    private boolean attemptConnection() {
        try {
            ms = new MulticastSocket();
            setChanged();
            notifyObservers("Connection started");
            clearChanged();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            if (!errorNotified) {
                setChanged();
                notifyObservers("Connection failed");
                clearChanged();
                errorNotified = true;
            }
            return false;
        }
    }


    public void sendMessage(final Mensaje message, final String destAddress, final int destPort) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (ms != null) {

                    try {
                        // Validate destAddress
                        InetAddress ia = InetAddress.getByName(destAddress);
                        byte[] data = serializar(message);
                        DatagramPacket packet = new DatagramPacket(data, data.length, ia, destPort);

                        System.out.println("Sending data to " + ia.getHostAddress() + ":" + destPort);
                        ms.send(packet);
                        System.out.println("Data was sent");

                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    setChanged();
                    notifyObservers("Not connected");
                    clearChanged();
                }
            }
        }).start();

    }

    public DatagramPacket receiveMessage() {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        try {
            ms.receive(packet);
            System.out.println("Data received from " + packet.getAddress() + ":" + packet.getPort());
            return packet;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private byte[] serializar(Object data) {
        byte[] bytes = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(data);
            bytes = baos.toByteArray();

            // Close streams
            oos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }
}
