import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SuperServer {
    static ExecutorService executeIt = Executors.newFixedThreadPool(2);
    static String[] players =new String[] {"RED", "GREEN"};
    static ArrayList<Server> playersServers  = new ArrayList<>();
    static int countPlayers = 0;
    static int flag = 0;

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(3345);
        System.out.println("Server socket created, command console reader for listen to server commands");

        while (!server.isClosed()) {
            Socket client = server.accept();

            if(countPlayers < 2) {
                Server newPlayer =  new Server(client, players[countPlayers], 1 - countPlayers);
                playersServers.add(newPlayer);
                executeIt.execute(newPlayer);
                countPlayers++;
                System.out.print("Connection accepted.");
            }

            if(countPlayers == 2 && flag == 0) {
                flag++;
                playersServers.get(0).actionPlayerForServer.subscribe(turnMessage -> {
                    playersServers.get(1).actionOpponentPlayerForServer.onNext(turnMessage);
                });
                playersServers.get(1).actionPlayerForServer.subscribe(turnMessage -> {
                    playersServers.get(0).actionOpponentPlayerForServer.onNext(turnMessage);
                });
            }
        }

        executeIt.shutdown();
    }
}
