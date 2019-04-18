package com.sismatix.drskin.Model;

public class My_order_model {
    String increment_id;
    String order_id;
    String status;
    String created_at;
    String name;
    String grand_total;
    String Paymentmethod;

    public My_order_model(String increment_id, String order_id, String status, String created_at, String name, String grand_total, String paymentmethod) {
        this.increment_id = increment_id;
        this.order_id = order_id;
        this.status = status;
        this.created_at = created_at;
        this.name = name;
        this.grand_total = grand_total;
        Paymentmethod = paymentmethod;
    }

    public String getIncrement_id() {
        return increment_id;
    }

    public void setIncrement_id(String increment_id) {
        this.increment_id = increment_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrand_total() {
        return grand_total;
    }

    public void setGrand_total(String grand_total) {
        this.grand_total = grand_total;
    }

    public String getPaymentmethod() {
        return Paymentmethod;
    }

    public void setPaymentmethod(String paymentmethod) {
        Paymentmethod = paymentmethod;
    }
}

