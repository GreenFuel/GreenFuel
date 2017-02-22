package com.example.tr.greenfuel.util;

import com.example.tr.greenfuel.entity.Travelingdata;

import java.util.ArrayList;

public class FuelCalculate {

	private static double density = 0.725;	//һ���������ʹ��93�����ͣ����ܶ�Ϊ0.725g/ml
	private static double[] basicFuelFactor = {52.14746,46.89057,42.77409,35.14694};
	//�ͺ����ӣ���λg/KM
	private static double[][] fuelFactor =
	{ 	{1002.876136,868.4222136,835.4000716,691.0994902},
		{306.3003184,267.2655391,253.5897871,208.8890037},
		{203.3901026,177.8638996,167.1075782,137.0507391},
		{149.7855264,131.3728657,123.2559146,100.32193},
		{126.8495542,111.7531798,104.0155557,84.48540493},
		{111.4969085,98.55295948,91.26205817,73.88510055},
		{97.57173411,86.84343778,79.8438338,64.66613581},
		{90.0032995,80.19258392,73.50896744,59.41644671},
		{81.24086862,72.68919678,66.26968928,53.60768339},
		{75.76842134,68.1281119,61.84582821,50.06485827},
		{70.33513256,63.33079742,57.38855508,46.52041446},
		{67.57990492,60.99760145,55.09762761,44.71690551},
		{63.28915874,57.2856175,51.64866134,41.98722438},
		{61.04083305,55.29914307,49.69542109,40.4831314},
		{55.47886527,50.32224793,45.12787572,36.88672069},
		{55.46267738,50.29208155,45.20228927,36.85363152},
		{53.39344311,48.44849915,43.56689416,35.5477748},
		{50.67888522,45.98757227,41.27529377,33.71865715},
		{49.53123982,45.06399097,40.45824749,33.06743076},
		{47.05624108,42.80948729,38.33284412,31.42855151},
		{45.98369789,41.95479378,37.45645784,30.77329277},
		{44.60551039,40.61409276,36.33317288,29.90031734},
		{43.30265398,39.43698206,35.26052407,29.02870536},
		{42.0542293,38.28790879,34.19433637,28.18266915},
		{41.09435754,37.40503786,33.42494576,27.51921796},
		{40.16091762,36.56532569,32.63415074,26.95877536},
		{39.85893138,36.32269143,32.33118367,26.74933833},
		{39.17812834,35.68457389,31.74663352,26.33120819},
		{38.95619294,35.46634446,31.49666679,26.12278306},
		{38.72129622,35.25637313,31.24188399,25.9557934},
		{37.98049649,34.61308425,30.59495114,25.52046755},
		{37.637229,34.34277301,30.3324756,25.33855514},
		{37.13040975,33.91547514,29.93168502,25.06844578},
		{36.39604722,33.20884346,29.25197782,24.67767265}
	};
	
	private static double FirstStdCatTDFuelCal(ArrayList<Travelingdata> travelingdatas){
		
		double fuelConsumption = 0;
		for( Travelingdata travelingdata:travelingdatas){
			switch(travelingdata.getTdRoadLv()){
				case 0 :
					fuelConsumption = fuelConsumption + 12.786 * Math.pow(travelingdata.getTdSpeed(), -0.7164) * basicFuelFactor[0]
							* travelingdata.getTdDistance();
					break;
				case 1 :
					fuelConsumption = fuelConsumption + 12.567 * Math.pow(travelingdata.getTdSpeed(), -0.7126) * basicFuelFactor[0]
							* travelingdata.getTdDistance();
					break;
				case 2 :
					fuelConsumption = fuelConsumption + 11.381 * Math.pow(travelingdata.getTdSpeed(), -0.6842) * basicFuelFactor[0]
							 * travelingdata.getTdDistance();
					break;
				default:
					break;
			}
		}
		return fuelConsumption;
	}
	
	private static double SecondStdCatTDFuelCal(ArrayList<Travelingdata> travelingdatas){
		
		double fuelConsumption = 0;
		for( Travelingdata travelingdata:travelingdatas){
			switch(travelingdata.getTdRoadLv()){
				case 0 :
					fuelConsumption = fuelConsumption + 12.214 * Math.pow(travelingdata.getTdSpeed(), -0.7014) * basicFuelFactor[1]
							* travelingdata.getTdDistance();
					break;
				case 1 :
					fuelConsumption = fuelConsumption + 11.87 * Math.pow(travelingdata.getTdSpeed(), -0.6939) * basicFuelFactor[1]
							* travelingdata.getTdDistance();
					break;
				case 2 :
					fuelConsumption = fuelConsumption + 10.728 * Math.pow(travelingdata.getTdSpeed(), -0.6657) * basicFuelFactor[1]
							 * travelingdata.getTdDistance();
					break;
				default:
					break;
			}
		}
		return fuelConsumption;
	}
	
	private static double ThirdStdCatTDFuelCal(ArrayList<Travelingdata> travelingdatas){
		
		double fuelConsumption = 0;
		for( Travelingdata travelingdata:travelingdatas){
			switch(travelingdata.getTdRoadLv()){
				case 0 :
					fuelConsumption = fuelConsumption + 12.989 * Math.pow(travelingdata.getTdSpeed(), -0.7233) * basicFuelFactor[2]
							* travelingdata.getTdDistance();
					break;
				case 1 :
					fuelConsumption = fuelConsumption + 12.891 * Math.pow(travelingdata.getTdSpeed(), -0.7221) * basicFuelFactor[2]
							* travelingdata.getTdDistance();
					break;
				case 2 :
					fuelConsumption = fuelConsumption + 11.691 * Math.pow(travelingdata.getTdSpeed(), -0.6943) * basicFuelFactor[2]
							 * travelingdata.getTdDistance();
					break;
				default:
					break;
			}
		}
		return fuelConsumption;
	}
	
	private static double FourthStdCatTDFuelCal(ArrayList<Travelingdata> travelingdatas){
		
		double fuelConsumption = 0;
		for( Travelingdata travelingdata:travelingdatas){
			switch(travelingdata.getTdRoadLv()){
				case 0 :
					fuelConsumption = fuelConsumption + 12.782 * Math.pow(travelingdata.getTdSpeed(), -0.7187) * basicFuelFactor[3]
							* travelingdata.getTdDistance();
					break;
				case 1 :
					fuelConsumption = fuelConsumption + 12.683 * Math.pow(travelingdata.getTdSpeed(), -0.7166) * basicFuelFactor[3]
							* travelingdata.getTdDistance();
					break;
				case 2 :
					fuelConsumption = fuelConsumption + 11.518 * Math.pow(travelingdata.getTdSpeed(), -0.691) * basicFuelFactor[3]
							 * travelingdata.getTdDistance();
					break;
				default:
					break;
			}
		}
		return fuelConsumption;
	}
	
	public static double CarFuelConsumptionCal(int carType,ArrayList<Travelingdata> travelingdatas){
		
		double fuelConsumption = 0;
		int temp;
		double speed;
		
		switch(carType){
			case 0:
				fuelConsumption = FirstStdCatTDFuelCal(travelingdatas);
				break;
			case 1:
				fuelConsumption = SecondStdCatTDFuelCal(travelingdatas);
				break;
			case 2:
				fuelConsumption = ThirdStdCatTDFuelCal(travelingdatas);
				break;
			case 3:
				fuelConsumption = FourthStdCatTDFuelCal(travelingdatas);
				break;
			default:
				break;
		}

		return fuelConsumption / 0.725;	//����λ��gת����ml
	}
}
