package sol;

import src.NodeNameExistsException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * A 2D array that represents a graph via an adjacency matrix in which boolean values are true if an edge exists between
 * the vertices represented by the row and column of that cell. HashMap of nodes is used to keep track of the index of
 * each node.
 */
public class EdgeArrayGraph implements IGraph {
    String name;
    private ArrayList<ArrayList<Boolean>> adjacencyMatrix;
    private HashMap<String, Integer> nodeIndexMap;

    /**
     * Constructor for EdgeArrayGraph. Initializes the graph's name, the adjacencyMatrix and the nodeToIndex Map.
     * nodeIndexMap relates Nodes to their index in the adjacencyMatrix. The adjacencyMatrix hold a 2D array of booleans.
     * These are set to true if there is an undirected or directed edge between the two nodes at that index.
     * @param name
     */
    public EdgeArrayGraph(String name) {
        this.name = name;
        this.nodeIndexMap = new HashMap<>();
        this.adjacencyMatrix = new ArrayList<>();
    }

    /**
     * Method to return all the Nodes in the Graph
     * @return LinkedList<String></String> that represents all the nodes contained in the graph.
     */
    @Override
    public LinkedList<String> getAllNodes() {
       return new LinkedList<>(this.nodeIndexMap.keySet());
    }

    /**
     * Method to add a new node with the given description. An exception will
     * be thrown if the description already names a node in the graph
     *
     * @param descr the text description or label to associate with the node
     * @throws NodeNameExistsException if that description is already
     * associated with a node in the graph
     */
    public void addNode(String descr) throws NodeNameExistsException {
        if (this.nodeIndexMap.containsKey(descr)){
            throw new NodeNameExistsException();
        }
        int newNodeIndex = this.adjacencyMatrix.size();
        this.nodeIndexMap.put(descr, newNodeIndex);
        this.adjacencyMatrix.add(new ArrayList<>(this.adjacencyMatrix.size()+1));
        for (ArrayList<Boolean> booleans : this.adjacencyMatrix) {
            this.adjacencyMatrix.get(newNodeIndex).add(false);
            booleans.add(false);
        }
    }

    /**
     * Method to add a directed edge between the nodes associated with the given
     * descriptions. If descr1 and descr2 are not already
     * valid node labels in the graph, those nodes are also created.
     * If the edge already exists, no changes are made
     * (and no exceptions or warnings are raised)
     *
     * @param descr1 the source node for the edge
     * @param descr2 the target node for the edge
     */
    public void addDirectedEdge(String descr1, String descr2) {
        try {
            this.addNode(descr1);
        }catch (NodeNameExistsException ignored) {}
        try{
            this.addNode(descr2);
        } catch (NodeNameExistsException ignored){}

        Integer index2 = this.nodeIndexMap.get(descr2);
        Integer index1 = this.nodeIndexMap.get(descr1);
        this.adjacencyMatrix.get(index1).set(index2,true);
    }

    /**
     * Method to add an undirected edge between the nodes associated with the given
     * descriptions. This is equivalent to adding two directed edges, one from
     * descr1 to descr2, and another from descr2 to descr1.
     * If descr1 and descr2 are not already valid node labels in the graph,
     * those nodes are also created.
     *
     * @param descr1 the source node for the edge
     * @param descr2 the target node for the edge
     */
    public void addUndirectedEdge(String descr1, String descr2) {
        this.addDirectedEdge(descr1, descr2);
        this.addDirectedEdge(descr2,descr1);
    }

    /**
     * Method to count how many nodes have edges to themselves
     *
     * @return the number of nodes that have edges to themselves
     */
    public int countSelfEdges() {
        //This method has O(N) runtime where N is the number of rows in the adjacencyMatrix
        int count = 0;
        for (int i = 0; i < this.adjacencyMatrix.size(); i++) {
            if (this.adjacencyMatrix.get(i).get(i)) {
                count++;
            }
        }
        return count;
    }


    /**
     * Method to check whether a given node has edges to every other node (with or without an edge to itself).
     * Assumes that fromNodeLabel is a valid node label in the graph.
     *
     * @param fromNodeLabel the node to check
     * @return true if fromNodeLabel has an edge to every other node, otherwise false
     */
    public boolean reachesAllOthers(String fromNodeLabel) {
        //This method has O(N) runtime where N is the number of Nodes in the Graph

        Integer nodeIndex = this.nodeIndexMap.get(fromNodeLabel);
        int numNodes = this.adjacencyMatrix.size();
        for (int i = 0; i < numNodes; i++) {
            if (i != nodeIndex && !this.adjacencyMatrix.get(nodeIndex).get(i)) {
                return false; // Current node does not have an edge to at least one other node
            }
        }

        return true; // Current node has edges to every other node
    }


    /**
     * Method to get all the immediate neighbors of a node. A neighbor is a node connected to the rootNode via a directed
     * or undirected edge.
     * @param rootNode node to get neighbors of
     * @return HashSet of Strings that represent node neighbors of the root.
     */
    @Override
    public HashSet<String> getNeighbors(String rootNode) {
        HashSet<String> neighbors = new HashSet<>();
        Integer nodeIndex = this.nodeIndexMap.get(rootNode);

        // If nodeLabel does not exist
        if (nodeIndex == null) {
            return neighbors; // Return an empty set
        }
        // Get the neighbors of the node
        for (int i = 0; i < this.adjacencyMatrix.size(); i++) {
            if (this.adjacencyMatrix.get(nodeIndex).get(i)) {
                neighbors.add(this.getIndexLabel(i));
            }
        }
        return neighbors;
    }


    /**
     * Method to get the Node associated with an index by looping through the nodeIndexMap
     * @param index index of desired node
     * @return Node at given index
     */
    private String getIndexLabel(int index) {
        for (HashMap.Entry<String, Integer> string : this.nodeIndexMap.entrySet()) {
            if (string.getValue() == index) {
                return string.getKey();
            }
        }
        return null;
    }

}
