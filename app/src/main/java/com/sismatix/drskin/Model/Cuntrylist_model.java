package com.sismatix.drskin.Model;

public class Cuntrylist_model {
    String flag;
    String name;
    String vlaue;
    public Cuntrylist_model(String flag, String name,String vlaue) {
        this.flag = flag;
        this.name = name;
        this.vlaue = vlaue;
    }

    public String getFlag() {
        return flag;
    }
    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVlaue() {
        return vlaue;
    }

    public void setVlaue(String vlaue) {
        this.vlaue = vlaue;
    }



}
