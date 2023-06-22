package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import org.example.algorithm.LeeAlgorithmSolver;
import org.example.algorithm.LeeResult;
import org.example.dto.CollectableItem;
import org.example.dto.MapPosition;
import org.example.dto.enums.ConstructionType;
import org.example.dto.enums.ObjectType;

public class Utils {
    private static final Random random = new Random();
    private static final int MAXIMUM_STAMINA = 10;

    private static final int BUILD_AFTER_ROUND = 225; //TODO - herte change this if the number of rounds is not 300, approximately 65% of the number of rounds

    //objects are the objects on the map, all the food, building resources and bunnies(since 2 bunnies cannot be at the same time on the same tile,
    // we should be safe to consider we are on a resource if that resource is found)
    public static String makeAction(Bunny bunny, char[][] table, String botId, Map<ObjectType, List<MapPosition>> objects, List<CollectableItem> collectableItems, List<MapPosition> buildingsPositions) {
        if(bunny.isFirstMarketOffer && bunny.currentRound >= 250) {
            List<String> marketKeys = Arrays.asList("carrot", "leaves", "beets", "flower", "wood", "clay", "rock", "hay");

            for(String key: marketKeys) {
                int localCount = bunny.backpack.get(ObjectType.valueOf(key));
                if(localCount > 0) {
                    bunny.isFirstMarketOffer = false;
                    return "{ \"market\": \"offer\", \"offer\": {\"offerType\": \"" + key + "\", \"offerCount\": 1, \"costType\": \"wood\", \"costCount\": 4}, \"bot_id\": \"" + botId +"\"}";
                }
            }
        }
        int i = bunny.row;
        int j = bunny.column;
        ObjectType bunnyObjectType = checkCollectableResourceAtTheGivenPosition(i, j, objects); // the object type on which the bunny stands on
        String decider = checkPosition(bunny, table, bunnyObjectType, buildingsPositions);

        if ("drink".equals(decider)) {
            return "{ \"action\": \"drink\", \"bot_id\":\"" + botId + "\" }";
        }
        if(decider.contains("eat")) {
            String[] bits = decider.split("\\|");
            if(bits.length == 1) {
                return "{ \"action\" : \"eat\", \"bot_id\" :\"" + botId + "\" }";
            }
            else {
                String whatToEat = bits[1];
                return "{ \"action\" : \"eat\", \"what\": \"" + whatToEat + "\", \"bot_id\" :\"" + botId + "\" }";
            }
        }
        else if("pick".equals(decider)) {
            return "{ \"action\" : \"pick\", \"bot_id\" :\"" + botId + "\" }";
        }
        else if(decider.contains("build")) {
            String[] bits = decider.split("\\|");
            ConstructionType constructionType = ConstructionType.valueOf(bits[1]);
            return "{ \"action\" : \"build\", \"what\": \"" + constructionType.name().toLowerCase() + "\", \"bot_id\" :\"" + botId + "\" }";
        }
        else {
            return computeResponse(bunny, table, collectableItems, botId);

        }
    }

    private static String computeResponse(Bunny bunny, char[][] table, List<CollectableItem> collectableItems, String botId){
        String direction;
        try {
            direction = getDirection(bunny, table, collectableItems);
        } catch (NullPointerException ignored) {
            System.out.println("NPE!!!");
            List<String> randomStrings = Arrays.asList("left", "right", "up", "down");
            int min = 0;
            int max = 3;
            int randomNumber = random.nextInt(max - min + 1) + min;
            return randomStrings.get(randomNumber) + ":1";
        }
        String directionSide=direction.split(":")[0];
        int speed=Integer.parseInt(direction.split(":")[1]);
        return "{ \"hop\" : \""+directionSide+"\", \"speed\":"+speed+", \"bot_id\" :\"" + botId + "\" }";
    }

    private static String getDirection(Bunny bunny, char[][] table, List<CollectableItem> collectableItems){
        int i = bunny.row;
        int j = bunny.column;
        LeeResult result = LeeAlgorithmSolver.solveLee(table,i,j);
        computeCoeffs(collectableItems, result);
        collectableItems.sort(Comparator.comparingDouble(o -> o.coefficient));
        CollectableItem bestCollectableItem = collectableItems.get(0);

        return getNextMove(new Pair<>(bestCollectableItem.mapPosition.row, bestCollectableItem.mapPosition.col), bunny, result.positionsMatrix);
    }

    private static String getNextMove(Pair<Integer,Integer> currentPoint, Bunny bunny, Pair[][] positionsDistances) {
        if(positionsDistances[currentPoint.getFirst()][currentPoint.getSecond()].equals(new Pair<>(bunny.row, bunny.column))) {
            if(currentPoint.getFirst() == bunny.row) {
                int distance = Math.abs(bunny.column - currentPoint.getSecond());
                if(currentPoint.getSecond() < bunny.column) {
                    return "left:" + distance;
                }
                else {
                    return "right:"+distance;
                }
            }
            else {
                int distance = Math.abs(bunny.row - currentPoint.getFirst());
                if(currentPoint.getFirst() < bunny.row) {
                    return "up:"+distance;
                }
                else {
                    return "down:"+distance;
                }
            }
        }
        else {
            return getNextMove(positionsDistances[currentPoint.getFirst()][currentPoint.getSecond()], bunny, positionsDistances);
        }
    }

    public static String checkPosition(Bunny bunny, char[][] table, ObjectType objectType, List<MapPosition> buildingsPositions) {
        int i = bunny.row;
        int j = bunny.column;
        if(table[i][j] == 's' && bunny.getStamina() < MAXIMUM_STAMINA) {
            return "drink";
        }
        if(objectType != null && objectType != ObjectType.bunns) {
            if(objectType == ObjectType.beets || objectType == ObjectType.leaves || objectType == ObjectType.carrot) {
                if(bunny.stamina + objectType.getGivenStamina() <= MAXIMUM_STAMINA) {
                    return "eat";
                }
            }
            return "pick";
        }
        if(table[i][j] == ' ') {
            //empty space, we try to build something
            List<ConstructionType> scoreSortedConstructionTypes = Arrays.stream(ConstructionType.values()).sorted(Comparator.comparing(ConstructionType::getScore).reversed()).collect(Collectors.toList());
            //TODO check if the scoreSortedConstructionTypes is sorted in the reversed order
            for(ConstructionType constructionType: scoreSortedConstructionTypes) {
                if(ConstructionType.checkIfWeHaveEnoughMaterials(constructionType, bunny)) {
                    if(bunny.stamina < constructionType.stamina) {
                        if(checkIfWeHaveEnoughFoodToBuild(bunny, constructionType)) {
                            ObjectType eatenResource = getTheFoodWeWillEatFromTheBackpack(bunny, constructionType);
                            if(Objects.nonNull(eatenResource)) {
                                return "eat|" + eatenResource.name().toLowerCase();
                            }
                        }
                    }
                    else {
                        MapPosition bunnyPosition = new MapPosition(bunny.row, bunny.column);
                        if(!buildingsPositions.contains(bunnyPosition) && (constructionType.score >= 100 || bunny.currentRound >= BUILD_AFTER_ROUND)) {
                            return "build|" + constructionType.name().toLowerCase();
                        }
                    }
                }
            }
        }
        return "move";
    }

    private static boolean checkIfWeHaveEnoughFoodToBuild(Bunny bunny, ConstructionType constructionType) {
        int differenceStamina = constructionType.stamina - bunny.stamina;
        int totalAvailableFood  = 0;
        for(Map.Entry<ObjectType, Integer> backpackObject: bunny.backpack.entrySet()) {
            ObjectType key = backpackObject.getKey();
            int count = backpackObject.getValue();
            totalAvailableFood = totalAvailableFood + key.getGivenStamina() * count;
        }
        return totalAvailableFood >= differenceStamina;
    }

    private static ObjectType getTheFoodWeWillEatFromTheBackpack(Bunny bunny, ConstructionType constructionType) {
        int differenceStamina = constructionType.stamina - bunny.stamina; // the stamina we need
        ObjectType eatedObjectType = null;
        for(Map.Entry<ObjectType, Integer> backpackObject: bunny.backpack.entrySet()) {
            ObjectType key = backpackObject.getKey();
            Integer count = backpackObject.getValue();
            if(count > 0 && key.getGivenStamina() > 0 && key != ObjectType.hay && key != ObjectType.flower && (eatedObjectType == null || key.getGivenStamina() > eatedObjectType.getGivenStamina())) {
                eatedObjectType = key;
            }
            if(count > 0 && key.getGivenStamina() > 0 && key != ObjectType.hay && key != ObjectType.flower && key.getGivenStamina() == differenceStamina) {
                return key;
            }
        }
        return eatedObjectType;
    }

    public static ObjectType checkCollectableResourceAtTheGivenPosition(int row, int column, Map<ObjectType, List<MapPosition>> objects) {
        MapPosition bunnyMapPosition = new MapPosition(row, column);
        for(Map.Entry<ObjectType, List<MapPosition>> objectEntry: objects.entrySet()) {
            if(objectEntry.getValue().contains(bunnyMapPosition) && objectEntry.getKey() != ObjectType.bunns) {
                return objectEntry.getKey();
            }
        }
        return null;
    }

    public static List<CollectableItem> changeMappingToCollectableItemList(Map<ObjectType, List<MapPosition>> objects) {
        List<CollectableItem> collectableItems = new ArrayList<>();
        for (Map.Entry<ObjectType, List<MapPosition>> mapEntry : objects.entrySet()) {
            ObjectType objectType = mapEntry.getKey();
            if(objectType==ObjectType.bunns){
                continue;
            }
            for(MapPosition mapPosition: mapEntry.getValue()) {
                collectableItems.add(new CollectableItem(mapPosition, objectType));
            }
        }
        return collectableItems;
    }

    private static void computeCoeffs(List<CollectableItem> collectableItems, LeeResult result){
        collectableItems.
                forEach(collectableItem -> collectableItem.coefficient = result.distanceMatrix[collectableItem.mapPosition.row][collectableItem.mapPosition.col]);
    }
}
