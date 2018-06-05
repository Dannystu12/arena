package game.environments;

import game.sprites.Collidable;

import java.awt.*;

public class Wall extends Rectangle implements Collidable {

    public Wall(int x, int y, int width, int height){
        super(x ,y , width, height);
    }

    @Override
    public Rectangle getBounds(){
        return this;
    }
}
