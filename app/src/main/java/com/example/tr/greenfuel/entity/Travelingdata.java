package com.example.tr.greenfuel.entity;

import java.sql.Timestamp;

/**
 * Travelingdata entity. @author MyEclipse Persistence Tools
 */

public class Travelingdata implements java.io.Serializable {

	// Fields

	private int tdId;
	private Driver driver;
	private Road road;
	private Routeinfo routeinfo;
	private double tdDistance;	//��ʻͨ����һ��·�εĳ���
	private int tdSpeed;		//��ʻͨ����һ��·�εľ���
	private Timestamp tdTime;
	private int tdRoadLv;		//��·�ȼ�����ģ���з�Ϊ����·������·�����θ�·��֧·�����֣��ֱ���0,1,2����

	// Constructors

	/** default constructor */
	public Travelingdata() {
	}

	/** full constructor */
	public Travelingdata(Driver driver, Road road, Routeinfo routeinfo,
						 double tdDistance, int tdSpeed, Timestamp tdTime, int tdRoadLv) {
		this.driver = driver;
		this.road = road;
		this.routeinfo = routeinfo;
		this.tdDistance = tdDistance;
		this.tdSpeed = tdSpeed;
		this.tdTime = tdTime;
		this.tdRoadLv = tdRoadLv;
	}

	public Travelingdata(int tdSpeed,double tdDistance,int tdRoadLv){
		this.tdSpeed = tdSpeed;
		this.tdDistance = tdDistance;
		this.tdRoadLv = tdRoadLv;
	}
	// Property accessors

	public int getTdId() {
		return this.tdId;
	}

	public void setTdId(int tdId) {
		this.tdId = tdId;
	}

	public Driver getDriver() {
		return this.driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public Road getRoad() {
		return this.road;
	}

	public void setRoad(Road road) {
		this.road = road;
	}

	public Routeinfo getRouteinfo() {
		return this.routeinfo;
	}

	public void setRouteinfo(Routeinfo routeinfo) {
		this.routeinfo = routeinfo;
	}

	public double getTdDistance() {
		return this.tdDistance;
	}

	public void setTdDistance(double tdDistance) {
		this.tdDistance = tdDistance;
	}

	public int getTdSpeed() {
		return this.tdSpeed;
	}

	public void setTdSpeed(int tdSpeed) {
		this.tdSpeed = tdSpeed;
	}

	public Timestamp getTdTime() {
		return this.tdTime;
	}

	public void setTdTime(Timestamp tdTime) {
		this.tdTime = tdTime;
	}

	public int getTdRoadLv() {
		return this.tdRoadLv;
	}

	public void setTdRoadLv(int tdRoadLv) {
		this.tdRoadLv = tdRoadLv;
	}

}