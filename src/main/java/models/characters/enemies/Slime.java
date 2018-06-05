package models.characters.enemies;

import models.utils.Dice;

public class Slime extends Enemy{
    public Slime(){
        super(Dice.roll(8) , 8, 6);
    }

}
