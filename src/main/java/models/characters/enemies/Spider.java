package models.characters.enemies;

import models.utils.Dice;

public class Spider extends Enemy {
    public Spider(){
        super(Dice.roll(6) + Dice.roll(6), 10, 6);
    }
}
