package edu.rpi.starcraft;

import bwapi.*;

import java.util.*;

public class ProductionManager extends Manager {
	
	private Queue<Pair<UnitType, Integer>> trainingQueue;
	
	private HashMap<UnitType,Integer> trainingBuildings;
	
	private int queuelessTrain(Unit productionBuilding, UnitType toMake){
    	if(productionBuilding.getTrainingQueue().isEmpty()&&
    			ownerBot.buildingManager.getUsableMinerals()>=toMake.mineralPrice()&&
    			ownerBot.buildingManager.getUsableGases()>=toMake.gasPrice()){
    		productionBuilding.train(toMake);
    		return 0;
    	}
    	else{
    		return -1;
    	}
    }
	
	public void clearTrainingQueue(){
		trainingQueue.clear();
	}
	
	public void addTrainingBuilding(Unit building){
		int count = trainingBuildings.get(building.getType());
		trainingBuildings.put(building.getType(), count+1);
	}
	
	public void removeTrainingBuilding(Unit building){
		int count = trainingBuildings.get(building.getType());
		trainingBuildings.put(building.getType(), count-1);
	}
	
	public int addToTrainingQueue(UnitType type, int count){
		
		return 0;
	}

	public ProductionManager(DunaiBot creator) {
		super(creator);
		trainingQueue = new LinkedList<Pair<UnitType,Integer>>();
		trainingBuildings = new HashMap<UnitType,Integer>();
		trainingBuildings.put(UnitType.Terran_Barracks, 0);
		trainingBuildings.put(UnitType.Terran_Command_Center, 0);
		trainingBuildings.put(UnitType.Terran_Factory, 0);
		trainingBuildings.put(UnitType.Terran_Starport, 0);
		// TODO Auto-generated constructor stub
	}

}
