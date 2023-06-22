package org.example.dto;

import org.example.dto.enums.ObjectType;

public class CollectableItem {
    public MapPosition mapPosition;
    public ObjectType objectType;
    public int coefficient;

    public CollectableItem() {
    }

    public CollectableItem(MapPosition mapPosition, ObjectType objectType) {
        this.mapPosition = mapPosition;
        this.objectType = objectType;
    }

    public MapPosition getMapPosition() {
        return mapPosition;
    }

    public void setMapPosition(MapPosition mapPosition) {
        this.mapPosition = mapPosition;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(ObjectType objectType) {
        this.objectType = objectType;
    }
}
