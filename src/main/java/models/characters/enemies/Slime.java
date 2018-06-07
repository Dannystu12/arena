package models.characters.enemies;

import models.utils.Dice;

public class Slime extends Enemy{
    public Slime(){
        super(Dice.roll(6) + 4 , 7, 8);
    }

}
