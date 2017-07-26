package edu.wit.cs.comp3370;

import java.util.ArrayList;
import java.util.LinkedList;

// represents a vertex in a graph, including a unique ID to keep track of vertex
public class Vertex{
	double c;
	Vertex parent;
	ArrayList<Vertex> neighbors;
	public Vertex(){
		c = Double.POSITIVE_INFINITY;
		parent = null;
		neighbors = new ArrayList<Vertex>();
	}
	
	/********************************************
	 * 
	 * You shouldn't modify anything past here
	 * 
	 ********************************************/

	public double x;
	public double y;
	public int ID;
	
}
