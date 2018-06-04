package models.characters.enemies;

import models.utils.Dice;

public class Goblin extends Enemy {
    public Goblin(){
        super(Dice.roll(6) + Dice.roll(6), 10, 6);
    }
}
