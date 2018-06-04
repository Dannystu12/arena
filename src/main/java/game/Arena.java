package game;

import engine.sprite.BufferedImageLoader;
import engine.Game;
import engine.sprite.SpriteSheet;

import java.awt.image.BufferedImage;

public class Arena {

    private Game game;

    public Arena(){
        game = new Game(800, 600, "Arena");
        game.getScreenFactory().showScreen(new ArenaScreen(game.getScreenFactory()));
    }

    public static void main(String[] args) {
        new Arena();
    }
}
