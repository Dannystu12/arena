package models.characters.enemies;

import models.utils.Dice;

public class Orc extends Enemy {
    public Orc(){
        super(Dice.roll(10) + 6, 11, 12);
    }
}
