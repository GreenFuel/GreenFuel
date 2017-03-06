package com.example.tr.greenfuel.light;

import java.util.Date;

public class MyRoute {
	private int vUP;
	private int vDOWN;
	private int vDRIVE;//道路驾驶速度
	private float oil;//前方所有道路总油耗
	private float distance;//距离
	private float oilNow;//当前道路油耗
	private Date x;//到达时刻
	private Date y;//离开时刻
	private String name;
	public MyRoute() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public MyRoute(int vUP, int vDOWN,float d,String name) {
		super();
		this.vUP = vUP;
		this.vDOWN = vDOWN;
		this.vDRIVE = vUP;
		this.oil = 0;
		this.distance = d;
		this.x = new Date();
		this.y = new Date();
		this.name = name;
	}
	
	
	public MyRoute(int vDRIVE, float oil, float oilNow,String name) {
		super();
		this.vDRIVE = vDRIVE;
		this.oil = oil;
		this.oilNow = oilNow;
		this.name = name;
	}

	public MyRoute(int vDRIVE, float oil, float oilNow,Date x, Date y,String name) {
		super();
		this.vDRIVE = vDRIVE;
		this.oil = oil;
		this.oilNow = oilNow;
		this.x = new Date(x.getTime());
		this.y = new Date(y.getTime());
		this.name = name;
	}
	public float getDistance() {
		return distance;
	}
	
	public float getOilNow() {
		return oilNow;
	}

	public void setOilNow(float oilNow) {
		this.oilNow = oilNow;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public int getvUP() {
		return vUP;
	}
	public void setvUP(int vUP) {
		this.vUP = vUP;
	}
	public int getvDOWN() {
		return vDOWN;
	}
	public void setvDOWN(int vDOWN) {
		this.vDOWN = vDOWN;
	}
	public int getvDRIVE() {
		return vDRIVE;
	}
	public void setvDRIVE(int vDRIVE) {
		this.vDRIVE = vDRIVE;
	}
	public float getOil() {
		return oil;
	}
	public void setOil(float oil) {
		this.oil = oil;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getX() {
		return x;
	}

	public void setX(Date x) {
		this.x = new Date(x.getTime());
	}

	public Date getY() {
		return y;
	}

	public void setY(Date y) {
		this.y = new Date(y.getTime());
	}

	@Override
	public String toString() {
		return "MyRoute{" +
				"vUP=" + vUP +
				", vDOWN=" + vDOWN +
				", vDRIVE=" + vDRIVE +
				", oil=" + oil +
				", distance=" + distance +
				", oilNow=" + oilNow +
				", x=" + x +
				", y=" + y +
				", name='" + name + '\'' +
				'}';
	}
}
