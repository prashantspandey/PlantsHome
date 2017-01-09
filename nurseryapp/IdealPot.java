package com.prashantpandey.nurseryapp;



public class IdealPot {

    private String type;
    private String imageurl;
    private String plant;
    private String pricesixInch,priceeightInch,pricenineInch,pricetenInch,pricetwelveInch,pricefourteenInch,pricesixteenInch,priceeighteenInch,pricetwentyInch;

    public IdealPot(String plant,String type, String pricesixInch, String priceeightInch,
                    String pricenineInch, String pricetenInch, String pricetwelveInch,
                    String pricefourteenInch, String pricesixteenInch, String priceeighteenInch, String pricetwentyInch,String imageurl) {
        this.plant = plant;
        this.type = type;

        this.pricesixInch = pricesixInch;
        this.priceeightInch = priceeightInch;
        this.pricenineInch = pricenineInch;
        this.pricetenInch = pricetenInch;
        this.pricetwelveInch = pricetwelveInch;
        this.pricefourteenInch = pricefourteenInch;
        this.pricesixteenInch = pricesixteenInch;
        this.priceeighteenInch = priceeighteenInch;
        this.pricetwentyInch = pricetwentyInch;
        this.imageurl = imageurl;
    }
    public String getPlant() {
        return plant;
    }


    public IdealPot() {
    }



    public String getPriceeighteenInch() {
        return priceeighteenInch;
    }

    public String getPriceeightInch() {
        return priceeightInch;
    }

    public String getPricefourteenInch() {
        return pricefourteenInch;
    }

    public String getPricenineInch() {
        return pricenineInch;
    }

    public String getPricesixInch() {
        return pricesixInch;
    }

    public String getPricesixteenInch() {
        return pricesixteenInch;
    }

    public String getPricetenInch() {
        return pricetenInch;
    }

    public String getPricetwelveInch() {
        return pricetwelveInch;
    }

    public String getPricetwentyInch() {
        return pricetwentyInch;
    }
    public String getImageurl() {
        return imageurl;
    }


    public String getType() {
        return type;
    }
}
