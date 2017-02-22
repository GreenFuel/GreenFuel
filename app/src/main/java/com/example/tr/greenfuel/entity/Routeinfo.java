package com.example.tr.greenfuel.entity;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Routeinfo entity. @author MyEclipse Persistence Tools
 */

public class Routeinfo implements java.io.Serializable {

	// Fields

	private Integer routeId;
	private Car car;
	private Driver driver;
	private Double startPoiLng;
	private Double startPoiLat;
	private Double desPoiLng;
	private Double desPoiLat;
	private Timestamp routeTime;
	private Double routeOilRatio;
	private Double routeCoemRatio;
	private Double routeHcemRatio;
	private Double routeNoemRatio;
	private Set travelingdatas = new HashSet(0);

	// Constructors

	/** default constructor */
	public Routeinfo() {
	}

	/** minimal constructor */
	public Routeinfo(Car car, Driver driver, Double startPoiLng,
			Double startPoiLat, Double desPoiLng, Double desPoiLat,
			Timestamp routeTime, Double routeOilRatio, Double routeCoemRatio,
			Double routeHcemRatio, Double routeNoemRatio) {
		this.car = car;
		this.driver = driver;
		this.startPoiLng = startPoiLng;
		this.startPoiLat = startPoiLat;
		this.desPoiLng = desPoiLng;
		this.desPoiLat = desPoiLat;
		this.routeTime = routeTime;
		this.routeOilRatio = routeOilRatio;
		this.routeCoemRatio = routeCoemRatio;
		this.routeHcemRatio = routeHcemRatio;
		this.routeNoemRatio = routeNoemRatio;
	}

	/** full constructor */
	public Routeinfo(Car car, Driver driver, Double startPoiLng,
			Double startPoiLat, Double desPoiLng, Double desPoiLat,
			Timestamp routeTime, Double routeOilRatio, Double routeCoemRatio,
			Double routeHcemRatio, Double routeNoemRatio, Set travelingdatas) {
		this.car = car;
		this.driver = driver;
		this.startPoiLng = startPoiLng;
		this.startPoiLat = startPoiLat;
		this.desPoiLng = desPoiLng;
		this.desPoiLat = desPoiLat;
		this.routeTime = routeTime;
		this.routeOilRatio = routeOilRatio;
		this.routeCoemRatio = routeCoemRatio;
		this.routeHcemRatio = routeHcemRatio;
		this.routeNoemRatio = routeNoemRatio;
		this.travelingdatas = travelingdatas;
	}

	// Property accessors

	public Integer getRouteId() {
		return this.routeId;
	}

	public void setRouteId(Integer routeId) {
		this.routeId = routeId;
	}

	public Car getCar() {
		return this.car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public Driver getDriver() {
		return this.driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public Double getStartPoiLng() {
		return this.startPoiLng;
	}

	public void setStartPoiLng(Double startPoiLng) {
		this.startPoiLng = startPoiLng;
	}

	public Double getStartPoiLat() {
		return this.startPoiLat;
	}

	public void setStartPoiLat(Double startPoiLat) {
		this.startPoiLat = startPoiLat;
	}

	public Double getDesPoiLng() {
		return this.desPoiLng;
	}

	public void setDesPoiLng(Double desPoiLng) {
		this.desPoiLng = desPoiLng;
	}

	public Double getDesPoiLat() {
		return this.desPoiLat;
	}

	public void setDesPoiLat(Double desPoiLat) {
		this.desPoiLat = desPoiLat;
	}

	public Timestamp getRouteTime() {
		return this.routeTime;
	}

	public void setRouteTime(Timestamp routeTime) {
		this.routeTime = routeTime;
	}

	public Double getRouteOilRatio() {
		return this.routeOilRatio;
	}

	public void setRouteOilRatio(Double routeOilRatio) {
		this.routeOilRatio = routeOilRatio;
	}

	public Double getRouteCoemRatio() {
		return this.routeCoemRatio;
	}

	public void setRouteCoemRatio(Double routeCoemRatio) {
		this.routeCoemRatio = routeCoemRatio;
	}

	public Double getRouteHcemRatio() {
		return this.routeHcemRatio;
	}

	public void setRouteHcemRatio(Double routeHcemRatio) {
		this.routeHcemRatio = routeHcemRatio;
	}

	public Double getRouteNoemRatio() {
		return this.routeNoemRatio;
	}

	public void setRouteNoemRatio(Double routeNoemRatio) {
		this.routeNoemRatio = routeNoemRatio;
	}

	public Set getTravelingdatas() {
		return this.travelingdatas;
	}

	public void setTravelingdatas(Set travelingdatas) {
		this.travelingdatas = travelingdatas;
	}

}