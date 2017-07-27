package edu.wit.cs.comp3370;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/* Calculates the minimal spanning tree of a graph 
 * 
 * Wentworth Institute of Technology
 * COMP 3370
 * Lab Assignment 9
 * Rachel Palmer
 */

public class LAB9 {
	
	/**
	 * FindMst first uses graph g and adds possible neighbors to every vertex (possible
	 * neighbors being those that are within distance less than epsilon). Then, it calls
	 * prim(Graph g) to add the MST edges to graph. These are the edges that return the
	 * smallest weight that will traverse the entire graph. The method prim uses Prim's
	 * algorithm and constructs a min-heap based on the costs of the vertices from the
	 * starting vertex (in this case, the value at index 0 is the start vertex). The
	 * vertex of smallest cost is removed from the heap each time, and the cost of its
	 * neighbors' is calculated. After every neighbor cost is updated, pushdown and
	 * pullup is called to re-organize the heap. Finally, every parent-child pair is
	 * added as an edge to the graph.
	 * @param g		graph generated from the points files
	 */
	public static void FindMST(Graph g) {
		//generate possible edges, as neighbors to vertices
		Vertex[] v = new Vertex[g.getVertices().length];
		v = g.getVertices();
		for(int i = 0; i < v.length; i++){
			for(int j = i+1; j < v.length; j++){
				if(distance(v[i], v[j]) < g.epsilon){
					v[i].neighbors.add(v[j]);
					v[j].neighbors.add(v[i]);
				}
			}
		}
		//populate graph with MST edges
		prim(g);
	}
	
	public static void prim(Graph g){
		ArrayList<Vertex> vertex = new ArrayList<Vertex>();
		for(int i = 0; i < g.getVertices().length; i++){
			vertex.add(g.getVertices()[i]);
		}
		vertex.get(0).c = 0;

		while(vertex.size() > 0){
			Vertex u = vertex.remove(0); //remove 0
			//call pushdown on index 0 unless arraylist is now empty
			if(!vertex.isEmpty()){
				pushdown(vertex, 0);
			}
			for(int i = 0; i<u.neighbors.size(); i++){
				if(vertex.contains(u.neighbors.get(i)) && distance(u, u.neighbors.get(i)) < u.neighbors.get(i).c){
					Vertex neighbor = u.neighbors.get(i);
					neighbor.c = distance(u, neighbor);
					neighbor.parent = u;
					int ind = vertex.indexOf(neighbor);
					pushdown(vertex,ind);
					pullup(vertex,ind);
				}
			}
		}
		//store vertices in an array
		Vertex[] v = new Vertex[g.getVertices().length];
		v = g.getVertices();
		//all parent-child pairs are added as edges
		for(int w = v.length-1; w >= 0; w--){
			if(v[w].parent != null){
				g.addEdge(v[w], v[w].parent);
			}
		}
	}
	
	public static double distance(Vertex a, Vertex b){
		return Math.sqrt(Math.pow(a.x-b.x, 2) + Math.pow(a.y-b.y, 2));
	}
	
	public static ArrayList<Vertex> pullup(ArrayList<Vertex> a, int i){
		int parenti = (i-1)/2;
		if(a.get(i).c < a.get(parenti).c){ 
			Vertex temp = a.get(parenti);
			a.set(parenti, a.get(i));
			a.set(i, temp); 
			if(parenti != 0)
				pullup(a, parenti);
		}
		return a;
	}
	
	public static ArrayList<Vertex> pushdown(ArrayList<Vertex> a, int i){
		int lchild = 2*i + 1;
		int rchild = 2*i + 2;
		int mini = i;
		if(lchild < a.size()){
			if(rchild < a.size()){ //if the rchild is valid
				if(a.get(lchild).c <= a.get(rchild).c && a.get(lchild).c < a.get(mini).c)
					mini = lchild;
				else if(a.get(rchild).c <= a.get(lchild).c && a.get(rchild).c < a.get(mini).c)
					mini = rchild;
			}
			else{ //if rchild is not valid
				if(a.get(lchild).c < a.get(mini).c)
					mini = lchild;
			}
			if(mini != i){
				Vertex temp = a.get(mini);
				a.set(mini, a.get(i));
				a.set(i, temp);
				pushdown(a, mini); 
			}
		}
		return a;
	}
	
	/********************************************
	 * 
	 * You shouldn't modify anything past here
	 * 
	 ********************************************/
	

	// reads in an undirected graph from a specific file formatted with one
	// x/y node coordinate per line:
	private static Graph InputGraph(String file1) {
		
		Graph g = new Graph();
		try (Scanner f = new Scanner(new File(file1))) {
			while(f.hasNextDouble()) // each vertex listing
				g.addVertex(f.nextDouble(), f.nextDouble());
		} catch (IOException e) {
			System.err.println("Cannot open file " + file1 + ". Exiting.");
			System.exit(0);
		}
		
		return g;
	}
	
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		String file1;
		
		System.out.printf("Enter <points file> <edge neighborhood>\n");
		System.out.printf("(e.g: points/small .5)\n");
		file1 = s.next();

		// read in vertices
		Graph g = InputGraph(file1);
		g.epsilon = s.nextDouble();
		
		FindMST(g);

		s.close();

		System.out.printf("Weight of tree: %f\n", g.getTotalEdgeWeight());
	}

}
