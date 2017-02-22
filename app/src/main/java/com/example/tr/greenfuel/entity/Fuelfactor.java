package com.example.tr.greenfuel.entity;

/**
 * Fuelfactor entity. @author MyEclipse Persistence Tools
 */

public class Fuelfactor implements java.io.Serializable {

	// Fields

	private Integer fuelFactorId;
	private Integer speedRange;
	private Short carType;
	private Double carFuelFactor;

	// Constructors

	/** default constructor */
	public Fuelfactor() {
	}

	/** full constructor */
	public Fuelfactor(Integer speedRange, Short carType, Double carFuelFactor) {
		this.speedRange = speedRange;
		this.carType = carType;
		this.carFuelFactor = carFuelFactor;
	}

	// Property accessors

	public Integer getFuelFactorId() {
		return this.fuelFactorId;
	}

	public void setFuelFactorId(Integer fuelFactorId) {
		this.fuelFactorId = fuelFactorId;
	}

	public Integer getSpeedRange() {
		return this.speedRange;
	}

	public void setSpeedRange(Integer speedRange) {
		this.speedRange = speedRange;
	}

	public Short getCarType() {
		return this.carType;
	}

	public void setCarType(Short carType) {
		this.carType = carType;
	}

	public Double getCarFuelFactor() {
		return this.carFuelFactor;
	}

	public void setCarFuelFactor(Double carFuelFactor) {
		this.carFuelFactor = carFuelFactor;
	}

}