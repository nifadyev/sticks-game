import models.InitMassage;
import models.TurnMessage;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Socket socket = new Socket("localhost", 3345);
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

        System.out.println("Client has connected to server");
        System.out.println("GameField has been created ");
        InitMassage initMassage = (InitMassage) ois.readObject();

        UIGame uiGame = new UIGame(initMassage.player, Integer.parseInt(initMassage.turn));
        uiGame.actionPlayerUI.subscribe(turnMessage -> {
            oos.writeObject(turnMessage);
            oos.flush();
            System.out.println("Client has sent message " + turnMessage.idStick + " " + turnMessage.resultTurn);
        });

        while(!socket.isOutputShutdown()){
            TurnMessage opponentTurn = (TurnMessage) ois.readObject();
            uiGame.actionOpponentUI.onNext(opponentTurn);
        }
    }
}