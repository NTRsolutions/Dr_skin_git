package com.sismatix.drskin.Model;

public class Wishlist_Model {
    String product_image,product_name,product_price,product_category,product_id;

    public Wishlist_Model(String product_image, String product_name, String product_price, String product_category, String product_id) {
        this.product_image = product_image;
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_category = product_category;
        this.product_id = product_id;
    }

    public String getProduct_image() {

        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_category() {
        return product_category;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public void setProduct_category(String product_category) {
        this.product_category = product_category;


    }
}