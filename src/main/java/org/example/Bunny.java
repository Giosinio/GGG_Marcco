package org.example;

import java.util.Map;
import org.example.dto.enums.ObjectType;

public class Bunny {
    public int row;
    public int column;
    public Map<ObjectType, Integer> backpack;
    public int stamina;
    public int currentRound; //not specific to bunny, but we need this
    public boolean isFirstMarketOffer = true;


    public void updateLocation(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        this.stamina = stamina;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public Map<ObjectType, Integer> getBackpack() {
        return backpack;
    }

    public void setBackpack(Map<ObjectType, Integer> backpack) {
        this.backpack = backpack;
    }
}

