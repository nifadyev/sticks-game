package models;

import java.awt.*;
import java.util.ArrayList;

public class StickStore {
    private int count;
    private ArrayList<Stick> sticks ;

    public StickStore() {
        this.sticks = new ArrayList<Stick>();
        this.count = 0;
    }

    public int addStick(int x, int y, int width, int height) {
        Stick newStick = new Stick(x, y, width, height, count);
        this.sticks.add(newStick);
        this.count++;
        return this.count - 1;
    }

    public void draw(Graphics g) {
        for (Stick stick : sticks) {
            g.setColor(stick.color);
            g.fillRect(stick.x, stick.y, stick.width,
                    stick.height);
        }
    }

    public ArrayList<Stick> getSticks() {
        return this.sticks;
    }
}
