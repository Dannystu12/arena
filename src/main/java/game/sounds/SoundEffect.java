package game.sounds;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public enum SoundEffect {

    SLIME_HIT("/sounds/slime_hit.wav"),
    HERO_MISS("/sounds/hero_miss.wav"),
    ENEMY_HIT_PLAYER("/sounds/enemy_hit_player.wav"),
    SLIME_MISS("/sounds/slime_miss.wav"),
    BAT_HIT("/sounds/bat_hit.wav"),
    GOBLIN_HIT("/sounds/goblin_hit.wav"),
    ORC_HIT("/sounds/orc_hit.wav"),
    RAT_HIT("/sounds/rat_hit.wav"),
    GOBLIN_MISS("/sounds/goblin_miss.wav"),
    SPIDER_MISS("/sounds/spider_miss.wav"),
    BAT_MISS("/sounds/bat_miss.wav"),
    RAT_MISS("/sounds/rat_miss.wav"),
    TROLL_MISS("/sounds/troll_miss.wav"),
    ORC_MISS("/sounds/orc_miss.wav"),
    SPIDER_HIT("/sounds/spider_hit.wav"),
    TROLL_HIT("/sounds/troll_hit.wav"),
    PUFF_OF_SMOKE("/sounds/puff_of_smoke.wav"),
    TAKE_POTION("/sounds/potion.wav"),
    LAUGH("/sounds/announcer/laugh.wav"),
    FIGHT("/sounds/announcer/fight.wav");


    // Each sound effect has its own clip, loaded with its own sound file.
    private Clip clip;

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
            clip.start();
    }


    public static void init() {
        values();
    }
}


