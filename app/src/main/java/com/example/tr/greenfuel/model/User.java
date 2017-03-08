package com.example.tr.greenfuel.model;

/**
 * Created by TR on 2017/3/7.
 */

public class User {
    private String name;
    private String tel;
    private String password;
    private String brand;
    private String type;
    private String model;

    public User() {

    }

    public User(String name, String tel, String password, String brand, String type, String model) {
        this.name = name;
        this.tel = tel;
        this.password = password;
        this.brand = brand;
        this.type = type;
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
