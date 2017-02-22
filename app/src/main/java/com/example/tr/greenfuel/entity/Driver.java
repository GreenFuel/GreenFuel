package com.example.tr.greenfuel.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * Driver entity. @author MyEclipse Persistence Tools
 */

public class Driver implements java.io.Serializable {

	// Fields

	private Integer driId;
	private String driName;
	private String driPhone;
	private String driHeadPhoto;
	private Set routeinfos = new HashSet(0);
	private Set travelingdatas = new HashSet(0);
	private Set cars = new HashSet(0);

	// Constructors

	/** default constructor */
	public Driver() {
	}

	/** minimal constructor */
	public Driver(String driPhone) {
		this.driPhone = driPhone;
	}

	/** full constructor */
	public Driver(String driName, String driPhone, String driHeadPhoto,
			Set routeinfos, Set travelingdatas, Set cars) {
		this.driName = driName;
		this.driPhone = driPhone;
		this.driHeadPhoto = driHeadPhoto;
		this.routeinfos = routeinfos;
		this.travelingdatas = travelingdatas;
		this.cars = cars;
	}

	// Property accessors

	public Integer getDriId() {
		return this.driId;
	}

	public void setDriId(Integer driId) {
		this.driId = driId;
	}

	public String getDriName() {
		return this.driName;
	}

	public void setDriName(String driName) {
		this.driName = driName;
	}

	public String getDriPhone() {
		return this.driPhone;
	}

	public void setDriPhone(String driPhone) {
		this.driPhone = driPhone;
	}

	public String getDriHeadPhoto() {
		return this.driHeadPhoto;
	}

	public void setDriHeadPhoto(String driHeadPhoto) {
		this.driHeadPhoto = driHeadPhoto;
	}

	public Set getRouteinfos() {
		return this.routeinfos;
	}

	public void setRouteinfos(Set routeinfos) {
		this.routeinfos = routeinfos;
	}

	public Set getTravelingdatas() {
		return this.travelingdatas;
	}

	public void setTravelingdatas(Set travelingdatas) {
		this.travelingdatas = travelingdatas;
	}

	public Set getCars() {
		return this.cars;
	}

	public void setCars(Set cars) {
		this.cars = cars;
	}

}