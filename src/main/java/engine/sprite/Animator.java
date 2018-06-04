package engine.sprite;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Animator {
    private ArrayList<BufferedImage> frames;
    private BufferedImage sprite;
    private volatile boolean running = false;
    private long previousTime, speed;
    private int frameAtPause, currentFrame;
    private boolean loop;

    public Animator(ArrayList<BufferedImage> frames, long speed, boolean loop){
        this.frames = frames;
        this.speed = speed;
        this.loop = loop;
        sprite = frames.get(currentFrame);
    }

    public void setSpeed(long speed){
        this.speed = speed;
    }

    public void update(long time){
        if(running){
            if(time - previousTime >= speed){
                currentFrame++;
                try{
                    sprite = frames.get(currentFrame);
                }catch (IndexOutOfBoundsException e){
                    if(loop){
                        currentFrame = 0;
                        sprite = frames.get(currentFrame);
                    } else {
                        stop();
                    }
                }
                previousTime = time;
            }
        }
    }

    public void start(){
        previousTime = 0;
        frameAtPause = 0;
        currentFrame = 0;
        running = true;
    }

    public void stop(){
        previousTime = 0;
        frameAtPause = 0;
        currentFrame = 0;
        running = false;
    }

    public void pause(){
        frameAtPause = currentFrame;
        running = false;

    }

    public void resume(){
        currentFrame = frameAtPause;
        running = true;
    }

    public BufferedImage getSprite(){
        return sprite;
    }

    public boolean isRunning(){
        return running;
    }
}
