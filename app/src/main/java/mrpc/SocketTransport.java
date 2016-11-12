package mrpc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;

/**
 * Created by Vector on 11/12/2016.
 */

public class SocketTransport extends TransportThread {

    int localPort;
    int remote_port;
    DatagramSocket socket;
    byte[] buffer = new byte[4096];
    SocketAddress broadcast;
    public SocketTransport(int local_port) throws SocketException {
        this.localPort = local_port;
        this.remote_port = local_port;
        socket = new DatagramSocket(local_port);
        broadcast = new InetSocketAddress("192.168.1.255", remote_port);
        //SocketAddress addr = new InetSocketAddress("0.0.0.0", local_port);
        //socket.bind(addr);
    }
    @Override
    public String poll() {
        DatagramPacket pkt = new DatagramPacket(buffer, buffer.length);
        try {
            socket.receive(pkt);
            return new String(buffer, "ASCII");
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public Boolean send(String message) {
        byte[] messageBytes;
        try {
            messageBytes = message.getBytes("ASCII");
            DatagramPacket pkt = new DatagramPacket(messageBytes, messageBytes.length, broadcast);
            socket.send(pkt);
            return true;
        } catch (UnsupportedEncodingException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }
}