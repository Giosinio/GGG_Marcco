package org.example.dto.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.example.Bunny;

public enum ConstructionType {
    BURROW(5, buildCostsMap(1, 0, 0, 0, 0), 20),
    HUT(3, buildCostsMap(0, 2, 3, 0, 0), 50),
    FENCE(1, buildCostsMap(0, 0, 0, 2, 0), 10),
    DWELLING(7, buildCostsMap(2, 2, 2, 2, 0), 200),
    VILLA(9, buildCostsMap(2, 3, 2, 4, 1), 500);
    public final int stamina;
    public final Map<ObjectType, Integer> constructionCost;
    public final int score;

    ConstructionType(int stamina, Map<ObjectType, Integer> constructionCost, int score) {
        this.stamina = stamina;
        this.constructionCost = constructionCost;
        this.score = score;
    }

    private static Map<ObjectType, Integer> buildCostsMap(int clay, int hay, int rocks, int wood, int flowers) {
        Map<ObjectType, Integer> constructionCost = new HashMap<>();
        constructionCost.put(ObjectType.clay, clay);
        constructionCost.put(ObjectType.hay, hay);
        constructionCost.put(ObjectType.rock, rocks);
        constructionCost.put(ObjectType.wood, wood);
        constructionCost.put(ObjectType.flower, flowers);

        return constructionCost;
    }

    public static boolean checkIfWeHaveEnoughMaterials(ConstructionType constructionType, Bunny bunny) {
        for(Map.Entry<ObjectType, Integer> constructionCostEntity: constructionType.constructionCost.entrySet()) {
            int resourcesInBackPack = bunny.backpack.get(constructionCostEntity.getKey());
            if(constructionCostEntity.getValue() > 0 && resourcesInBackPack < constructionCostEntity.getValue()) {
                return false;
            }
        }
        return true;
    }

    public int getScore() {
        return score;
    }

    public static Set<String> getKeys() {
        return Arrays.stream(ObjectType.values())
                .map(Object::toString)
                .collect(Collectors.toSet());
    }
}
