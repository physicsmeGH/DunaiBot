package edu.rpi.starcraft;

import bwapi.*;
import bwta.*;
import bwta.Region;
import java.util.*;


public class BuildingManager extends AbstractManager implements Manager {
	
	private Pair<Integer,Integer> queuedResources;
	private Pair<Integer,Integer> usableResources;
	private Queue<Pair<UnitType,EnhancedRegion>> buildingQueue;
	
	
	
	
	public int buildInRegion(Region region, UnitType building){
		
		
		return 0;
		
	}
	
	
	
	public void finalizeBuilding(Unit unit,UnitType type, int status){
		queuedResources = new Pair<Integer,Integer>(queuedResources.first-type.mineralPrice(),queuedResources.second-type.gasPrice());
		if(status ==0){
			//building successful
			
		}
		else{
			//building failed
			addToBuildingQueue(type, ownerBot.mapManager.getNearestRegion(unit.getPosition()));
		}
		
	}
	
	public boolean canAfford(UnitType type){
		if(type.mineralPrice()<=getUsableMinerals()&&type.gasPrice()<=getUsableGases()){
			return true;
		}
		else{
			return false;
		}
	}
	
	private void reduceQueue(){
		while(!buildingQueue.isEmpty()&& canAfford(buildingQueue.peek().first)){
			//removes first task in queue and carry it outs
		}
	}
	
	public int getUsableMinerals(){
		return usableResources.first;
	}
	
	public int getUsableGases(){
		return usableResources.second;
	}
	public void updateInfo(){
		usableResources.first = ownerBot.getPlayer().minerals() - queuedResources.first;
		usableResources.second = ownerBot.getPlayer().gas() - queuedResources.second;
		
	}
	public void addToBuildingQueue(UnitType buildingType, Position pos){
		addToBuildingQueue(buildingType, ownerBot.mapManager.getNearestRegion(pos));
	}
	
	public void addToBuildingQueue(UnitType buildingType,EnhancedRegion region){
		buildingQueue.add(new Pair(buildingType,region));
	}
	
	public BuildingManager(DunaiBot creator) {
		super(creator);
		queuedResources = new Pair<Integer,Integer>(0,0);
		usableResources = new Pair<Integer,Integer>(0,0);
		buildingQueue = new LinkedList<Pair<UnitType,EnhancedRegion>>();
		// TODO Auto-generated constructor stub
	}



	@Override
	public void actionOnFrame() {
		// TODO Auto-generated method stub
		
	}
	
}
