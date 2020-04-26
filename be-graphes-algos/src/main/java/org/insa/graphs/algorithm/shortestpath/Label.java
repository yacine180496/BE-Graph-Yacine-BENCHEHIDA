package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Node;

public class Label implements Comparable<Label>{
	
/*----------------------Attributs--------------------------------------------------------*/
	
	private Node current_Node ; 
	
	private boolean mark ; // le marquage 
	
	private double cost; // le coût 
	
	private int father; // le prédécent 
	
/*---------------------Constructeur-------------------------------------------------------*/
	
	public Label(Node node) {
		this.current_Node = node ; 
		this.mark = false ; 
		this.cost = Double.POSITIVE_INFINITY; 
		this.father = 0; 
	}

/*--------------------les--Getteur---&---les---Setteur-------------------------------------*/

	public Node getCurrent_Node() {
		return current_Node;
	}

	public void setCurrent_Node(Node current_Node) {
		this.current_Node = current_Node;
	}

	public boolean isMark() {
		return mark;
	}

	public void setMark(boolean mark) {
		this.mark = mark;
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