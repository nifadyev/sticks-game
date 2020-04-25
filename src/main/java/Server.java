import io.reactivex.subjects.PublishSubject;
import models.InitMassage;
import models.TurnMessage;

import java.io.*;
import java.net.Socket;

public class Server implements Runnable {
    private static Socket clientDialog;
    private static String player;
    private static int turn;
    private static InitMassage initMassage;

    public PublishSubject<TurnMessage> actionPlayerForServer = PublishSubject.create();
    public PublishSubject<TurnMessage> actionOpponentPlayerForServer = PublishSubject.create();

    public Server(Socket client, String player, int turn) {
        Server.clientDialog = client;
        Server.player = player;
        Server.turn = turn;
        Server.initMassage = new InitMassage(player, turn);
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(clientDialog.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientDialog.getInputStream());

            actionOpponentPlayerForServer.subscribe(turnMessage -> {
                out.writeObject(turnMessage);
                out.flush();
            });
            out.writeObject(Server.initMassage);
            out.flush();

            while (!clientDialog.isClosed()) {
                TurnMessage turnMessage = (TurnMessage) in.readObject();
                System.out.println("Received from client - " + turnMessage.idStick+ " , " + turnMessage.resultTurn);
                actionPlayerForServer.onNext(turnMessage);
            }

            System.out.println("Client disconnected");
            System.out.println("Closing connections & channels.");

            in.close();
            out.close();

            clientDialog.close();

            System.out.println("Connections & channels closed.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
