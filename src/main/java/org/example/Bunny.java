package org.example;

import java.util.Map;
import org.example.dto.enums.ObjectType;

public class Bunny {
    public int row;
    public int column;
    public Map<ObjectType, Integer> backpack;
    public int stamina;


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
}
