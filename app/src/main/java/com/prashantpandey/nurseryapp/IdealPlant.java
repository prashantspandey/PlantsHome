package com.prashantpandey.nurseryapp;


public class IdealPlant {

   private String commonName;
   private String scientificName;
   private String imageUrl;
   private String category;
   private String priceSmall;
    private String priceMedium;
    private String priceLarge;
    private String priceXLarge;
    private String priceXXLarge;
    private String priceOther;
    private boolean small;
    private boolean medium;
    private boolean large;
    private boolean xLarge;
    private boolean xxLarge;
    private String other;



    private String description;

    public IdealPlant(String category, String commonName, String imageUrl,
                      String priceLarge, String priceMedium, String priceSmall, String priceXLarge, String priceXXLarge, String priceOther,
                      String scientificName, String description, boolean small, boolean medium, boolean large, boolean xLarge, boolean xxLarge,
                      String other) {
        this.category = category;
        this.commonName = commonName;
        this.imageUrl = imageUrl;
        this.priceLarge = priceLarge;
        this.priceMedium = priceMedium;
        this.priceSmall = priceSmall;
        this.priceXLarge = priceXLarge;
        this.priceXXLarge = priceXXLarge;
        this.priceOther = priceOther;
        this.scientificName = scientificName;
        this.description = description;
        this.small = small;
        this.medium = medium;
        this.large = large;
        this.xLarge = xLarge;
        this.xxLarge = xxLarge;
        this.other = other;
    }

    public IdealPlant() {
    }

    public String getCategory() {
        return category;
    }

    public String getCommonName() {
        return commonName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPriceLarge() {
        return priceLarge;
    }

    public String getPriceMedium() {
        return priceMedium;
    }

    public String getPriceSmall() {
        return priceSmall;
    }

    public String getScientificName() {
        return scientificName;
    }


    public String getDescription() {
        return description;
    }
    public boolean isSmall() {
        return small;
    }

    public boolean isMedium() {
        return medium;
    }

    public boolean isLarge() {
        return large;
    }

    public String getPriceXLarge() {
        return priceXLarge;
    }

    public String getPriceXXLarge() {
        return priceXXLarge;
    }

    public boolean isxLarge() {
        return xLarge;
    }

    public boolean isXxLarge() {
        return xxLarge;
    }

    public String Other() {
        return other;
    }

    public String getPriceOther() {
        return priceOther;
    }

    @Override
    public String toString() {
        return  getCommonName()  + " "+   getScientificName();

    }
}
