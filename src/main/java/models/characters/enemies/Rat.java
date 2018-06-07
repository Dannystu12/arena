package models.characters.enemies;

import models.utils.Dice;

public class Rat extends Enemy {
    public Rat(){
        super(Dice.roll(6) + Dice.roll(6), 10, 6);
    }
}
