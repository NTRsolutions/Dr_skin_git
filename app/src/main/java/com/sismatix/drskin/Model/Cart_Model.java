package com.sismatix.drskin.Model;

public class Cart_Model {
    String product_name,product_price,product_image,product_description,product_id,row_total,product_qty,itemid,wishlist;
    public Cart_Model(String product_qty){
        this.product_qty = product_qty;

    }

    public Cart_Model(String product_name, String product_price, String product_image,
                      String product_description, String product_id, String row_total,
                      String product_qty, String itemid,String wishlist) {
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_image = product_image;
        this.product_description = product_description;
        this.product_id = product_id;
        this.row_total = row_total;
        this.product_qty = product_qty;
        this.wishlist=wishlist;
        this.itemid = itemid;
    }

    public String getProduct_id() {

        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getRow_total() {
        return row_total;
    }

    public void setRow_total(String row_total) {
        this.row_total = row_total;
    }

    public String getProduct_qty() {
        return product_qty;
    }

    public void setProduct_qty(String product_qty) {
        this.product_qty = product_qty;
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

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }
    public String getWishlist() {
        return wishlist;
    }
    public void setWishlist(String wishlist) {
        this.wishlist = wishlist;
    }
}
