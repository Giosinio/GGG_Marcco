package org.example.dto;

import java.util.List;
import java.util.Map;

import org.example.dto.MapPosition;
import org.example.dto.MarketObj;
import org.example.dto.enums.MessageType;
import org.example.dto.enums.ObjectType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class MarccoMessage {

    public MessageType messageType;
    public int round;
    public char[][] gameBoard;
    public Map<ObjectType, List<MapPosition>> objects;
    public List<MarketObj> market;
    public Map<ObjectType, Integer> backpack;
    public int state;
    public int row;
    public int col;
    public int stamina;
    public List<MarketObj> gained;

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
