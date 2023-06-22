package org.example.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.example.dto.MapPosition;
import org.example.dto.MarketObj;
import org.example.dto.enums.ConstructionType;
import org.example.dto.enums.MessageType;
import org.example.dto.enums.ObjectType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class MarccoMessage {

    public MessageType messageType;
    public int round;
    public char[][] gameBoard;
    public Map<String, List<MapPosition>> objects;
    public Map<ObjectType, List<MapPosition>> collectableObjects = new HashMap<>(); //food + construction materials, cuz objects also have bunns and buildings
    public List<MarketObj> market;
    public Map<ObjectType, Integer> backpack;
    public int state;
    public int row;
    public int col;
    public int stamina;
    public List<MarketObj> gained;

    public void mapObjectsToCollectableObjects() {
        for(Map.Entry<String, List<MapPosition>> objectsEntry: objects.entrySet()) {
            String key = objectsEntry.getKey();
            if(ObjectType.getKeys().contains(key)) {
                ObjectType objectType = ObjectType.valueOf(key);
                collectableObjects.put(objectType, objectsEntry.getValue());
            }
        }
    }

//    public String getGameBoardAsString(char[][] gameBoard) {
//        if (gameBoard != null) {
//            StringBuilder stringBuilder = new StringBuilder();
//            stringBuilder.append("[");
//            for (char[] row : gameBoard) {
//                stringBuilder.append("[");
//                for (char column : row) {
//                    stringBuilder.append("\"");
//                    stringBuilder.append(column);
//                    stringBuilder.append("\"");
//                    stringBuilder.append(",");
//                }
//                stringBuilder.append("]");
//            }
//            return stringBuilder.toString();
//        }
//        return "";
//    }

//    public String getObjectsAsString(Map<ObjectType, MarccoObject[]> objects) {
//        if (objects != null) {
//            StringBuilder stringBuilder = new StringBuilder();
//            stringBuilder.append("{");
//            for (Map.Entry<ObjectType, MarccoObject[]> entry : objects.entrySet()) {
//                stringBuilder.append("\"");
//                stringBuilder.append(entry.getKey());
//                stringBuilder.append("\"");
//                stringBuilder.append("=");
//                stringBuilder.append("\"");
//                stringBuilder.append(Arrays.toString(entry.getValue()));
//                stringBuilder.append("\"");
//            }
//            return stringBuilder.toString();
//        }
//        return "";
//    }
}
