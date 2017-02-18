package com.example.tr.greenfuel.model;

/**
 * Created by TR on 2017/2/17.
 * 保存收藏的点和搜索过的点
 */

public class MyPlace {
    private String name;
    private Double Lat;
    private Double Lng;
    private boolean isCollection;

    public MyPlace(String name, Double lat, Double lng, boolean isCollection) {
        this.name = name;
        Lat = lat;
        Lng = lng;
        this.isCollection = isCollection;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLat() {
        return Lat;
    }

    public void setLat(Double lat) {
        Lat = lat;
    }

    public Double getLng() {
        return Lng;
    }

    public void setLng(Double lng) {
        Lng = lng;
    }

    public boolean isCollection() {
        return isCollection;
    }

    public void setCollection(boolean collection) {
        isCollection = collection;
    }

    @Override
    public String toString() {
        return "MyPlace{" +
                "name='" + name + '\'' +
                ", Lat=" + Lat +
                ", Lng=" + Lng +
                ", isCollection=" + isCollection +
                '}';
    }
}
