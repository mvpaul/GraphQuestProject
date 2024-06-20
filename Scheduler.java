package sol;

import java.util.*;

import src.NoScheduleException;

/**
 * Scheduler class that has methods to create and check validity of schedules for Labs. These methods work to create
 * schedules for two TAs that must teach all the labs in a Graph. If there is no possible schedule, exceptions are
 * thrown.
 */
public class Scheduler {

    /**
     * Constructor for scheduler;
     */
    public Scheduler() {
    }

    /**
     * Method which checks if a given allocation of labs adheres to
     * the scheduling constraints of the graph. Assumes that
     * all lab names in proposedAlloc are valid labels in theGraph.
     *
     * @param theGraph      the graph to try to schedule
     * @param proposedAlloc the proposed allocation of labs between Kathi and Elijah
     * @return boolean indicating whether the proposed allocation is valid
     */
    public static boolean checkValidity(IGraph theGraph, ArrayList<HashSet<String>> proposedAlloc) {
        //Verify that there are only two HashSets in the proposedAlloc
        if (proposedAlloc.size() != 2) {
            return false;
        }

        //Verify that all the labs from the graph have been assigned to a teacher
        HashSet<String> allLabs = new HashSet<>(proposedAlloc.get(0)); //get a single list with all the labs
        allLabs.addAll(proposedAlloc.get(1));

        if (!allLabs.containsAll(theGraph.getAllNodes())) {
            return false;
        }

        //Verify that all the neighbors of a node, which is assigned to one teacher, are assigned to the other teacher.
        //This verifies that teachers are not double booked.
        for (String node : proposedAlloc.get(0)) {
            for (String neighbor : theGraph.getNeighbors(node)) {
                if (neighbor.equals(node) || proposedAlloc.get(0).contains(neighbor)) {
                    return false;
                }
                if (proposedAlloc.get(1).contains(node)) { //Make sure the lab is not also contained in the other list
                    return false;
                }
            }
        }
        for (String node : proposedAlloc.get(1)) {
            for (String neighbor : theGraph.getNeighbors(node)) {
                if (neighbor.equals(node) || proposedAlloc.get(1).contains(neighbor)) {
                    return false;
                }
                if (proposedAlloc.get(0).contains(node)) //make sure the lab is not also contained in the other list
                    return false;
            }
        }


        return true; //return true if all of the above don't apply
    }

    /**
     * Method to compute a valid split of the graph nodes
     * without violating scheduling constraints,
     * if such a split exists
     * Throws a NoScheduleException if no such split exists
     *
     * @param theGraph the graph to try to schedule
     * @return an ArrayList of HashSets of node labels that constitute a
     * valid split of the graph
     * @throws NoScheduleException if no such split exists
     */
    public static ArrayList<HashSet<String>> findSchedule(IGraph theGraph) throws NoScheduleException {
        ArrayList<HashSet<String>> schedule = new ArrayList<>(); //initialize schedule to create

        //to keep track of nodes that have been checked and ones that need to be checked
        Stack<String> queue = new Stack<>();
        HashSet<String> visited = new HashSet<>();


        //empty sets to add labs to
        schedule.add(new HashSet<>()); //for teacher 0
        schedule.add(new HashSet<>()); // for teacher 1

        int currentTeacher = 0;

        //For every node cluster, this assigns an arbitrary start node to a teacher, then assigns the neighbors of
        // the start node to the opposite teacher. It does the same for each neighbor.
        for (String node : theGraph.getAllNodes()) { //to make sure unconnected Nodes are added to the schedule
            if (!visited.contains(node)) {
                schedule.get(currentTeacher).add(node); //assign start node and add it to queue
                queue.push(node);
                while (!queue.isEmpty()) { //repeat until node cluster has assigned teachers
                    String currentNode = queue.pop();
                        visited.add(currentNode);
                        currentTeacher = (currentTeacher + 1) % 2; //get other teacher
                        for (String neighbor : theGraph.getNeighbors(currentNode)) {
                            if (!visited.contains(neighbor)){
                                schedule.get(currentTeacher).add(neighbor); //assign neighbors to other teacher
                                queue.push(neighbor); //add neighbors to queue
                            }
                        }
                }
            }
        }

        if (checkValidity(theGraph, schedule)) { //Only return the schedule if it's a valid schedule
            return schedule;
        } else {
            throw new NoScheduleException(); //exception is thrown if the schedule is not valid
        }
    }

}
