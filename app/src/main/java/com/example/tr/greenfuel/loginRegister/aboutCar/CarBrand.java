package com.example.tr.greenfuel.loginRegister.aboutCar;

import java.io.Serializable;

/**
 * Created by tangpeng on 2017/2/22.
 */

public class CarBrand implements Serializable{
    private String brandName;   //汽车品牌名字
    private String sortLetter;  //汽车品牌名字的首字母，list排序的依据
    private  int id;
    private int parentId;   //上级ID
    private String logoURL; //logo图片的url
    private int depth;  //深度: 1品牌 2子公司 3车型 4具体车型

    public CarBrand(String brandName, String sortLetter) {
        this.brandName = brandName;
        this.sortLetter = sortLetter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public CarBrand() {
    }

    @Override
    public String toString() {
        return "CarBrand{" +
                "brandName='" + brandName + '\'' +
                ", sortLetter='" + sortLetter + '\'' +
                ", id=" + id +
                ", parentId=" + parentId +
                ", logoURL='" + logoURL + '\'' +
                ", depth=" + depth +
                '}';
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getSortLetter() {
        return sortLetter;
    }

    public void setSortLetter(String sortLetter) {
        this.sortLetter = sortLetter;
    }
}
