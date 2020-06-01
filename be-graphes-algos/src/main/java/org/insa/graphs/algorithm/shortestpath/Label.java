package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Node;
import org.insa.graphs.model.Graph;
import java.util.*;

public class Label implements Comparable<Label>{
	
/*--------------------------Attributs------------------------------------------------------*/
	
	private int current_NodeId ; 
	
	private boolean mark ; // le marquage 
	
	private double cost; // le coût 
	
	private int father; // le prédécent 
	
	private boolean aux ; // pour savoir s'il est dans le tas
	
	
/*-------------------------Constructeur---------------------------------------------------*/
	
	


	public Label(Node node) {
		this.current_NodeId = node.getId() ;  
		this.mark = false ; 
		this.cost = Double.POSITIVE_INFINITY; 
		this.father = 0; 
	}
	
	
/*--------------------les--Getteur---&---les---Setteur-------------------------------------*/
	
	
	public static ArrayList<Label> createAllLabelFromGraph(Graph graph) {
		ArrayList<Label> labels = new ArrayList<Label>();
		for(Node node: graph.getNodes()) {
			labels.add(new Label(node));
		}
		return labels;
	}


	public boolean isAux() {
		return aux;
	}

	public void setAux() {
		this.aux = true;
	}
	
	public boolean getMark() {
		return mark;
	}
	
	public void setMark() {
		this.mark = true;
	}

	public int getCurrent_NodeId() {
		return current_NodeId;
	}


	public void setCurrent_NodeId(int current_Node) {
		this.current_NodeId = current_Node;
	}

	
	public double getCost() {
		return cost;
	}
	
	public double getTotalCost() {
		return this.cost;
	}
	
	public void setCost(double cost) {
		this.cost = cost;
	}

	public int getFather() {
		return father;
	}

	public void setFather(int father) {
		this.father = father;
	}
	
	
	@Override
	public int compareTo(Label other) {
		return Double.compare(this.getTotalCost(), other.getTotalCost());
	}
	
	
}