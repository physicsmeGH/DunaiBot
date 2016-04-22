import bwapi.*;
import bwta.BWTA;
import bwta.BaseLocation;
import java.util.*;
import java.util.Map.*;
import java.util.function.*;

import java.util.stream.Collectors;


public class TestBot1 extends DefaultBWListener {
	
	
	public static final int STATUS_IDLE = 0;
	public static final int STATUS_BUILDING = 1;
	public static final int STATUS_GATHERING = 2;
	public static final int STATUS_STUCK = 3;
	
	
	public static final BiConsumer<Unit, Position> FUNCTION_ATTACK_POSITION = 
			(unit, position) -> {unit.attack(position);};
			
	public static final BiConsumer<Unit, Position> FUNCTION_MOVE_POSITION =
			(unit, position) -> {unit.move(position);};
			
	public static final BiConsumer<Unit, Position> FUNCTION_PATROL_POSITION = 
			(unit, position) -> {unit.patrol(position);};
			
	private class BuilderUnits {
		private Map<Unit,Integer> units;
		
		private int numbers;
		
		private UnitType building;
		
		private Queue<UnitType> buidQueue;
		
		public BuilderUnits(){
			this.units = new HashMap<Unit,Integer>();
			this.numbers = 0;
			
		}
		public int getBuilderCount(){
			numbers = 0;
			for(Entry<Unit,Integer> entry:units.entrySet()){
				if(entry.getValue()==STATUS_IDLE||entry.getValue()==STATUS_BUILDING){
					numbers++;
				}
			}
			return numbers;
		}
		
		
		public void addBuilder(Unit unit){
			if(getBuilderCount()<5){
				units.put(unit, STATUS_IDLE);
				//numbers ++;
			}
			else{
				units.put(unit, STATUS_GATHERING);
			}
			/*if(units.get(unit)==null){
				units.put(unit, STATUS_IDLE);
			}
			else{
				
			}*/
		}
		
		public Unit getIdleBuilderUnit(){
			for(Entry<Unit,Integer> entry:units.entrySet()){
				if(entry.getValue()==STATUS_IDLE){
					return entry.getKey();
				}
			}
			return null;
		}
		
		public int build(UnitType type, TilePosition pos){
			Unit currentBuilderUnit = getIdleBuilderUnit();
			if(currentBuilderUnit.canBuild(type, pos)){
				currentBuilderUnit.build(type, pos);
				units.put(currentBuilderUnit, STATUS_BUILDING);
				this.getBuilderCount();
				return 1;
			}
			else{
				return -1;
			}
		}
		
		public void build(UnitType type){
			
			Unit currentBuilderUnit = getIdleBuilderUnit();
			if(currentBuilderUnit!=null){
				for(int i=-30;i<30;i++){
					for(int j=-30;j<30;j++){
						int newX,newY;
						newX = basePosition.getX()+i;
						newY = basePosition.getY()+j;
						TilePosition newBuildPosition =new TilePosition(newX,newY ); 
						if(build(type,newBuildPosition)>0){
							return;
						}
					}
				}
			}
		}
	}


    private Mirror mirror = new Mirror();

    private Game game;

    private Player self;
    
    private int workerCount;
    
    private int newWorkerCount;
    
    private BuilderUnits builder;
    
    private TilePosition basePosition;
    
    private Map<UnitType,Integer> unitCount;
    
    public void run() {
        mirror.getModule().setEventListener(this);
        mirror.startGame();
    }

    @Override
    public void onUnitCreate(Unit unit) {
    	UnitType type = unit.getType();
        System.out.println("New unit discovered " + type);
        modUnitCount(type, 1);
        
    }
    
    
    public void onUnitDestroy(Unit unit){
    	UnitType type = unit.getType();
    	System.out.println("Unit killed "+ type );
    	modUnitCount(type, -1);
    }
    
    private void modUnitCount(UnitType type, int number){
    	int count = getUnitCount(type); 
    	if(count == 0){
    		unitCount.put(type, 0);
    	}
    	unitCount.put(type, count + number);
    }
    
    private Integer getUnitCount(UnitType type){
    	if (unitCount.get(type)== null){
    		return 0;
    	}
    	else{
    		return unitCount.get(type);
    	}
    }
    @Override
    public void onStart() {
        game = mirror.getGame();
        self = game.self();
        workerCount = 4;
        unitCount = new HashMap<UnitType,Integer>();

        //Use BWTA to analyze map
        //This may take a few minutes if the map is processed first time!
        System.out.println("Analyzing map...");
        BWTA.readMap();
        BWTA.analyze();
        System.out.println("Map data ready");
        
        int i = 0;
        for(BaseLocation baseLocation : BWTA.getBaseLocations()){
        	System.out.println("Base location #" + (++i) + ". Printing location's region polygon:");
        	for(Position position : baseLocation.getRegion().getPolygon().getPoints()){
        		System.out.print(position + ", ");
        	}
        	System.out.println();
        }
        Unit myBase = getUnit(UnitType.Terran_Command_Center);
        basePosition = myBase.getTilePosition();
        builder = new BuilderUnits();
        
    }
    
    private Unit getUnit(UnitType type){
    	for(Unit myUnit:self.getUnits()){
    		if (myUnit.getType() == type){
    			return myUnit;
    		}
    	}
    	return null;
    }
    
    private void expand(){
    	TilePosition newBaseTilePosition =BWTA.getNearestBaseLocation(basePosition).getTilePosition();
    	builder.build(UnitType.Terran_Command_Center,newBaseTilePosition);
    }
    
    private List<Unit> getUnits(UnitType type){
    	List<Unit> output;
    	output = self.getUnits().stream().filter(e -> e.getType()== type).collect(Collectors.toList());
    	
    	return output;
    	//unit -> unit.getType() == type 
    }
    
    
    /*
    private void updateUnitCount(){
    	unitCount.clear();
    	for(Unit myUnit:self.getUnits()){
    		UnitType type = myUnit.getType();
    		Integer count = unitCount.get(myUnit.getType()); 
    		if(count == null){
    			unitCount.put(type, 1);
    		}
    		else{
    			unitCount.put(type, count+1);
    		}
    	}
    }*/
    
    /*private void setBuilder(){
    	if (builder.units.size() == null){
    		builder = new BuilderUnit(getUnit(UnitType.Terran_SCV));
    		game.drawTextMap(0, 300, "builder assigned");
    	}
    	else{
    		if(builder.unit.isIdle()){
    			builder.status = STATUS_IDLE;
    		}
    		game.drawTextMap(0, 300, "builder: "+builder.unit.getPosition().toString());
    	}
    }*/
    
    private void buildIfNotExist(UnitType type){
    	buildIfLessThan(type, 1);
    }
    
    private void buildIfLessThan(UnitType type, Integer number){
    	if(getUnitCount(type)<number){
    		builder.build(type);
    	}
    }
    
    
    private void groupAttack(List<Unit> selected, Position target){
    	selected.forEach(unit-> FUNCTION_ATTACK_POSITION.accept(unit, target));
    }
    
    private void groupMove(List<Unit> selected, Position target){
    	selected.forEach(unit-> FUNCTION_MOVE_POSITION.accept(unit, target));
    }
    
    private void groupPatrol(List<Unit> selected, Position target){
    	selected.forEach(unit-> FUNCTION_PATROL_POSITION.accept(unit, target));
    }
    private void queuelessTrain(Unit productionBuilding, UnitType toMake){
    	if(productionBuilding.getTrainingQueue().isEmpty()&&
    			self.minerals()>=toMake.mineralPrice()&&
    			self.gas()>=toMake.gasPrice()){
    		productionBuilding.train(toMake);
    	}
    }
   
    @Override
    public void onFrame() {
        //game.setTextSize(10);
        game.drawTextScreen(10, 10, "Playing as " + self.getName() + " - " + self.getRace());

        StringBuilder units = new StringBuilder("My units:\n");
        unitCount.entrySet().forEach(entry -> 
        	{
        		units.append(entry.getKey());
        		units.append(": ");  
        		units.append(entry.getValue());
        		units.append("\n");});
        units.append("\n" + "builders:" + builder.numbers +"\n");
        
        
        
        workerCount = getUnitCount(UnitType.Terran_SCV);
        //iterate through my units
        //updateUnitCount();
        
        if((self.supplyTotal()-self.supplyUsed())/(float)self.supplyTotal()<0.3){
        	if(self.minerals()>=100){
        		builder.build(UnitType.Terran_Supply_Depot);
        	}
        }
        if(self.minerals()>=300){
        	buildIfLessThan(UnitType.Terran_Barracks,6);
        	buildIfNotExist(UnitType.Terran_Engineering_Bay);
        }
        
        if(self.minerals()>=600){
        	expand();
        }
        
        if(getUnitCount(UnitType.Terran_Marine)>30){
        	groupPatrol(getUnits(UnitType.Terran_Marine), BWTA.getNearestBaseLocation(basePosition).getPosition());
        }
        for (Unit myUnit : self.getUnits()) {
            //units.append(myUnit.getType()).append(" ").append(myUnit.getTilePosition()).append("\n");
            
            
            //if there's enough minerals, train an SCV
            if (myUnit.getType() == UnitType.Terran_Command_Center && workerCount < 5+24 * unitCount.get(UnitType.Terran_Command_Center)) {
            	queuelessTrain(myUnit, UnitType.Terran_SCV);
            }
            
            if(myUnit.getType( )== UnitType.Terran_Barracks ){
            	queuelessTrain(myUnit, UnitType.Terran_Marine);
            }

            //if it's a worker and it's idle, send it to the closest mineral patch
            if(myUnit.isGatheringMinerals() || myUnit.isGatheringGas()){
            	builder.addBuilder(myUnit);
            }
            if (myUnit.getType().isWorker() && myUnit.isIdle()) {
                Unit closestMineral = null;
                builder.addBuilder(myUnit);

                //find the closest mineral
                for (Unit neutralUnit : game.neutral().getUnits()) {
                    if (neutralUnit.getType().isMineralField()) {
                        if (closestMineral == null || myUnit.getDistance(neutralUnit) < myUnit.getDistance(closestMineral)) {
                            closestMineral = neutralUnit;
                        }
                    }
                }

                //if a mineral patch was found, send the worker to gather it
                if (closestMineral != null) {
                    myUnit.gather(closestMineral, false);
                }
            }
        }
        workerCount = newWorkerCount;

        //draw my units on screen
        game.drawTextScreen(10, 25, units.toString());
    }

    public static void main(String[] args) {
        new TestBot1().run();
    }
}