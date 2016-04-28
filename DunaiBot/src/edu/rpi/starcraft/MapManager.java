package edu.rpi.starcraft;

import bwta.*;
import bwta.Region;
import bwapi.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.jgraph.*;
import org.jgrapht.graph.*;
import org.jgrapht.graph.AsWeightedGraph;
import org.jgrapht.graph.SimpleGraph;

public class MapManager extends AbstractManager implements Manager {
	
	private ArrayList<Chokepoint> chokepoints;
	
	private ArrayList<Region> regions;
	
	private AsWeightedGraph<EnhancedRegion, Chokepoint> mapGraph;
	
	protected Map<Pair<Integer,Integer>,EnhancedRegion> regionMap;
	
	public EnhancedRegion getNearestRegion(Position pos){
		Comparator<Entry<Pair<Integer,Integer>,EnhancedRegion>> c = (Entry<Pair<Integer,Integer>,EnhancedRegion> ent1,Entry<Pair<Integer,Integer>,EnhancedRegion> ent2)->{
			Pair<Integer,Integer> pos1,pos2;
			pos1 = ent1.getKey();
			pos2 = ent2.getKey();
			int deltaXOne = pos1.first-pos.getX();
			int deltaYOne = pos1.second-pos.getY();
			int deltaXTwo = pos2.first-pos.getX();
			int deltaYTwo = pos2.second-pos.getY();
			return Integer.compare(deltaXOne*deltaXOne+deltaYOne*deltaYOne, deltaXTwo*deltaXTwo+deltaYTwo*deltaYTwo);
		};
		List<Entry<Pair<Integer,Integer>,EnhancedRegion>> sortedRegions = regionMap.entrySet().stream().collect(Collectors.toList());
		sortedRegions.sort(c);
		return sortedRegions.get(0).getValue();
	}
	
	
	public void visualizeRegion(bwapi.Region region){
		int left, top, right, bottom;
		left = region.getBoundsLeft();
		top = region.getBoundsTop();
		right = region.getBoundsRight();
		bottom = region.getBoundsBottom();
		ownerBot.getGame().drawBoxMap(left, top, right, bottom, Color.Teal);		
	}
	public void drawPolygon(List<Position> points){
		Iterator<Position> iterator = points.iterator();
		Position first = points.get(0);
		Position previous = first;
		Position current = first;
		for(int i=1;i<points.size();i++){
			current = points.get(i);
			ownerBot.getGame().drawLineMap(previous, current, Color.Black);
			previous = current;
		}
		ownerBot.getGame().drawLineMap(first, current, Color.Black);
		
	}
	
	public void visualizeRegion(Region region){
		/*for (Position position : region.getPolygon().getPoints()){
			ownerBot.getGame().drawCircleMap(position, 2, Color.Black);
		}*/
		drawPolygon(region.getPolygon().getPoints());
	}
	public void visualizeChokepoint(Chokepoint chokepoint){
		int radius = (int)chokepoint.getWidth();
		Position pos = chokepoint.getCenter();
		ownerBot.drawCircleAt(pos, radius, Color.Purple);
		Region regionOne, regionTwo;
		Pair<Region,Region> regionPair = chokepoint.getRegions();
		regionOne = regionPair.first;
		regionTwo = regionPair.second;
		ownerBot.getGame().drawLineMap(regionOne.getCenter(), regionTwo.getCenter(), Color.Orange);
	}
	
	public void tacticalOverlay(){
		
		for(Chokepoint point:chokepoints){
			visualizeChokepoint(point);
		}
		
		for(Region region:regions){
			visualizeRegion(region);
		}
	}
	private Pair<Integer,Integer> regionToCord(Region region){
		return new Pair<Integer,Integer>(region.getCenter().getX(),region.getCenter().getY());
	}
	public MapManager(DunaiBot creator) {
		super(creator);
		// TODO Auto-generated constructor stub
		chokepoints = new ArrayList<Chokepoint>(BWTA.getChokepoints());
		regions = new ArrayList<Region>(BWTA.getRegions());
		SimpleGraph<EnhancedRegion,Chokepoint> newGraph = new SimpleGraph<EnhancedRegion, Chokepoint>(Chokepoint.class);
		Map<Chokepoint,Double> weightMap = new HashMap<Chokepoint,Double>();
		regionMap = new HashMap<Pair<Integer,Integer>,EnhancedRegion>();
		for(Region region:regions){
			EnhancedRegion eRegion = new EnhancedRegion(region);
			newGraph.addVertex(eRegion);
			regionMap.put(regionToCord(region), eRegion);
			
			
			
		}
		for(Chokepoint chokepoint:chokepoints){
			EnhancedRegion regionOne, regionTwo;
			regionOne = regionMap.get(regionToCord(chokepoint.getRegions().first));
			regionTwo = regionMap.get(regionToCord(chokepoint.getRegions().second));
			double distance = regionOne.region.getDistance(regionTwo.region);
			if(!regionOne.equals(regionTwo)){
				newGraph.addEdge(regionOne, regionTwo, chokepoint);
				weightMap.put(chokepoint, distance);
			}
		}
		mapGraph = new AsWeightedGraph<EnhancedRegion,Chokepoint>(newGraph, weightMap);
		
		
		
	}


	@Override
	public void actionOnFrame() {
		// TODO Auto-generated method stub
		
	}

}
