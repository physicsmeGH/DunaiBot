package edu.rpi.starcraft;

import bwapi.*;
import bwta.*;
import bwta.Region;
import java.util.*;


public class BuildingManager extends Manager {
	
	private Pair<Integer,Integer> queuedResources;
	private Pair<Integer,Integer> usableResources;
	private Queue<Pair<UnitType,EnhancedRegion>> buildingQueue;
	
	
	
	
	public int buildInRegion(Region region, UnitType building){
		
		
		return 0;
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
	
}
