package edu.rpi.starcraft;

import bwta.*;
import bwta.Region;
import bwapi.*;
import java.util.*;
import org.jgraph.*;
import org.jgrapht.graph.*;
import org.jgrapht.graph.AsWeightedGraph;
import org.jgrapht.graph.SimpleGraph;

public class MapManager extends Manager {
	
	private ArrayList<Chokepoint> chokepoints;
	
	private ArrayList<Region> regions;
	
	private AsWeightedGraph<Region, Chokepoint> mapGraph;
	
	
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
	
	public MapManager(DunaiBot creator) {
		super(creator);
		// TODO Auto-generated constructor stub
		chokepoints = new ArrayList<Chokepoint>(BWTA.getChokepoints());
		regions = new ArrayList<Region>(BWTA.getRegions());
		SimpleGraph<Region,Chokepoint> newGraph = new SimpleGraph<Region, Chokepoint>(Chokepoint.class);
		HashMap<Chokepoint,Double> weightMap = new HashMap<Chokepoint,Double>();
		for(Region region:regions){
			newGraph.addVertex(region);
		}
		for(Chokepoint chokepoint:chokepoints){
			Region regionOne, regionTwo;
			regionOne = chokepoint.getRegions().first;
			regionTwo = chokepoint.getRegions().second;
			double distance = regionOne.getDistance(regionTwo);
			newGraph.addEdge(regionOne, regionTwo, chokepoint);
			weightMap.put(chokepoint, distance);
		}
		mapGraph = new AsWeightedGraph<Region,Chokepoint>(newGraph, weightMap);
		
		
	}

}
