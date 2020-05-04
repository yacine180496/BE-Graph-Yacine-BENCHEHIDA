package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Point;

public class LabelStar extends Label {
	private double costEstimated;
	
	public LabelStar(Node node,ShortestPathData data) {
		super(node);
		
		if(data.getMode()==AbstractInputData.Mode.LENGTH) {
			this.costEstimated = (double)Point.distance(node.getPoint(),data.getDestination().getPoint());
		}else { // TIME
			int speed = Math.max(data.getMaximumSpeed(), data.getGraph().getGraphInformation().getMaximumSpeed()); 
			this.costEstimated = (double) (Point.distance(node.getPoint(), data.getDestination().getPoint()) / speed*1000.d/3600.d);
		}
		
	}
	
	@Override
	public double getTotalCost() {
		return this.costEstimated + this.getCost();
	}
}
