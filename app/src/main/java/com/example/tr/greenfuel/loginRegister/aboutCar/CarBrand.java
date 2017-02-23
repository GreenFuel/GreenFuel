package com.example.tr.greenfuel.loginRegister.aboutCar;

/**
 * Created by tangpeng on 2017/2/22.
 */

public class CarBrand {
    private String brandName;   //汽车品牌名字
    private String sortLetter;  //汽车品牌名字的首字母，list排序的依据

    public CarBrand(String brandName, String sortLetter) {
        this.brandName = brandName;
        this.sortLetter = sortLetter;
    }

    public CarBrand() {
    }

    @Override
    public String toString() {
        return "CarBrand{" +
                "brandName='" + brandName + '\'' +
                ", sortLetter='" + sortLetter + '\'' +
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
