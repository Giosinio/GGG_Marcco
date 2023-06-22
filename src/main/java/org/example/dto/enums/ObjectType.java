package org.example.dto.enums;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public enum ObjectType {
    leaves(3), carrot(4), beets(2), flower(2), wood(0), clay(0), rock(0), hay(3), bunns(0);

    final Integer givenStamina;

    ObjectType(int givenStamina) {
        this.givenStamina = givenStamina;
    }

    public Integer getGivenStamina() {
        return givenStamina;
    }

    public static Set<String> getKeys() {
        return Arrays.stream(ObjectType.values())
                .map(Object::toString)
                .collect(Collectors.toSet());
    }
}
