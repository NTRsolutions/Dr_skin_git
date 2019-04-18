package com.sismatix.drskin.Model;

public class Product_Grid_Model {

    String product_image,product_price,producr_title,type,product_id,product_specialprice;

    public Product_Grid_Model(String product_image, String product_price, String producr_title, String type, String product_id, String product_specialprice) {
        this.product_image = product_image;
        this.product_price = product_price;
        this.producr_title = producr_title;
        this.type = type;
        this.product_id= product_id;
        this.product_specialprice= product_specialprice;
    }

    public String getProduct_image() {

        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProducr_title() {
        return producr_title;
    }

    public void setProducr_title(String producr_title) {
        this.producr_title = producr_title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_specialprice() {
        return product_specialprice;
    }

    public void setProduct_specialprice(String product_specialprice) {
        this.product_specialprice = product_specialprice;
    }
}
