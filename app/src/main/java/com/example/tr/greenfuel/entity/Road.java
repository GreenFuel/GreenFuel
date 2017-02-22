package com.example.tr.greenfuel.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * Road entity. @author MyEclipse Persistence Tools
 */

public class Road implements java.io.Serializable {

	// Fields

	private Integer roadId;
	private String roadName;
	private Short roadLv;
	private Integer roadLength;
	private Double roadCenterLng;
	private Double roadCenterLat;
	private Set travelingdatas = new HashSet(0);
	private Set roadhistoryinfos = new HashSet(0);

	// Constructors

	/** default constructor */
	public Road() {
	}

	/** minimal constructor */
	public Road(String roadName, Short roadLv, Integer roadLength,
			Double roadCenterLng, Double roadCenterLat) {
		this.roadName = roadName;
		this.roadLv = roadLv;
		this.roadLength = roadLength;
		this.roadCenterLng = roadCenterLng;
		this.roadCenterLat = roadCenterLat;
	}

	/** full constructor */
	public Road(String roadName, Short roadLv, Integer roadLength,
			Double roadCenterLng, Double roadCenterLat, Set travelingdatas,
			Set roadhistoryinfos) {
		this.roadName = roadName;
		this.roadLv = roadLv;
		this.roadLength = roadLength;
		this.roadCenterLng = roadCenterLng;
		this.roadCenterLat = roadCenterLat;
		this.travelingdatas = travelingdatas;
		this.roadhistoryinfos = roadhistoryinfos;
	}

	// Property accessors

	public Integer getRoadId() {
		return this.roadId;
	}

	public void setRoadId(Integer roadId) {
		this.roadId = roadId;
	}

	public String getRoadName() {
		return this.roadName;
	}

	public void setRoadName(String roadName) {
		this.roadName = roadName;
	}

	public Short getRoadLv() {
		return this.roadLv;
	}

	public void setRoadLv(Short roadLv) {
		this.roadLv = roadLv;
	}

	public Integer getRoadLength() {
		return this.roadLength;
	}

	public void setRoadLength(Integer roadLength) {
		this.roadLength = roadLength;
	}

	public Double getRoadCenterLng() {
		return this.roadCenterLng;
	}

	public void setRoadCenterLng(Double roadCenterLng) {
		this.roadCenterLng = roadCenterLng;
	}

	public Double getRoadCenterLat() {
		return this.roadCenterLat;
	}

	public void setRoadCenterLat(Double roadCenterLat) {
		this.roadCenterLat = roadCenterLat;
	}

	public Set getTravelingdatas() {
		return this.travelingdatas;
	}

	public void setTravelingdatas(Set travelingdatas) {
		this.travelingdatas = travelingdatas;
	}

	public Set getRoadhistoryinfos() {
		return this.roadhistoryinfos;
	}

	public void setRoadhistoryinfos(Set roadhistoryinfos) {
		this.roadhistoryinfos = roadhistoryinfos;
	}

}