package com.example.tr.greenfuel.model;



/**
 * Created by TR on 2017/2/17.
 * 用来保存路径的起点、终点位置
 */

public class MyPaths {
    private String originName;
    private String endName;
    private Double oLat;
    private Double oLng;
    private Double eLat;
    private Double eLng;

    public MyPaths() {
    }

    public MyPaths(String originName, String endName, Double oLat, Double oLng, Double eLat, Double eLng) {
        this.originName = originName;
        this.endName = endName;
        this.oLat = oLat;
        this.oLng = oLng;
        this.eLat = eLat;
        this.eLng = eLng;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getEndName() {
        return endName;
    }

    public void setEndName(String endName) {
        this.endName = endName;
    }

    public Double getoLat() {
        return oLat;
    }

    public void setoLat(Double oLat) {
        this.oLat = oLat;
    }

    public Double getoLng() {
        return oLng;
    }

    public void setoLng(Double oLng) {
        this.oLng = oLng;
    }

    public Double geteLat() {
        return eLat;
    }

    public void seteLat(Double eLat) {
        this.eLat = eLat;
    }

    public Double geteLng() {
        return eLng;
    }

    public void seteLng(Double eLng) {
        this.eLng = eLng;
    }

    @Override
    public String toString() {
        return "MyPaths{" +
                "originName='" + originName + '\'' +
                ", endName='" + endName + '\'' +
                ", oLat=" + oLat +
                ", oLng=" + oLng +
                ", eLat=" + eLat +
                ", eLng=" + eLng +
                '}';
    }
}
