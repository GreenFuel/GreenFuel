package com.example.tr.greenfuel.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * Car entity. @author MyEclipse Persistence Tools
 */

public class Car implements java.io.Serializable {

	// Fields

	private Integer carId;
	private Driver driver;
	private String carBrand;
	private String carType;
	private Short carEmissionStd;
	private Set routeinfos = new HashSet(0);

	// Constructors

	/** default constructor */
	public Car() {
	}

	/** minimal constructor */
	public Car(Driver driver, String carBrand, String carType,
			Short carEmissionStd) {
		this.driver = driver;
		this.carBrand = carBrand;
		this.carType = carType;
		this.carEmissionStd = carEmissionStd;
	}

	/** full constructor */
	public Car(Driver driver, String carBrand, String carType,
			Short carEmissionStd, Set routeinfos) {
		this.driver = driver;
		this.carBrand = carBrand;
		this.carType = carType;
		this.carEmissionStd = carEmissionStd;
		this.routeinfos = routeinfos;
	}

	// Property accessors

	public Integer getCarId() {
		return this.carId;
	}

	public void setCarId(Integer carId) {
		this.carId = carId;
	}

	public Driver getDriver() {
		return this.driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public String getCarBrand() {
		return this.carBrand;
	}

	public void setCarBrand(String carBrand) {
		this.carBrand = carBrand;
	}

	public String getCarType() {
		return this.carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public Short getCarEmissionStd() {
		return this.carEmissionStd;
	}

	public void setCarEmissionStd(Short carEmissionStd) {
		this.carEmissionStd = carEmissionStd;
	}

	public Set getRouteinfos() {
		return this.routeinfos;
	}

	public void setRouteinfos(Set routeinfos) {
		this.routeinfos = routeinfos;
	}

}