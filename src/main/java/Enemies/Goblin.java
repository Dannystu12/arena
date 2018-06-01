package Enemies;

import utils.Dice;

public class Goblin extends Enemy {
    public Goblin(){
        super(Dice.roll(6) + Dice.roll(6), 10, 6);
    }
}
