package org.example.dto.enums;

import java.util.HashMap;
import java.util.Map;
import org.example.Bunny;

public enum ConstructionType {
    BURROW(5, buildCostsMap(1, 0, 0, 0, 0)),
    HUT(3, buildCostsMap(0, 2, 3, 0, 0)),
    FENCE(1, buildCostsMap(0, 0, 0, 2, 0)),
    DWELLING(7, buildCostsMap(2, 2, 2, 2, 0)),
    VILLA(9, buildCostsMap(2, 3, 2, 4, 1));
    public int stamina;
    public Map<ObjectType, Integer> constructionCost;

    ConstructionType(int stamina, Map<ObjectType, Integer> constructionCost) {
        this.stamina = stamina;
        this.constructionCost = constructionCost;
    }

    private static Map<ObjectType, Integer> buildCostsMap(int clay, int hay, int rocks, int wood, int flowers) {
        Map<ObjectType, Integer> constructionCost = new HashMap<>();
        constructionCost.put(ObjectType.CLAY, clay);
        constructionCost.put(ObjectType.HAY, hay);
        constructionCost.put(ObjectType.ROCK, rocks);
        constructionCost.put(ObjectType.WOOD, wood);
        constructionCost.put(ObjectType.FLOWER, flowers);

        return constructionCost;
    }

    public static boolean checkIfItCanBeBuilt(ConstructionType constructionType, Bunny bunny) {
        if(bunny.getStamina() < constructionType.stamina) {
            return false;
        }
        for(Map.Entry<ObjectType, Integer> constructionCostEntity: constructionType.constructionCost.entrySet()) {
            int resourcesInBackPack = bunny.backpack.get(constructionCostEntity.getKey());
            if(constructionCostEntity.getValue() > 0 && resourcesInBackPack < constructionCostEntity.getValue()) {
                return false;
            }
        }
        return true;
    }


}
