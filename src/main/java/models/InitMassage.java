package models;

import java.io.Serializable;

public class InitMassage implements Serializable {
    public InitMassage(String player, int turn) {
        this.player = player;
        this.turn = String.valueOf(turn);
    }
    public String player;
    public String turn;
}
