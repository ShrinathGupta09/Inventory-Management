package com.realllydan.management;

public class Merchandise {
    private String merch_image_url;
    private String merch_name;
    private String merch_cost;
    private String merch_quantity;

    public Merchandise() {

    }

    public Merchandise(String merch_image_url, String merch_name, String merch_cost, String merch_quantity) {
        this.merch_image_url = merch_image_url;
        this.merch_name = merch_name;
        this.merch_cost = merch_cost;
        this.merch_quantity = merch_quantity;
    }

    public String getMerch_image_url() {
        return merch_image_url;
    }

    public void setMerch_image_url(String merch_image_url) {
        this.merch_image_url = merch_image_url;
    }

    public String getMerch_name() {
        return merch_name;
    }

    public void setMerch_name(String merch_name) {
        this.merch_name = merch_name;
    }

    public String getMerch_cost() {
        return merch_cost;
    }

    public void setMerch_cost(String merch_cost) {
        this.merch_cost = merch_cost;
    }

    public String getMerch_quantity() {
        return merch_quantity;
    }

    public void setMerch_quantity(String merch_quantity) {
        this.merch_quantity = merch_quantity;
    }
}
