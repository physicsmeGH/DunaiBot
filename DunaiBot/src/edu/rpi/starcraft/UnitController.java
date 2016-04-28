package edu.rpi.starcraft;

import java.util.*;
import java.util.function.*;
import bwapi.*;
import bwta.*;
import bwta.Region;
import sun.misc.FloatingDecimal.BinaryToASCIIConverter;

public class UnitController {
	
	public static final List<UnitCommandType> attacks = Arrays.asList(UnitCommandType.Attack_Move,UnitCommandType.Attack_Unit);
	public static final List<UnitCommandType> moves = Arrays.asList(UnitCommandType.Move);
	public static final List<UnitCommandType> builds = Arrays.asList(UnitCommandType.Build);
	public static final List<UnitCommandType> casts = Arrays.asList(UnitCommandType.Use_Tech_Unit,UnitCommandType.Use_Tech_Position);
	
	public static final Map<UnitCommandType,Integer> commandToStatus;
	static{
		HashMap<UnitCommandType,Integer>  newMap = new HashMap<UnitCommandType,Integer> ();
		BiConsumer<List<UnitCommandType>, Integer> adder = (List<UnitCommandType> commandList, Integer status)->
			{	
				for(UnitCommandType commandType:commandList){
					newMap.put(commandType, status);
				}
			};
		adder.accept(attacks, Statuses.ATTACKING);
		adder.accept(casts, Statuses.CASTING);
		adder.accept(builds, Statuses.BUILDING);
		adder.accept(moves, Statuses.MOVING);
		
		commandToStatus = Collections.unmodifiableMap(newMap);
	}
	
	
	protected Unit unit;
	protected Queue<UnitCommand> commandQueue;
	protected int status;
	
	public void addCommand(UnitCommand command){
		commandQueue.add(command);
	}
	
	public void clearQueue(){
		commandQueue.clear();
	}
	
	public void actionOnFrame(){
		if(unit.isIdle()){
			if(commandQueue.isEmpty()){
				status = Statuses.IDLE;
			}
			else{
				UnitCommand currentCommand = commandQueue.peek();
				
				if(unit.canIssueCommand(currentCommand)){
					commandQueue.remove();
					unit.issueCommand(currentCommand);
					status = commandToStatus.get(currentCommand.getUnitCommandType());
				}
			}
		}
	}
	
	public UnitController(Unit unit) {
		this.unit = unit;
		commandQueue = new LinkedList<UnitCommand>();
		// TODO Auto-generated constructor stub
	}

}
