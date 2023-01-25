import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class ServerMulticlient {
    @SuppressWarnings("unused")

    public static void main(String[] args) throws IOException {
        HashMap<String,String> users=new HashMap<>();
        HashMap<String,String> publications=new HashMap<>();
        String currentuser="";
        chargerfichier("users.txt",users);
        chargerublications("publication",publications);
        boolean connectUser=false;

        Selector selector = Selector.open(); // selector is open here

        ServerSocketChannel crunchifySocket = ServerSocketChannel.open();


        InetSocketAddress crunchifyAddr = new InetSocketAddress("localhost", 1234);

        crunchifySocket.bind(crunchifyAddr);

        crunchifySocket.configureBlocking(false);

        int ops = crunchifySocket.validOps();


        SelectionKey selectKy = crunchifySocket.register(selector, ops, null);

        // Infinite loop..
        // Keep server running
        while (true) {

            selector.select();


            Set<SelectionKey> crunchifyKeys = selector.selectedKeys();
            Iterator<SelectionKey> crunchifyIterator = crunchifyKeys.iterator();

            while (crunchifyIterator.hasNext()) {
                SelectionKey myKey = crunchifyIterator.next();


                if (myKey.isAcceptable()) {
                    SocketChannel crunchifyClient = crunchifySocket.accept();

                    crunchifyClient.configureBlocking(false);
                    crunchifyClient.register(selector, SelectionKey.OP_READ);
                    log("Connection Accepted: " + crunchifyClient.getLocalAddress() + "\n");


                    String welcome ="Bienvenue dans le serveur...\n"+
                            "connexion ou inscirption..? \n"+
                            "pour se connecter :CONNECT user MotDePasse\n"+
                            "pour s'inscrire :SIGNUP user MotDePasse \n";

                    byte[] message = new String(welcome).getBytes();
                    ByteBuffer buffer = ByteBuffer.wrap(message);
                        crunchifyClient.write(buffer);


                } else if (myKey.isReadable()) {

                    SocketChannel crunchifyClient = (SocketChannel) myKey.channel();


                    ByteBuffer crunchifyBuffer = ByteBuffer.allocate(256);
                    crunchifyClient.read(crunchifyBuffer);
                    String result = new String(crunchifyBuffer.array()).trim();

                    String[] reponse=result.split(" ");

                    if(reponse[0].equals("CONNECT")){
                        if(connectUser==true)send(crunchifyClient,"Already connect");
                        else{
                            if(reponse.length!=3 && reponse[1].charAt(0)!='@')send(crunchifyClient,"Error d'arguments ");
                            else{
                                if(users.get(reponse[1]).equals(reponse[2])){
                                    send(crunchifyClient,"connexion réussie");
                                    connectUser=true;
                                    currentuser=reponse[1];
                                }
                                else {
                                    send(crunchifyClient,"connexion impossible réessayez ");
                                }
                            }
                        }
                    }
                    if(reponse[0].equals("SIGNUP")){
                          if(reponse.length!=3 && reponse[1].charAt(0)=='@')send(crunchifyClient,"Error d'arguments ");
                          else {

                              if(users.get(reponse[1])!=null)send(crunchifyClient,"Nom d'utilisateur déja pris réessayez ");
                              else {

                                  FileWriter fw = new FileWriter("users.txt", true);
                                  fw.write(reponse[1] + '\t' + reponse[2] +'\n');
                                  fw.close();
                                  users.put(reponse[1],reponse[2]);
                                  send(crunchifyClient,"inscription réussie");
                                  connectUser=true;
                                  currentuser=reponse[1];
                              }
                          }
                    }
                    if(reponse[0].equals("PUBLISH") && connectUser){
                        String content="";
                        for (int i=1;i<reponse.length;i++){
                            content=reponse[i]+content;
                        }
                        publications.put(reponse[0],content);
                        writeinfilepublication("publication",currentuser, content);
                        send(crunchifyClient,"Publication postée ");
                    }


                }
                crunchifyIterator.remove();
            }
        }
    }

    private static void log(String str) {

        System.out.println(str);
    }
    private static void send(SocketChannel crunchifyClient,String msg) throws IOException {
        byte[] message = new String(msg).getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(message);
        crunchifyClient.write(buffer);
    }
    private static void chargerfichier(String fichier,HashMap<String,String> list) throws IOException {
        BufferedReader reader=new BufferedReader(new FileReader(fichier));
        String line;
        while(null!=(line=reader.readLine ())){
            String[] token=line.split("\t");
            //System.out.println(token[0]);
            list.put(token[0],token[1]);
        }
    }
    private static void chargerublications(String fichier,HashMap<String,String> list) throws IOException {
        BufferedReader reader=new BufferedReader(new FileReader(fichier));
        String line;
        while(null!=(line=reader.readLine ())){
            String[] token=line.split(" ");
            String content="";
            for (int i=1;i< token.length;i++){
                content=token[i]+content;
            }
            list.put(token[0],content);
        }
    }
    private static void writeinfilepublication(String fichier,String user,String content) throws IOException {
        FileWriter fw = new FileWriter(fichier, true);
        fw.write(user + '\t' + content+'\n');
        fw.close();

    }
}

