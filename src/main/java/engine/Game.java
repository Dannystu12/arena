package engine;

import engine.input.KeyboardListener;
import engine.input.MousepadListener;

import javax.swing.*;

public class Game {

    private final JFrame window;
    private final ScreenFactory screenFactory;
    private final GameThread gameThread;
    private final KeyboardListener keyboardListener;
    private final MousepadListener mousepadListener;

    public Game(int windowX, int windowY, String title){
        window = new JFrame();
        window.setSize(windowX, windowY);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setFocusable(true); //Allows frame to get input
        window.setLocationRelativeTo(null); //Center frame in screen
        window.setTitle(title);
        screenFactory = new ScreenFactory(this);
        gameThread = new GameThread(this);
        window.add(gameThread);

        // Register keypresses
        keyboardListener = new KeyboardListener();
        window.addKeyListener(keyboardListener);

        // Register clicks
        mousepadListener = new MousepadListener();
        window.addMouseListener(mousepadListener);

        new Thread(gameThread).start();
        window.setVisible(true);
    }

    public JFrame getWindow(){
        return window;
    }

    public ScreenFactory getScreenFactory() {
        return screenFactory;
    }

    public KeyboardListener getKeyboardListener(){
        return keyboardListener;
    }

    public MousepadListener getMouseListener(){
        return mousepadListener;
    }
}
