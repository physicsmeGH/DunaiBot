package edu.rpi.starcraft;

import bwapi.*;
import bwta.*;


public class TechManager extends AbstractManager implements Manager {
	
	public static final int TECH_INFANTRY = 0;	//marines, bunkers
	public static final int TECH_ADVANCED_INFANTRY = 1;	//marines, firebats, medics, turrets, comsat
	public static final int TECH_MECHANIC = 2; 	//vultures, siege tanks
	public static final int TECH_ADVANCED_MECHANIC = 3;	//goliaths
	public static final int TECH_FLYER = 4;		//Wraith
	public static final int TECH_FLYER_ADVANCED =5;		//Dropship, Valkyrie
	public static final int TECH_SCIENCE = 6;			//science vessels
	public static final int TECH_YAMATO = 7;			//battlecruiser
	public static final int TECH_GHOST = 8;				//ghosts, silos, nukes
	
	
	boolean[] targetTechLevels= {false,false,false,false,false,false,false,false,false};
	boolean[] currentTechLevels = {false,false,false,false,false,false,false,false,false};
	public TechManager(DunaiBot creator) {
		super(creator);
		
	}

	@Override
	public void actionOnFrame() {
		// TODO Auto-generated method stub
		
	}

}
