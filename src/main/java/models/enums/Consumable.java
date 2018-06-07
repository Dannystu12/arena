package models.enums;

import models.utils.Dice;

public enum Consumable {
    HEALTH_POTION(3,10,3);

    private int numDice;
    private int numSides;
    private int bonus;

    Consumable(int numDice, int numSides, int bonus){
        this.numDice = numDice;
        this.numSides = numSides;
        this.bonus = bonus;
    }

    public int getEffect(){
        int result = bonus;
        for(int i = 0; i < numDice; i++){
            result += Dice.roll(numSides);
        }
        return result;
    }
}
