package sol;

import src.NoRouteException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Class that performs operations on graphs. Has method to check if two nodes are connected in a graph and a method to
 * return the shortest route between two nodes.
 */
public class GraphUtils {

    /**
     * Constructor for graphUtils.
     */
    public GraphUtils() {
    }

    /**
     * Method to use breadth-first-search to check whether there is a path
     * from one node to another in a graph. Assumes that both fromNodeLabel
     * and toNodeLabel are valid node labels in theGraph.
     *
     * @param theGraph      the graph to traverse
     * @param fromNodeLabel name of the node from which to start searching
     * @param toNodeLabel   name of the node we want to reach
     * @return boolean indicating whether such a route exists
     */
    public static boolean hasRoute(NodeEdgeGraph theGraph, String fromNodeLabel, String toNodeLabel) {
        NodeEdgeGraph.Node fromNode = theGraph.getNode(fromNodeLabel);
        NodeEdgeGraph.Node toNode = theGraph.getNode(toNodeLabel);
        // set up and initialize data structures
        HashSet<NodeEdgeGraph.Node> visited = new HashSet<>();
        LinkedList<NodeEdgeGraph.Node> toCheck = new LinkedList<>();
        toCheck.add(fromNode);

        // process nodes to search for toNode
        while (!toCheck.isEmpty()) {
            NodeEdgeGraph.Node checkNode = toCheck.pop();
            if (checkNode.equals(toNode)) {
                return true;
            } else if (!visited.contains(checkNode)) {
                visited.add(checkNode);
                toCheck.addAll(checkNode.nextNodes);
            }
        }
        return false;
    }

    /**
     * Method to produce a sequence of nodes that constitutes a shortest path
     * from fromNodeLabel to toNodeLabel. Assumes that both fromNodeLabel
     * and toNodeLabel are valid node labels in theGraph.
     * Throws a NoRouteException if no such path exists.
     * It calls trackParents to build a linkedList backwards starting from the toNode tracking parents until there are
     * no parents. "addFirst" is used to ensure the route goes from the fromNode to the toNode and not the other way
     * around.
     *
     * @param theGraph      the graph to traverse
     * @param fromNodeLabel the node from which to start searching
     * @param toNodeLabel   the node we want to reach
     * @return List of nodes in order of the path
     * @throws NoRouteException if no such path exists
     */
    public static LinkedList<String> getRoute(IGraph theGraph, String fromNodeLabel, String toNodeLabel) throws NoRouteException {
        //get map of parents from the fromNode to the toNode
        HashMap<String, String> parentMap = trackParents(theGraph, fromNodeLabel, toNodeLabel);

        LinkedList<String> route = new LinkedList<>();
        route.add(toNodeLabel); //add the final Node to the route

        String parent = parentMap.get(toNodeLabel); //get the parent of the final node
        while (parent != null) {
            route.addFirst(parent); //add the parent to the list before the child
            parent = parentMap.get(parent); //get the parent of the parent and repeat
        }
        return route;
    }

    /**
     * Helper method for getRoute that returns a map of the parents of the nodes from the fromNode to the toNode. It
     * uses BFS to find the shortest path to the toNode while storing the path to the toNode by tracking all the
     * parents. It uses a visited list to ensure that every node is visited once.
     * @param theGraph Graph that contains fromNode and toNode to traverse
     * @param fromNode the starting node to get route from
     * @param toNode the destination node to get route to
     * @return HashMap<Child, Parent> for the nodes along the path from the fromNode to the toNode
     * @throws NoRouteException if there is no route between the nodes.
     */
    private static HashMap<String, String> trackParents(IGraph theGraph, String fromNode, String toNode) throws NoRouteException {
        HashMap<String, String> parentMap = new HashMap<>(); //new map to track the parents of the nodes along the route

        // to keep track of nodes that need to be checked and ones that have already been checked
        Stack<String> queue = new Stack<>();
        HashSet<String> visited = new HashSet<>();
        queue.push(fromNode); //add the start node to the queue
        String currentNode;
        while (!queue.isEmpty()) {
            currentNode = queue.pop();
            visited.add(currentNode);
            if (currentNode.equals(toNode)) { //if the toNode is found, stop looping and return the map
                return parentMap;
            } else {
                for (String neighbor : theGraph.getNeighbors(currentNode)) {
                    if (!visited.contains(neighbor)) {
                        if (!parentMap.containsKey(neighbor)) {
                            parentMap.put(neighbor, currentNode); //for every neighbor of the current node, add
                            //them to the parent Map.
                            queue.push(neighbor);
                        }
                    }
                }
            }
        }

        throw new NoRouteException(); //if a map has not been returned by this point, there is no route between the nodes
    }
}