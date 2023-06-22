package org.example.dto.enums;

public enum ObjectType {
    LEAVES(3), CARROT(4), BEETS(2), FLOWER(2), WOOD(0), CLAY(0), ROCK(0), HAY(3), BUNNS(0);

    final Integer givenStamina;

    ObjectType(int givenStamina) {
        this.givenStamina = givenStamina;
    }

    public Integer getGivenStamina() {
        return givenStamina;
    }
}
