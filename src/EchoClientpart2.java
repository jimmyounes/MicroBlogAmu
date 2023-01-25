import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class EchoClientpart2 {
    public static void main(String[] args) throws IOException, InterruptedException {

        InetSocketAddress crunchifyAddr = new InetSocketAddress("localhost", 1234);

        //  selectable channel for stream-oriented connecting sockets
        SocketChannel crunchifyClient = SocketChannel.open(crunchifyAddr);

        log("Connecting to Server on port 1234..");
        Scanner console = new Scanner(System.in);




        while (true) {
            System.out.println("Entrez votre texte");
            String string =console.nextLine();
            byte[] message = new String(string).getBytes();
            ByteBuffer buffer = ByteBuffer.wrap(message);
            crunchifyClient.write(buffer);
            if(string=="exit")break;
            buffer.clear();



        }

        crunchifyClient.close();
    }

    private static void log(String str) {

        System.out.println(str);
    }
}
