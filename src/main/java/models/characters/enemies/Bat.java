package models.characters.enemies;

import models.utils.Dice;

public class Bat extends Enemy {
    public Bat(){
        super(Dice.roll(4) + 2, 3, 4);
    }
}
