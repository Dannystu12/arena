package models.characters.enemies;

import models.utils.Dice;

public class Spider extends Enemy {
    public Spider(){
        super(Dice.roll(6) + 2, 5, 6);
    }
}
