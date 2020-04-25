package models;

import java.io.Serializable;

public class TurnMessage implements Serializable {
    public String idStick;
    public String resultTurn;

    public TurnMessage(int id, int result) {
        this.idStick = String.valueOf(id);
        this.resultTurn = String.valueOf(result);
    }
}
