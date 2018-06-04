package engine.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardListener implements KeyListener{

    private boolean[] keys;

    public KeyboardListener(){
         keys = new boolean[256];
         for(int i = 0; i < 256; i++){
            keys[i] = false;
         }
    }

    @Override
    public void keyTyped(KeyEvent event) {

     }

    @Override
    public void keyPressed(KeyEvent event) {
        keys[event.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent event) {
        keys[event.getKeyCode()] = false;
    }

    public boolean isKeyPressed(int key){
        return keys[key];
    }

    public boolean isKeyReleased(int key){
        return !keys[key];
    }
}
