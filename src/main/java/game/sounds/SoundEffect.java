package game.sounds;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;

public enum SoundEffect {

    HERO_HIT("/sounds/hero_hit.wav"),
    HERO_MISS("/sounds/hero_miss.wav"),
    SLIME_HIT("/sounds/slime_hit.wav"),
    SLIME_MISS("/sounds/slime_miss.wav");


    // Each sound effect has its own clip, loaded with its own sound file.
    private Clip clip;
    private final float VOLUME = 1f;

    // Constructor to construct each element of the enum with its own sound file.
    SoundEffect(String soundFileName) {
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
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * VOLUME) + gainControl.getMinimum();
            gainControl.setValue(gain);
            clip.start();
    }


    public static void init() {
        values();
    }
}


