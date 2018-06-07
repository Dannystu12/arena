package game.sounds;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public enum Announcer {
    FIGHT("/sounds/announcer/fatality.wav"),
    FLAWLESS("/sounds/announcer/flawless.wav"),
    VICTORY("/sounds/announcer/victory.wav"),
    LAUGH("/sounds/announcer/laugh.wav"),
    WELL_DONE("/sounds/announcer/well_done.wav"),
    OUTSTANDING("/sounds/announcer/outstanding.wav"),
    IMPRESSIVE("/sounds/announcer/impressive.wav"),
    EXCELLENT("/sounds/announcer/excellent.wav"),
    BRUTALITY("/sounds/announcer/brutality.wav");


    // Each sound effect has its own clip, loaded with its own sound file.
    private Clip clip;

    // Constructor to construct each element of the enum with its own sound file.
    Announcer(String soundFileName) {
        try {
            URL url = this.getClass().getResource(soundFileName);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip.isRunning())
            clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }


    public static void init() {
        values();
    }
}
