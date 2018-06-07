package models.characters.enemies;

import models.utils.Dice;

public class Goblin extends Enemy {
    public Goblin(){
        super(Dice.roll(8) + 4, 9, 10);
    }
}
