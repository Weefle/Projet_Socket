import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Principale {


    public static void main(String[] args) throws IOException {

        SocketPerso socket_client = null;
        IOCommandes commandes = new IOCommandes(new BufferedReader(new InputStreamReader(System.in)), System.out);
        String msg;

        ArrayList<String> userInfo = new ArrayList<>();
        ArrayList<Socket> _clientList = new ArrayList<>();



            msg = commandes.lireEcran();
            if(msg.equals("client")) {
                //FAIS SI LE LOGIN EST OK
                //SocketPerso socket_client = new SocketPerso(new Socket("127.0.0.1",5000));

                //LOGIN
                System.out.println("Nom d'utilisateur : ");
                msg = commandes.lireEcran();
                userInfo.add(msg);
                System.out.println("Mot de passe : ");
                msg = commandes.lireEcran();
                userInfo.add(msg);
                try{
                    LogUser log= new LogUser();
                    socket_client = log.login(userInfo);
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                //FIN LOGIN

                SocketPerso.Thread_Client_Receive receiver = new SocketPerso.Thread_Client_Receive(socket_client);

                receiver.start();

                SocketPerso.Thread_Client_Send sender = new SocketPerso.Thread_Client_Send(socket_client);

                sender.start();


            }else if(msg.equals("serveur")) {
                Socket_Serveur socket_serveur = new Socket_Serveur(new ServerSocket(5000));
                while (!socket_serveur.getServer().isClosed()) {
                    InputStream inputStream = null;
                    try{
                        //REVOIR OBJET USER AU ACCEPT
                            inputStream = socket_serveur.acceptClient().getInputStream();

                            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

                             //RECEPTION DE L'OBJET HASHMAP AVEC LE SOCKET ET LE USER
                            var o_user = (Object) objectInputStream.readObject();
                            Socket_Serveur.users.add((HashMap<Socket, User>) o_user);
                            System.out.println("Received [" + ((HashMap<?, ?>) o_user).size() + "] messages from: " + inputStream);
                            Socket client = socket_serveur.acceptClient();
                            ClientServiceThread cliThread = new ClientServiceThread(client, socket_serveur);
                            cliThread.start();
                        }catch(Exception  ex){
                            ex.printStackTrace();
                        }

                }

            }
    }
}


