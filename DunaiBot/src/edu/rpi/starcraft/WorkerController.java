package edu.rpi.starcraft;

import bwapi.Unit;
import bwapi.UnitCommand;

public class WorkerController extends UnitController {
	
	@Override
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
	public WorkerController(Unit unit) {
		super(unit);
		// TODO Auto-generated constructor stub
	}

}
