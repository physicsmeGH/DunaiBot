package edu.rpi.starcraft;

import bwta.Region;
import bwapi.*;
import java.util.*;

public class EnhancedRegion {
	private Region region;
	
	private boolean isUnderControl;
	
	private boolean hasBase;
	
	private int mineralPatchCount;
	
	
	
	protected void addBuilding(Unit building){
		buildingList.add(building);
	}
	
	protected void removeBuilding(Unit building){
		buildingList.remove(building);
	}
	
	
	
	private ArrayList<Unit> buildingList;
	public EnhancedRegion(Region region) {
		this.region = region;
		buildingList = new ArrayList<Unit>();
		
		// TODO Auto-generated constructor stub
	}

}
