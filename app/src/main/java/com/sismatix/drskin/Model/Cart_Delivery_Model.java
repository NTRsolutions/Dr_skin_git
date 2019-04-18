package com.sismatix.drskin.Model;

public class Cart_Delivery_Model {
    String code, method, title, price;
    private boolean isSelected = false;

    public Cart_Delivery_Model(String code, String method, String title, String price) {
        this.code = code;
        this.method = method;
        this.title = title;
        this.price = price;
    }


    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

