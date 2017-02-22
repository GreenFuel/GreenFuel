package com.example.tr.greenfuel.entity;

import java.sql.Timestamp;

/**
 * Roadhistoryinfo entity. @author MyEclipse Persistence Tools
 */

public class Roadhistoryinfo implements java.io.Serializable {

	// Fields

	private Integer roadHistoryId;
	private Road road;
	private Double roadOilRatio;
	private Double roadCoemRatio;
	private Double roadHcemRatio;
	private Double roadNoemRatio;
	private Short roadCongestionLv;
	private Short roadAverageSpeed;
	private Timestamp recordDate;

	// Constructors

	/** default constructor */
	public Roadhistoryinfo() {
	}

	/** full constructor */
	public Roadhistoryinfo(Road road, Double roadOilRatio,
			Double roadCoemRatio, Double roadHcemRatio, Double roadNoemRatio,
			Short roadCongestionLv, Short roadAverageSpeed, Timestamp recordDate) {
		this.road = road;
		this.roadOilRatio = roadOilRatio;
		this.roadCoemRatio = roadCoemRatio;
		this.roadHcemRatio = roadHcemRatio;
		this.roadNoemRatio = roadNoemRatio;
		this.roadCongestionLv = roadCongestionLv;
		this.roadAverageSpeed = roadAverageSpeed;
		this.recordDate = recordDate;
	}

	// Property accessors

	public Integer getRoadHistoryId() {
		return this.roadHistoryId;
	}

	public void setRoadHistoryId(Integer roadHistoryId) {
		this.roadHistoryId = roadHistoryId;
	}

	public Road getRoad() {
		return this.road;
	}

	public void setRoad(Road road) {
		this.road = road;
	}

	public Double getRoadOilRatio() {
		return this.roadOilRatio;
	}

	public void setRoadOilRatio(Double roadOilRatio) {
		this.roadOilRatio = roadOilRatio;
	}

	public Double getRoadCoemRatio() {
		return this.roadCoemRatio;
	}

	public void setRoadCoemRatio(Double roadCoemRatio) {
		this.roadCoemRatio = roadCoemRatio;
	}

	public Double getRoadHcemRatio() {
		return this.roadHcemRatio;
	}

	public void setRoadHcemRatio(Double roadHcemRatio) {
		this.roadHcemRatio = roadHcemRatio;
	}

	public Double getRoadNoemRatio() {
		return this.roadNoemRatio;
	}

	public void setRoadNoemRatio(Double roadNoemRatio) {
		this.roadNoemRatio = roadNoemRatio;
	}

	public Short getRoadCongestionLv() {
		return this.roadCongestionLv;
	}

	public void setRoadCongestionLv(Short roadCongestionLv) {
		this.roadCongestionLv = roadCongestionLv;
	}

	public Short getRoadAverageSpeed() {
		return this.roadAverageSpeed;
	}

	public void setRoadAverageSpeed(Short roadAverageSpeed) {
		this.roadAverageSpeed = roadAverageSpeed;
	}

	public Timestamp getRecordDate() {
		return this.recordDate;
	}

	public void setRecordDate(Timestamp recordDate) {
		this.recordDate = recordDate;
	}

}