package org.insa.graphs.algorithm.utils;

import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.model.Node;

public class LabelStar extends Label {
	protected float Coutestimer;  

	
	protected double CbCoutestimer(int NodeId, ShortestPathData data) {
    	AbstractInputData.Mode mode = data.getMode(); 
    	double d = data.getGraph().get(NodeId).getPoint().distanceTo(data.getDestination().getPoint()); 
    	return (mode == Mode.LENGTH) ? d : (d/((Math.max(data.getGraph().getGraphInformation().getMaximumSpeed(), data.getMaximumSpeed()) / 3.6))); 
    }

	public LabelStar(Node noeud, ShortestPathData data) {
		super(noeud);
		this.Coutestimer= (float) CbCoutestimer(noeud.getId(), data); 
	}
	

	public double getCT() {
		return this.cout + this.Coutestimer; 
	}
}
