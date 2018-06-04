package models.utils;

import java.util.Random;

public class Dice {

    private static Random rng = new Random();

    public static int roll(int sides){
        return sides <= 0 ? 0 : rng.nextInt(sides) + 1;
    }
}
