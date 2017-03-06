package com.example.tr.greenfuel.light;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Light {
	private MyLoction loc;
	private int period;
	private int duration;
	private Date start;
	private int[] minEst;

	public Light() {

	}

	public Light(MyLoction loc, int period, int duration, Date start) {
		super();
		this.loc = loc;
		this.period = period;
		this.duration = duration;
		this.start = start;
	}
	public MyLoction getLoc() {
		return loc;
	}
	public void init(int n){
		setMinEst(n);

	}
	public void setMinEst(int n){
		minEst = new int[n+1];
		for(int j = 0; j<n+1; j++){
			minEst[j] =Integer.MAX_VALUE;
		}
	}
	public void setLoc(MyLoction loc) {
		this.loc = loc;
	}
	public int getPeriod() {
		return period;
	}
	public void setPeriod(int period) {
		this.period = period;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}

	public boolean getState(Date d){
		int t = (int) ((int)(d.getTime() - this.start.getTime())/1000f);
		int sy = 0;
		sy = t%this.period;
		if(sy<0&&(sy+this.period<this.duration)){
			return true;
		}
		if(sy>0&&sy<this.duration){
			return true;
		}
		return false;
	}

	public Date getNearestLast(Date d,int no){
		int t = (int) ((int)(d.getTime() - this.start.getTime())/1000f);
		int sy = 0;
		sy = t%this.period;
		Date nearest = new Date(d.getTime());
		if(sy<0){
			if(sy+period<duration){
				return d;
			}
			nearest.setTime(nearest.getTime()-(sy+this.period)*1000);
		}
		if(sy>0){
			if(sy<duration){
				return d;
			}
			nearest.setTime(nearest.getTime()-sy*1000);
		}
		nearest.setTime(nearest.getTime()-period*1000*no);
		return nearest;
	}
	public void F(Date x, Date y, int i, float oilValue,  List<Light> ls,  List<MyRoute> rs,final List<MyRoute> rr){
		Date yy = new Date();
		Date xx = new Date();
		int vv = 0;
		float mm = Integer.MAX_VALUE;

		for(int vj = rs.get(i).getvUP();vj >= rs.get(i).getvDOWN();vj = vj-2) {
			Date nextX = new Date(y.getTime()+(long)((rs.get(i).getDistance()/(float)vj)*3600*1000));
			Date nextY = new Date(ls.get(i).getNearestLast(nextX, -1).getTime());
			//float value = oilValue+fmove(nextX,y,rs.get(i).getDistance())+fdelay(nextX,nextY);
			float value = oilValue+fmove2(vj,rs.get(i).getDistance())+fdelay(nextX,nextY);
			if(i == ls.size()-1) {
				if(value < minEst[i]) {
					minEst[i] = (int) value;
					rs.get(i).setX(nextX);
					rs.get(i).setY(nextY);
					rs.get(i).setOil(value);
					rs.get(i).setvDRIVE(vj);
					rs.get(i).setOilNow(fmove2(vj,rs.get(i).getDistance())+fdelay(nextX,nextY));
					rr.removeAll(rr);
					for(MyRoute r : rs){
						rr.add(new MyRoute(r.getvDRIVE(),r.getOil(),r.getOilNow(),r.getX(),r.getY(),r.getName()));
					}
				}
			}
			if(nextY.getTime() != nextX.getTime()) {
				if(value<mm){
					yy = nextY;
					xx = nextX;
					vv = vj;
					mm = value;
				}
			}else{
				if(i == ls.size()-1) {
					break;
				}
				rs.get(i).setX(nextX);
				rs.get(i).setY(nextY);
				rs.get(i).setOil(value);
				rs.get(i).setvDRIVE(vj);
				rs.get(i).setOilNow(fmove2(vj,rs.get(i).getDistance())+fdelay(nextX,nextY));
				F(nextX, nextY, i+1, value, ls, rs,rr);
			}
		}
		if(i != (ls.size()-1) &&mm != Integer.MAX_VALUE) {
			rs.get(i).setX(xx);
			rs.get(i).setY(yy);
			rs.get(i).setOil(mm);
			rs.get(i).setvDRIVE(vv);
			rs.get(i).setOilNow(mm-oilValue);
			F(xx, yy, i+1, mm, ls, rs,rr);
		}

	}
//	public  void F(Date x, Date y, int i, float oilValue,  List<Light> ls,  List<MyRoute> rs,final List<MyRoute> rr){
//		Date yy = new Date();
//		Date xx = new Date();
//		int vv = 0;
//		float mm = Integer.MAX_VALUE;
//
//		for(int vj = rs.get(i).getvUP();vj >= rs.get(i).getvDOWN();vj = vj-2) {
//			Date nextX = new Date(y.getTime()+(long)((rs.get(i).getDistance()/(float)vj)*3600*1000));
//			Date nextY = new Date(ls.get(i).getNearestLast(nextX, -1).getTime());
//			//float value = oilValue+fmove(nextX,y,rs.get(i).getDistance())+fdelay(nextX,nextY);
//			float value = oilValue+fmove2(vj,rs.get(i).getDistance())+fdelay(nextX,nextY);
//			if(value < minEst[i]) {
//				minEst[i] = (int) value;
//				rs.get(i).setX(nextX);
//				rs.get(i).setY(nextY);
//				rs.get(i).setOil(value);
//				rs.get(i).setvDRIVE(vj);
//				rs.get(i).setOilNow(fmove2(vj,rs.get(i).getDistance())+fdelay(nextX,nextY));
//				rr.removeAll(rr);
//				for(MyRoute r : rs){
//					rr.add(new MyRoute(r.getvDRIVE(),r.getOil(),r.getOilNow(),r.getX(),r.getY(),r.getName()));
//				}
//				yy = nextY;
//				xx = nextX;
//				vv = vj;
//				mm = value;
//				if(i != (rs.size()-1))
//					F(xx, yy, i+1, mm, ls, rs,rr);
//			}
//		}
////		if(i != (rs.size()-1) &&mm != Integer.MAX_VALUE) {
////			rs.get(i).setX(xx);
////			rs.get(i).setY(yy);
////			rs.get(i).setOil(mm);
////			rs.get(i).setvDRIVE(vv);
////			rs.get(i).setOilNow(mm-oilValue);
////			F(xx, yy, i+1, mm, ls, rs,rr);
////		}
//	}

	public float fmove(Date x ,Date y , float l) {
		int speed = (int)(l/(((x.getTime()-y.getTime())/1000/3600)));
		//return (float) ((0.0025*speed*speed-0.255*speed+31.75));
		System.out.println("t:"+(x.getTime()-y.getTime()));
		System.out.println("v:"+speed+"  "+0.9617*Math.pow(speed/2,0.3142)/100*l);

		return (float) (12.786*Math.pow(speed,-0.7164)/100*l);
		//return (float) (0.9617*Math.pow(speed/2,0.3142)/100*l);
	}

	public float fmove2(int speed , float l){
		//return (float) (((12.567*Math.pow(speed,-0.7126))/100)*l)*1000;
		//return (float) ((0.0025*speed*speed-0.255*speed+31.75)*l*10);
		return (float) ((0.9617*Math.pow(speed/2,0.3142)*l)*10);
	}

	public float fdelay(Date x ,Date y){

		return (float)(((y.getTime()-x.getTime())/3600*3.2));
	}

	public Date getNearestNext(Date d){
		int t = (int) ((int)(d.getTime() - this.start.getTime())/1000f);
		int sy = 0;
		sy = t%this.period;
		Date nearest = new Date(d.getTime());
		if(sy<0){
			nearest.setTime(nearest.getTime()-sy*1000);
		}
		if(sy>0){
			nearest.setTime(nearest.getTime()-sy*1000+period*1000);
		}
		return nearest;
	}

	public static void main(String[] arg) {

		List<MyRoute> rs = new ArrayList<MyRoute>();//设置的8条道路

		rs.add(new MyRoute(70,30,0.5f,"1"));
		rs.add(new MyRoute(40,20,0.5f,"1"));
		rs.add(new MyRoute(60,30,1f,"1"));
		rs.add(new MyRoute(70,40,0.8f,"1"));
		rs.add(new MyRoute(70,30,0.6f,"1"));
		rs.add(new MyRoute(50,30,0.45f,"1"));
		rs.add(new MyRoute(60,30,0.76f,"1"));
		rs.add(new MyRoute(30,10,0.6f,"1"));

		List<Light> ls = new ArrayList<Light>();//8个红绿灯路口

		ls.add(new Light(new MyLoction(30.762219, 103.997244), 170, 70,
				new Date((9*3600+42*60+47)*1000)));
		ls.add(new Light(new MyLoction(30.762219, 103.997244), 93, 60,
				new Date((9*3600+43*60+46)*1000)));
		ls.add(new Light(new MyLoction(30.762219, 103.997244), 73, 40,
				new Date((9*3600+42*60+20)*1000)));
		ls.add(new Light(new MyLoction(30.762219, 103.997244), 76, 50,
				new Date((9*3600+42*60+42)*1000)));
		ls.add(new Light(new MyLoction(30.762219, 103.997244), 140, 48,
				new Date((9*3600+43*60+11)*1000)));
		ls.add(new Light(new MyLoction(30.762219, 103.997244), 112, 70,
				new Date((9*3600+43*60+42)*1000)));
		ls.add(new Light(new MyLoction(30.762219, 103.997244), 130, 65,
				new Date((9*3600+42*60+10)*1000)));
		ls.add(new Light(new MyLoction(30.762219, 103.997244), 139, 80,
				new Date((9*3600+42*60+16)*1000)));
		Light ll = new Light(new MyLoction(30.762219, 103.997244), 139, 80,
				new Date((9*3600+42*60+16)*1000));

		ll.setMinEst(8);//初始化最小值

		List<MyRoute> rr = new ArrayList<MyRoute>();
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

		//开始计算
		long tnow = System.currentTimeMillis();
		ll.F(new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), 0, 0, ls, rs,rr);

		//输出结果
		System.out.println("用时："+(System.currentTimeMillis()-tnow)/1000);
		System.out.println("油耗End-----："+rs.get(7).getOil());
		int j = 0;
		for(MyRoute r : rr){
			System.out.println("v"+(++j)+":"+r.getvDRIVE()+" 到达时刻："+formatter.format(r.getX())+"离开时刻："+formatter.format(r.getY())+"\n"+" \t此路段油耗:"+r.getOilNow()+" 前方所有道路总油耗:"+r.getOil());
		}
	}
}
