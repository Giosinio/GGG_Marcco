package org.example.dto;

public class Gained {

    private int gainCount;
    private String gainType;
    private int offerCount;
    private String offerType;

    @Override
    public String toString() {
        return "Gained{" + "gainCount=" + gainCount + ", gainType='" + gainType + '\'' + ", offerCount=" + offerCount + ", offerType='" + offerType + '\'' + '}';
    }

    public int getGainCount() {
        return gainCount;
    }

    public void setGainCount(int gainCount) {
        this.gainCount = gainCount;
    }

    public String getGainType() {
        return gainType;
    }

    public void setGainType(String gainType) {
        this.gainType = gainType;
    }

    public int getOfferCount() {
        return offerCount;
    }

    public void setOfferCount(int offerCount) {
        this.offerCount = offerCount;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }
}
