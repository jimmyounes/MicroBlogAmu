import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Scanner;

public class Echoclient {
    public static void main(String[] args) throws IOException, InterruptedException {
        boolean connexion = false;

        InetSocketAddress crunchifyAddr = new InetSocketAddress("localhost", 1234);

        //  selectable channel for stream-oriented connecting sockets
        SocketChannel crunchifyClient = SocketChannel.open(crunchifyAddr);

        log("Connecting to Server on port 1234..");

        Scanner console = new Scanner(System.in);
//////////////////////////////////////////////////////////////
        ByteBuffer bufferIn = ByteBuffer.allocate(1024);
        int bytesIn = 0;
        bytesIn = crunchifyClient.read(bufferIn);

        if (bytesIn == -1) {
            throw new IOException("Socket closed");
        }
        if (bytesIn > 0) {
            bufferIn.flip();
            bufferIn.mark();


            while (bufferIn.hasRemaining()) {
                System.out.print((char) bufferIn.get());
            }
            System.out.println();
            bufferIn.compact();

            while (true) {

                System.out.println("Entrez votre réponse ");
                String string = console.nextLine();
                byte[] message = new String(string).getBytes();
                ByteBuffer buffer = ByteBuffer.wrap(message);
                crunchifyClient.write(buffer);
                if (string == "exit") break;
               String[]  instruction=string.split(" ");
               if(instruction[0].equals("SIGNUP")  || instruction[0].equals("CONNECT") || instruction[0].equals("PUBLISH")) read(crunchifyClient);

                buffer.clear();


            }

            // clsose(): Closes this channel.
            // If the channel has already been closed then this method returns immediately.
            // Otherwise it marks the channel as closed and then invokes the implCloseChannel method in order to complete the close operation.
            crunchifyClient.close();
        }


    }

    private static void log(String str) {

        System.out.println(str);
    }

    private static void read(SocketChannel crunchifyClient) throws IOException {
        ByteBuffer bufferIn = ByteBuffer.allocate(1024);
        int bytesIn = 0;

        bytesIn = crunchifyClient.read(bufferIn);

        if (bytesIn == -1) {

            throw new IOException("Socket closed");
        }
        if (bytesIn > 0) {

            bufferIn.flip();
            bufferIn.mark();


            while (bufferIn.hasRemaining()) {
                System.out.print((char) bufferIn.get());

            }
            System.out.println();
            bufferIn.compact();
        }
    }
}