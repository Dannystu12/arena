package engine;

import javax.swing.*;
import java.awt.*;

public class GameThread extends JPanel implements Runnable {

    private final Game game;

    public GameThread(Game game){
        this.game = game;
        setFocusable(true); //Allows input
    }

    @Override
    public void run() {
        while(true){
            try{
                if(game.getScreenFactory().getCurrentScreen() != null){
                    game.getScreenFactory().getCurrentScreen().onUpdate();
                    Thread.sleep(10); // Sets fps
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        // Rendering hint is here to reduce lag
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if(game.getScreenFactory().getCurrentScreen() != null){
            game.getScreenFactory().getCurrentScreen().onDraw(g2d);
        }
        repaint();
    }
}
