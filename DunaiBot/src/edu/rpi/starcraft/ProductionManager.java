package edu.rpi.starcraft;

import bwapi.*;

import java.util.*;

public class ProductionManager extends AbstractManager implements Manager{
	
	private Queue<Pair<UnitType, Integer>> trainingQueue;
	
	private HashMap<UnitType,Integer> trainingBuildings;
	
	private HashMap<UnitType, Integer> researchBuildings;
	
	public void setArmyTargetSize(int size){
		
	}
	
	public void setArmyTargetComposition(){
		
	}
	
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
	
	public void modTrainingBuilding(Unit building,int amount){
		int count = trainingBuildings.get(building.getType());
		trainingBuildings.put(building.getType(), count+amount);
	}
	
	public void modResearchBuilding(Unit building, int amount){
		int count = researchBuildings.get(building.getType());
		researchBuildings.put(building.getType(), count+amount);
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
		trainingBuildings.put(UnitType.Terran_Nuclear_Silo, 0);
		
		researchBuildings = new HashMap<UnitType,Integer>();
		researchBuildings.put(UnitType.Terran_Academy, 0);
		researchBuildings.put(UnitType.Terran_Armory, 0);
		researchBuildings.put(UnitType.Terran_Control_Tower,0);
		researchBuildings.put(UnitType.Terran_Covert_Ops,0);
		researchBuildings.put(UnitType.Terran_Engineering_Bay,0);
		researchBuildings.put(UnitType.Terran_Physics_Lab,0);
		researchBuildings.put(UnitType.Terran_Science_Facility,0);
		researchBuildings.put(UnitType.Terran_Machine_Shop,0);
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public void actionOnFrame() {
		// TODO Auto-generated method stub
		
	}

}
