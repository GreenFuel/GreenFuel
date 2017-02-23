package com.example.tr.greenfuel.util;

public class EmissionCalculate {

	/** 
	 * Input��	a--�������ٶ�
	 * 			v--������˲ʱ�ٶȣ���λΪkm/h
	 * Output��	NOEmission--����������ŷ�������λΪmg/s
	 * function�����㳵������������ŷ���
	 */
	public static double NOEmissionCal(double a, double v){
		double NOEmission = 0;
		NOEmission += -1.646013 * a + 0.257026 * Math.pow(a, 2) + 0.59187 * Math.pow(a, 3) +
						0.162353 * a * v + -0.034504 * Math.pow(a, 2) * v + -0.008106 * Math.pow(a, 3) * v +
						-0.002891 * a * Math.pow(v, 2) + 0.000896 * Math.pow(a, 2) * Math.pow(v, 2) + 
						0.000262 * Math.pow(a, 3) * Math.pow(v, 2) + 0.361401 * v + -0.005651 * Math.pow(v, 2)+
						-0.113739;
		return NOEmission;
	}
	
	/** 
	 * Input��	a--�������ٶ�
	 * 			v--������˲ʱ�ٶȣ���λΪkm/h
	 * Output��	HCEmission--̼�⻯������ŷ�������λΪmg/s
	 * function�����㳵��̼�⻯������ŷ���
	 */
	public static double HCEmissionCal(double a, double v){
		double HCEmission = 0;
		HCEmission += -0.480287 * a + 0.017818 * Math.pow(a, 2) + 0.074631 * Math.pow(a, 3) +
						0.070015 * a * v + -0.003120 * Math.pow(a, 2) * v + -0.010206 * Math.pow(a, 3) * v +
						-0.002256 * a * Math.pow(v, 2) + 0.0000678 * Math.pow(a, 2) * Math.pow(v, 2) + 
						0.000341 * Math.pow(a, 3) * Math.pow(v, 2) + 0.0000208 * a * Math.pow(v, 3)+
						-0.00000315 * Math.pow(a, 3) * Math.pow(v, 3) + 0.32044 * v + -0.000597 * Math.pow(v, 2) + 
						0.412267;
		return HCEmission;
	}
	
	/** 
	 * Input��	a--�������ٶ�
	 * 			v--������˲ʱ�ٶȣ���λΪkm/h
	 * Output��	COEmission--һ����̼���ŷ�������λΪmg/s
	 * function�����㳵��һ����̼���ŷ���
	 */
	public static double COEmissionCal(double a, double v){
		double COEmission = 0;
		COEmission += 7.115069 * a + 7.299528 * Math.pow(a, 2) + 1.507933 * Math.pow(a, 3) +
						-0.592252 * a * v + -0.724626 * Math.pow(a, 2) * v + -0.139018 * Math.pow(a, 3) * v +
						0.011174 * a * Math.pow(v, 2) + 0.13234 * Math.pow(a, 2) * Math.pow(v, 2) + 
						0.002491 * Math.pow(a, 3) * Math.pow(v, 2) + 1.403942 * v + 1.403942 * Math.pow(v, 2) + 
						13.288111;
		return COEmission;
	}

	public static double cEmissionCal(double v){
		double c = 0;
		c = (0.0025*v*v-0.2554*v+31.75)*0.725*43f;
		return c*(74100+3*25+0.6*298);
	}
}
