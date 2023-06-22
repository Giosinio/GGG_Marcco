package org.example.dto;

public class MarketObj {
    private int id;
    private String offerType;
    private int offerCount;
    private String costType;
    private int costCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    public int getOfferCount() {
        return offerCount;
    }

    public void setOfferCount(int offerCount) {
        this.offerCount = offerCount;
    }

    public String getCostType() {
        return costType;
    }

    public void setCostType(String costType) {
        this.costType = costType;
    }

    public int getCostCount() {
        return costCount;
    }

    public void setCostCount(int costCount) {
        this.costCount = costCount;
    }

    @Override
    public String toString() {
        return "MarketObj{" + "id=" + id + ", offerType='" + offerType + '\'' + ", offerCount=" + offerCount + ", costType='" + costType + '\'' + ", costCount=" + costCount + '}';
    }
}
