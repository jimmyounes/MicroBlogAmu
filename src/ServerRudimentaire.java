package TP5;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ServerRudimentaire {
    @SuppressWarnings("unused")
    public static void main(String[] args) throws IOException {

        Selector selector = Selector.open(); // selector is open here

        ServerSocketChannel crunchifySocket = ServerSocketChannel.open();


        InetSocketAddress crunchifyAddr = new InetSocketAddress("localhost", 1234);

        crunchifySocket.bind(crunchifyAddr);

        crunchifySocket.configureBlocking(false);

        int ops = crunchifySocket.validOps();

        SelectionKey selectKy = crunchifySocket.register(selector, ops, null);

        while (true) {

            log("I'm a server and I'm waiting for new connection and buffer select...");
            selector.select();

            Set<SelectionKey> crunchifyKeys = selector.selectedKeys();
            Iterator<SelectionKey> crunchifyIterator = crunchifyKeys.iterator();

            while (crunchifyIterator.hasNext()) {
                SelectionKey myKey = crunchifyIterator.next();

                if (myKey.isAcceptable()) {
                    SocketChannel crunchifyClient = crunchifySocket.accept();

                    crunchifyClient.configureBlocking(false);

                    crunchifyClient.register(selector, SelectionKey.OP_READ,new Countnumber());
                    log("Connection Accepted: " + crunchifyClient.getLocalAddress() + "\n");


                } else if (myKey.isReadable()) {

                    SocketChannel crunchifyClient = (SocketChannel) myKey.channel();

                    ByteBuffer crunchifyBuffer = ByteBuffer.allocate(256);
                    crunchifyClient.read(crunchifyBuffer);
                    String result = new String(crunchifyBuffer.array()).trim();

                    log("Message received: " + result);
                    if(myKey.attachment() instanceof Countnumber){
                        ((Countnumber) myKey.attachment()).number++;
                        System.out.println("nombre de lignes :"+((Countnumber) myKey.attachment()).number);
                    }
                    System.out.println(myKey.attachment());

                    if (result.equals("Crunchify.com")) {
                        crunchifyClient.close();
                        log("\nIt's time to close connection as we got last company name 'Crunchify'");
                        log("\nServer will keep running. Try running client again to establish new connection");
                    }
                }
                crunchifyIterator.remove();
            }
        }
    }

    private static void log(String str) {

        System.out.println(str);
    }
}
