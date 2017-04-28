package com.prashantpandey.nurseryapp;



public class IdealCart {

    private String commonName;
    private String Total;
    private String Quantity;
    private String price;
    private String imageUrl;



    private String size;

    public IdealCart(String commonName, String imageUrl, String price, String quantity, String total,String size) {
        this.commonName = commonName;
        this.imageUrl = imageUrl;
        this.price = price;
        Quantity = quantity;
        this.size = size;
        Total = total;
    }

    public IdealCart() {
    }

    public String getCommonName() {
        return commonName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPrice() {
        return price;
    }

    public String getQuantity() {
        return Quantity;
    }

    public String getTotal() {
        return Total;
    }
    public String getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "Name:"+getCommonName() +","+ "Size:"+ getSize()+","+ "Quantity:" + getQuantity() +","+ "Price:"+ getPrice() +","+ "Total:"+ getTotal();
    }
}
