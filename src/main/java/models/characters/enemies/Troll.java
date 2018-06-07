package models.characters.enemies;

import models.utils.Dice;

public class Troll extends Enemy {
    public Troll(){
        super(Dice.roll(12) + 6, 13, 20);
    }
}
