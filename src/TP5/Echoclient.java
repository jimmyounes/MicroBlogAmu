package TP5;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

public class Echoclient {
    public static void main(String[] args) throws IOException, InterruptedException {
         int n= Integer.parseInt(args[0]);
        InetSocketAddress crunchifyAddr = new InetSocketAddress("localhost", 1234);

        //  selectable channel for stream-oriented connecting sockets
        SocketChannel crunchifyClient = SocketChannel.open(crunchifyAddr);

        log("Connecting to Server on port 1234..");





        for (int i=0;i<n;i++) {

            byte[] message = new String("client numéro :"+i).getBytes();
            ByteBuffer buffer = ByteBuffer.wrap(message);
            crunchifyClient.write(buffer);

            log("sending: " + "client numéro :"+i);
            buffer.clear();



        }

        // close(): Closes this channel.
        // If the channel has already been closed then this method returns immediately.
        // Otherwise it marks the channel as closed and then invokes the implCloseChannel method in order to complete the close operation.
        crunchifyClient.close();
    }

    private static void log(String str) {

        System.out.println(str);
    }
}
