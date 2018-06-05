package game.sprites;

import engine.Screen;
import game.ArenaScreen;

import java.awt.*;

public class DamagePopup {

    private int x;
    private int y;
    private int value;
    private final int MOVE_AMOUNT = 1;
    private long initTime;
    private int duration = 750;
    private boolean isComplete = false;
    private static final Font font = new Font("Helvetica", Font.BOLD, 12);

    public DamagePopup(int value, int x, int y){
        this.x = x;
        this.y = y;
        this.initTime = System.currentTimeMillis();
        this.value = value;
    }


    public void onUpdate(){
        y -= MOVE_AMOUNT;
        if(System.currentTimeMillis() - initTime >= duration){
            isComplete = true;
        }
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void onDraw(Graphics2D g2d) {
        if(value <= 0){
            g2d.setColor(Color.decode("#34438e"));
        } else {
            g2d.setColor(Color.decode("#8c1d04"));
        }

        g2d.setFont(font);
        g2d.drawString(Integer.toString(value), x, y);
    }


}
