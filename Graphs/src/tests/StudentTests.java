package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import implementation.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class StudentTests {
    @Test
    public void testDijkstras() {
    	Graph<Integer> graph = new Graph<Integer>();

		/* Adding vertices to the graph */
    	graph.addVertex("ST", 1);	
		graph.addVertex("A", 2);
		graph.addVertex("B", 3);
		graph.addVertex("C", 4);
		graph.addVertex("D", 5);
		graph.addVertex("M", 6);

		/* Adding directed edges */
		graph.addDirectedEdge("ST", "A", 11);
		graph.addDirectedEdge("ST", "B", 6);
		graph.addDirectedEdge("A", "C", 2);
		graph.addDirectedEdge("B", "A", 4);
		graph.addDirectedEdge("B", "D", 3);
		graph.addDirectedEdge("C", "D", 5);
		graph.addDirectedEdge("D", "C", 7);
		
		ArrayList<String> shortestPath = new ArrayList<String>();
		System.out.println(graph.doDijkstras("C", "D", shortestPath));
		for (String str : shortestPath)
			System.out.println(str);
    }

}