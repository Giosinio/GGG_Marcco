package org.example;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.example.dto.MapPosition;
import org.example.dto.enums.ConstructionType;
import org.example.dto.enums.ObjectType;

import jdk.nashorn.internal.ir.BaseNode;

public class Utils {
    private static final int MAXIMUM_STAMINA = 10;

    private static final int NUMBER_ROUNDS = 300;

    //objects are the objects on the map, all the food, building resources and bunnies(since 2 bunnies cannot be at the same time on the same tile,
    // we should be safe to consider we are on a resource if that resource is found)
    public static String makeAction(Bunny bunny, char[][] table, String botId, Map<ObjectType, List<MapPosition>> objects) {
        int i = bunny.row;
        int j = bunny.column;
        ObjectType bunnyObjectType = checkCollectableResourceAtTheGivenPosition(i, j, objects); // the object type on which the bunny stands on
        String decider = checkPosition(bunny, table, bunnyObjectType);

        if ("drink".equals(decider)) {
            return "{ \"action\": \"drink\", \"botId\":" + botId + "\" }";
        }
        if(decider.contains("eat")) {
            String[] bits = decider.split("|");
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
            String[] bits = decider.split("|");
            ConstructionType constructionType = ConstructionType.valueOf(bits[1]);
            return "{ \"action\" : \"build\", \"what\": \"" + constructionType.name().toLowerCase() + "\", \"bot_id\" :\"" + botId + "\" }";
        }
        else {
            //TODO: implement move action
//            return computeResponse(robot,tabla,trashes,dumpsterList,botId);
            return "{ \"action\" : \"hop\", \"speed\": \"" + "1" + "\", \"bot_id\" :\"" + botId + "\" }";

        }
    }

//    private static String computeResponse(Bunny bunny, char[][] table, List<Trash> trashes, List<Dumpster> dumpsterList,String botId){
//        String direction=getDirection(bunny, table, trashes, dumpsterList);
//        if(direction==null){
//            return null;
//        }
//        String directionSide=direction.split(":")[0];
//        int speed=Integer.parseInt(direction.split(":")[1]);
//        return "{ \"move\" : \""+directionSide+"\", \"speed\":"+speed+", \"bot_id\" :\"" + botId + "\" }";
//    }

//    private static String getDirection(Bunny bunny, char[][] table, List<Trash> trashes,List<Dumpster> dumpsterList){
//        int i = robot.row;
//        int j = robot.col;
//        LeeResult result=LeeAlgorithmSolver.solveLee(table,i,j);
//        computeCoeffs(trashes,result);
//        trashes.sort((o1, o2) -> Double.compare(o2.coefficient, o1.coefficient));
//        Trash bestTrash=null;
//        for (Trash trash : trashes) {
//            if (robot.canStore(trash)) {
//                bestTrash = trash;
//                break;
//            }
//        }
//        if(bestTrash!=null){
//            return getNextMove(new Pair<>(bestTrash.row, bestTrash.column), robot, result.positionsMatrix);
//        }
//        List<Container> containers = robot.containerList;
//        containers.sort(new Comparator<Container>() {
//            @Override
//            public int compare(Container o1, Container o2) {
//                return Integer.compare(o2.getFilled(),o1.filled);
//            }
//        });
//        Container bestContainer=containers.get(0);
//        ObjectType containerType=bestContainer.getType();
//        Dumpster dumpster=dumpsterList.stream()
//                .filter(dumpster1 -> dumpster1.objectType.equals(containerType))
//                .findFirst()
//                .orElse(null);
//        if(dumpster==null){
//            return null;
//        }
//        return getNextMove(new Pair<>(dumpster.row, dumpster.column), robot, result.positionsMatrix);
//    }

    public static String checkPosition(Bunny bunny, char[][] table, ObjectType objectType) {
        int i = bunny.row;
        int j = bunny.column;
        if(table[i][j] == 's' && bunny.getStamina() < MAXIMUM_STAMINA) {
            return "drink";
        }
        if(objectType != null && objectType != ObjectType.BUNNS) {
            if(objectType == ObjectType.BEETS || objectType == ObjectType.LEAVES || objectType == ObjectType.CARROT) {
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
                            return "eat|" + eatenResource.name().toLowerCase();
                        }
                    }
                    return "build|" + constructionType.name().toLowerCase();
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
            if(count > 0 && (eatedObjectType == null || key.getGivenStamina() > eatedObjectType.getGivenStamina())) {
                eatedObjectType = key;
            }
            if(count > 0 && key.getGivenStamina() == differenceStamina) {
                return key;
            }
        }
        return eatedObjectType;
    }

    public static ObjectType checkCollectableResourceAtTheGivenPosition(int row, int column, Map<ObjectType, List<MapPosition>> objects) {
        MapPosition bunnyMapPosition = new MapPosition(row, column);
        for(Map.Entry<ObjectType, List<MapPosition>> objectEntry: objects.entrySet()) {
            if(objectEntry.getValue().contains(bunnyMapPosition) && objectEntry.getKey() != ObjectType.BUNNS) {
                return objectEntry.getKey();
            }
        }
        return null;
    }
}
