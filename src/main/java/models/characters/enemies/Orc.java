package models.characters.enemies;

import models.utils.Dice;

public class Orc extends Enemy {
    public Orc(){
        super(Dice.roll(6) + Dice.roll(6), 10, 6);
    }
}
