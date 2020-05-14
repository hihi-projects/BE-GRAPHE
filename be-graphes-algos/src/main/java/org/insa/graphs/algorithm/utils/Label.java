package org.insa.graphs.algorithm.utils;

import org.insa.graphs.model.*;

public class Label implements Comparable<Label> {
	
	protected float cout ;
	private boolean marque;
	private boolean intas;
	private Node node ;
	private Node pere;

	// constructeur
	
	public Label (Node noeud) {
		
		this.marque = false;
		this.node = noeud;
		this.pere = null;
		this.intas = false;
		this.cout = Float.POSITIVE_INFINITY;
	
	}
	
	public void setMarque() {
		this.marque = true ;
	}
	
	public void setCout(float cout) {
		this.cout = cout ;
	}
	
	public void setFather (Node pere) {
		this.pere = pere ;
	}
	
	public void setIntas () {
		this.intas = true ;
	}
	
	public Node getNode() {
		return this.node;
	}
	
	public boolean getMarque() {
		return this.marque;
	}
	
	public float getCout() {
		return this.cout ;
	}
	
	public Node getPere() {
		return this.pere;
	}
	
	public boolean getIntas() {
		return this.intas;
	}
	
	// on utilise même un getteur pour obtenir le cout total : CT
	
	public float getCT() {
		return this.cout;
	}
	
	// notre fonction qui compare le CT de 2 label
	// return un entier qui représente le résultat de la comparaison
	
	public int compareTo(Label anotherone) {
		int resu ;
		if (this.getCT() == anotherone.getCT()) {
			resu = 0;
		}
		else if (this.getCT() < anotherone.getCT()) {
			resu = -1 ;
		}
		else {
			resu = 1; // supérieur 
		}
		return resu ;
	}
}
