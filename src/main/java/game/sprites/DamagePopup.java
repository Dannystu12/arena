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
    private static Font font;
    private static Font critFont;
    private boolean isCrit;

    public DamagePopup(int value, int x, int y, boolean isCrit){
        this.x = x;
        this.y = y;
        this.initTime = System.currentTimeMillis();
        this.value = value;
        this.isCrit = isCrit;
    }

    public static void setFont(Font newFont){
        font = newFont;
    }

    public static void setCritFont(Font newFont){
        critFont = newFont;
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
        g2d.setFont(font);

        if(isCrit) {
            g2d.setColor(Color.decode("#9d0da8"));
            g2d.setFont(critFont);
        }else if(value <= 0){
            g2d.setColor(Color.decode("#34438e"));
        } else {
            g2d.setColor(Color.decode("#8c1d04"));
        }

        g2d.drawString(Integer.toString(value), x, y);
    }


}
