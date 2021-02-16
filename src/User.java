import java.net.Socket;
import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;

class LogUser {

    LogUser(){

    }

    public SocketPerso login (ArrayList<String> args) {
        SocketPerso socket_client = null;
        try {
            String url = "jdbc:msql://localhost/serveur_db";
            Connection conn = DriverManager.getConnection(url,"ServerMaster","Master2004$");
            Statement stmt = conn.createStatement();
            ResultSet rs;

            rs = stmt.executeQuery(
                    "SELECT pseudo, dt_last_connection " +
                            "FROM user WHERE password ="+ args.get(1) + " AND pseudo="+ args.get(0));
            while ( rs.next() ) {
                String pseudo = rs.getString("pseudo");
                System.out.println(pseudo);

                //INSTANCIER CLIENT ICI
            }
            User currentUser = new User(rs.getString("pseudo"));
            socket_client = new SocketPerso(new Socket("127.0.0.1",5000));
            Socket_Serveur.users.put(currentUser, socket_client.getSocket());
            conn.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return socket_client;
    }
}

public class User {

    User(){}
    User(String username){
        _username = username;
        LocalTime time = LocalTime.now();
        _lastConnection = time;
    }

    public String _username;
    public LocalTime _lastConnection;


}
