package org.example.model;

public class Purchases {
    private int id;
    private int userId;
    private int tourId;
    private double finalPrice;
    private String purchaseDate;
    private double discount;
    private double originalPrice;

    public Purchases(int id, int userId, int tourId, double finalPrice, String purchaseDate, double discount, double originalPrice) {
        this.id = id;
        this.userId = userId;
        this.tourId = tourId;
        this.finalPrice = finalPrice;
        this.purchaseDate = purchaseDate;
        this.originalPrice = originalPrice;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getTourId() {
        return tourId;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public double getDiscount() {return discount;}

    public double getOriginalPrice() {return originalPrice;}

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setTourId(Integer tourId) {
        this.tourId = tourId;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public void setDiscount(double discount) {this.discount = discount;}

    public void setOriginalPrice(double originalPrice) {this.originalPrice = originalPrice;}

}

