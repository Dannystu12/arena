package models.characters.enemies;

import models.utils.Dice;

public class Bat extends Enemy {
    public Bat(){
        super(Dice.roll(6) + Dice.roll(6), 10, 6);
    }
}
