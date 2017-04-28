package com.prashantpandey.nurseryapp;



public class IdealOrder {

    private String name;
    private String phone;
    private String address;
    private String order;
    private String amount;
    private String time;
    private String uid;
    private boolean confirmation;
    private boolean deliveryStatus;

    public IdealOrder(String name, String phone, String address, String order,String amount,String time,
                      boolean confirmation,boolean deliveryStatus,String uid) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.order = order;
        this.amount = amount;
        this.time = time;
        this.confirmation = confirmation;
        this.deliveryStatus = deliveryStatus;
        this.uid = uid;
    }

    public IdealOrder() {
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getOrder() {
        return order;
    }

    public String getPhone() {
        return phone;
    }

    public String getAmount() {
        return amount;
    }

    public String getTime() {
        return time;
    }

    public boolean isConfirmation() {
        return confirmation;
    }

    public boolean isDeliveryStatus() {
        return deliveryStatus;
    }

    public String getUid() {
        return uid;
    }
}
