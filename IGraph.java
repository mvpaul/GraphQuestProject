package sol;

import src.NodeNameExistsException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Interface for different Graph types. Allows GraphUtils to perform operations on different types of graphs
 */
public interface IGraph {


    /**
     * Method to add a node to the graph with the description
     * @param descr                     description of the node
     * @throws NodeNameExistsException  if node is already in the graph, exception is thrown
     */
    void addNode(String descr) throws NodeNameExistsException;

    /**
     * Method to add a directed edge between nodes. A directed edge points in one direction (from descr1 to descr2)
     * @param descr1 node to draw edge from
     * @param descr2 node to draw edge to
     */
    void addDirectedEdge(String descr1, String descr2);

    /**
     * Method to add an undirected edge between nodes. It's a two-way relationship where a directed edge is added
     * from descr1 to descr2 and also from descr2 to descr 1.
     * @param descr1 first node to connect
     * @param descr2 second node to connect
     */
    void addUndirectedEdge (String descr1, String descr2);

    /**
     * Method to count the amount of nodes that have edges that routes back to itself
     * @return number of nodes with self edges
     */
    int countSelfEdges();

    /**
     * Method that checks whether a node has a route to all other nodes in the graph.
     * @param fromNodeLabel node to check
     * @return boolean (true if it reaches all other nodes in the graph)
     */
    boolean reachesAllOthers(String fromNodeLabel);

    /**
     * Method to get all the immediate neighbors of a node. A neighbor is a node connected to the rootNode via a directed
     * or undirected edge.
     * @param rootNode node to get neighbors of
     * @return HashSet of Strings that represent node neighbors of the root.
     */
    HashSet<String> getNeighbors(String rootNode);

    /**
     * Method to return all the nodes in a graph
     * @return LinkedList<String> that contains all the Nodes
     */
    LinkedList<String> getAllNodes();
}
