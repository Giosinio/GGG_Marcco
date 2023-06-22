package org.example.dto.enums;

public enum ObjectType {
    leaves(3), carrot(4), beets(2), flower(2), wood(0), clay(0), rock(0), hay(3), bunns(0);

    final Integer givenStamina;

    ObjectType(int givenStamina) {
        this.givenStamina = givenStamina;
    }

    public Integer getGivenStamina() {
        return givenStamina;
    }
}
