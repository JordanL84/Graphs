package implementation;

import java.util.*;

/**
 * Implements a graph. We use two maps: one map for adjacency properties 
 * (adjancencyMap) and one map (dataMap) to keep track of the data associated 
 * with a vertex. 
 * 
 * @author cmsc132
 * 
 * @param <E>
 */
public class Graph<E> {
	/* You must use the following maps in your implementation */
	private HashMap<String, HashMap<String, Integer>> adjacencyMap;
	private HashMap<String, E> dataMap;

	public Graph() {
		adjacencyMap = new HashMap<>();
		dataMap = new HashMap<>();
	}

	public void addVertex(String vertexName, E data) {
		if (dataMap.containsKey(vertexName))
			throw new IllegalArgumentException("Vertex already exists");
		adjacencyMap.put(vertexName, new HashMap<String, Integer>());
		dataMap.put(vertexName, data);
	}
	
	public void addDirectedEdge(String startVertexName, String endVertexName, int cost) {
		if (!(dataMap.containsKey(startVertexName) && dataMap.containsKey(endVertexName)))
			throw new IllegalArgumentException("Vertex does not exist");
		adjacencyMap.get(startVertexName).put(endVertexName, cost);
	}
	
	public String toString() {
		String result = "Vertices: ";
		TreeMap<String, HashMap<String, Integer>> sortedMap = new TreeMap<String, 
				HashMap<String, Integer>>(adjacencyMap);
		result += sortedMap.keySet();
		result += "\nEdges:\n";
		for (String vertex : sortedMap.keySet()) {
			result += "Vertex(" + vertex + ")--->{";
			for (String endPoint : adjacencyMap.get(vertex).keySet()) {
				result += endPoint+"="+adjacencyMap.get(vertex).get(endPoint)+", ";
			}
			if (sortedMap.get(vertex).size() > 0)
				result = result.substring(0, result.length()-2);
			result += "}\n";
		}
		return result;
	}
	
	public Map<String, Integer> getAdjacentVertices(String vertexName) {
		return adjacencyMap.get(vertexName);
	}
	
	public int getCost(String startVertexName, String endVertexName) {
		if (!(dataMap.containsKey(startVertexName) && dataMap.containsKey(endVertexName)))
			throw new IllegalArgumentException("Vertex does not exist");
		if (!(adjacencyMap.get(startVertexName).containsKey(endVertexName)))
			throw new IllegalArgumentException("Edge does not exist");
		return adjacencyMap.get(startVertexName).get(endVertexName);
	}
	
	public Set<String> getVertices() {
		return dataMap.keySet();
	}
	
	public E getData(String vertex) {
		if (!(dataMap.containsKey(vertex)))
			throw new IllegalArgumentException("Vertex does not exist");
		return dataMap.get(vertex);
	}
	
	public void doBreadthFirstSearch(String startVertexName, CallBack<E> callBack) {
		if (!(dataMap.containsKey(startVertexName)))
			throw new IllegalArgumentException("Vertex does not exist");
		Set<String> visited = new HashSet<>();
		Queue<String> discovered = new LinkedList<>();
		discovered.add(startVertexName);
		while (!(discovered.isEmpty())) {
			String first = discovered.peek();
			discovered.remove(first);
			if (!(visited.contains(first))) {
				visited.add(first);
				callBack.processVertex(first, dataMap.get(first));
				Map<String, Integer> successors = new TreeMap<>(adjacencyMap.get(first));
				for (String successor : successors.keySet()) {
					if (!(visited.contains(successor)))
						discovered.add(successor);
				}
			}
		}
	}
	
	public void doDepthFirstSearch(String startVertexName, CallBack<E> callBack) {
		if (!(dataMap.containsKey(startVertexName)))
			throw new IllegalArgumentException("Vertex does not exist");
		Set<String> visited = new HashSet<>();
		Stack<String> discovered = new Stack<>();
		discovered.push(startVertexName);
		while (!discovered.isEmpty()) {
			String first = discovered.pop();
			if (!(visited.contains(first))) {
				visited.add(first);
				callBack.processVertex(first, dataMap.get(first));
				Map<String, Integer> successors = new TreeMap<>(adjacencyMap.get(first));
				for (String successor : successors.keySet()) {
					if (!(visited.contains(successor)))
						discovered.push(successor);
				}
			}
		}
	}
	
	public int doDijkstras(String startVertexName, String endVertexName,
			   ArrayList<String> shortestPath) {
		if (!(dataMap.containsKey(startVertexName) && dataMap.containsKey(endVertexName)))
			throw new IllegalArgumentException("Vertex does not exist");
		Map<String, String> prev = new HashMap<>();
		Map<String, Integer> cost = new HashMap<>();
		Set<String> remaining = new HashSet<>();
		for (String str : dataMap.keySet()) {
			if (str.equals(startVertexName)) 
				cost.put(str, 0);
			else 
				cost.put(str, 10001); //Integer.MAX_VALUE
			remaining.add(str);
		}
		while (!(remaining.isEmpty())) {
			String k = null;
			int min = 10001;
			for (String str : remaining) {
				if (cost.get(str) <= min) {
					k = str;
					min = cost.get(str);
				}
			}
			remaining.remove(k);
			for (String adj : adjacencyMap.get(k).keySet()) {
				if (remaining.contains(adj)) {
					int edge = cost.get(k) + getCost(k, adj);
					if (edge < cost.get(adj)) {
						cost.put(adj, edge);
						prev.put(adj, k);
					}
				}
			}
		}
		if (cost.get(endVertexName) > 10000) { //Change to Integer.MAX_VALUE
			shortestPath.add("None");
			return -1;
		}
		String curr = endVertexName;
		while (!(curr.equals(startVertexName))) {
			shortestPath.add(0, curr);
			curr = prev.get(curr);
		}
		shortestPath.add(0, startVertexName);
		return cost.get(endVertexName);
	}
}