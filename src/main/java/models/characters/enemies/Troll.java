package models.characters.enemies;

import models.utils.Dice;

public class Troll extends Enemy {
    public Troll(){
        super(Dice.roll(6) + Dice.roll(6), 10, 6);
    }
}
