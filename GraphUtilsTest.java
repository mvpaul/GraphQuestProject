package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import sol.EdgeArrayGraph;
import sol.GraphUtils;
import sol.IGraph;
import sol.NodeEdgeGraph;
import src.NoRouteException;
import src.NodeNameExistsException;

import java.util.LinkedList;

import static org.junit.Assert.*;

public class GraphUtilsTest {
    private IGraph complexGraph;
    private IGraph edgeGraph;
    @Before
    public void setUpGraphNode(){
        this.complexGraph = new NodeEdgeGraph("complex Graph");
        try {
            this.complexGraph.addNode("node 1");
            this.complexGraph.addNode("node 2");
            this.complexGraph.addNode("node 3");
            this.complexGraph.addNode("node 4");
            this.complexGraph.addNode("node 5");
            this.complexGraph.addNode("node 6");
            this.complexGraph.addNode("node 7");

            this.complexGraph.addUndirectedEdge("node 5", "node 7");
            this.complexGraph.addDirectedEdge("node 5", "node 4");
            this.complexGraph.addDirectedEdge("node 6", "node 7");
            this.complexGraph.addDirectedEdge("node 3","node 4");
            this.complexGraph.addDirectedEdge("node 1", "node 2");
            this.complexGraph.addDirectedEdge("node 1", "node 3");

        }catch (NodeNameExistsException e){
            fail("Unexpected failure: NodeNameExistsException for addNode");
        }

    }
    // Assumes that graph will be empty, modifies it in-place
    private void addSimpleGraphNodes(IGraph graph) throws NodeNameExistsException {
        graph.addNode("node 1");
        graph.addNode("node 2");
        graph.addNode("node 3");
        graph.addNode("node 4");
    }

    // Assumes that graph will have nodes from `addSimpleGraphNodes`,
    //     modifies it in-place
    private void addSimpleGraphEdges(IGraph graph) {
        graph.addDirectedEdge("node 1", "node 2");
        graph.addDirectedEdge("node 2", "node 3");
    }

    // Assumes that graph will be empty, modifies it in-place
    private void makeSimpleGraph(IGraph graph) throws NodeNameExistsException {
        addSimpleGraphNodes(graph);
        addSimpleGraphEdges(graph);
    }
    @Test
    public void testEdgeGraphSetUp(){
        this.edgeGraph = new EdgeArrayGraph("simple edge graph");
        try{
            this.edgeGraph.addNode("node 1");
            this.edgeGraph.addNode("node 2");
            this.edgeGraph.addNode("node 3");

            this.edgeGraph.addDirectedEdge("node 1", "node 2");
            this.edgeGraph.addDirectedEdge("node 2", "node 3");
            Assert.assertEquals("[node 2]", this.edgeGraph.getNeighbors("node 1").toString());
        } catch (NodeNameExistsException e) {
            fail("unexpected failure when adding nodes to edgeGraph");
        }
    }

    @Test
    public void testGetRouteSimple(){
        try {
            NodeEdgeGraph simpleGraph = new NodeEdgeGraph("a graph");
            makeSimpleGraph(simpleGraph);

            String fromNode = "node 1";
            String toNode = "node 3";
            LinkedList<String> route = GraphUtils.getRoute(simpleGraph, fromNode, toNode);

            LinkedList<String> expectedRoute = new LinkedList<>();
            expectedRoute.add("node 1");
            expectedRoute.add("node 2");
            expectedRoute.add("node 3");

            Assert.assertEquals(expectedRoute, route);
        }
        catch (NodeNameExistsException e) {
            // fail() automatically stops and fails the current test with an error message
            fail("Could not create graph to test");
        } catch (NoRouteException e) {
            fail("getRoute did not find a route");
        }
    }

    @Test
    public void testGetRouteSimpleNoRoute(){
        try {
            NodeEdgeGraph simpleGraph = new NodeEdgeGraph("a graph");
            makeSimpleGraph(simpleGraph);
            String fromNode = "node 1";
            String toNode = "node 4";
            Assert.assertThrows(
                    NoRouteException.class,
                    () -> GraphUtils.getRoute(simpleGraph, fromNode, toNode));
        }
        catch (NodeNameExistsException e) {
            fail("Could not create graph to test");
        }
    }

    @Test
    public void testGetRouteComplex(){
        try {
            assertEquals("[node 1, node 3, node 4]",
                    GraphUtils.getRoute(this.complexGraph, "node 1", "node 4").toString());
            assertEquals("[node 1, node 2]",
                    GraphUtils.getRoute(this.complexGraph, "node 1", "node 2").toString());
            assertEquals("[node 6, node 7, node 5, node 4]",
                    GraphUtils.getRoute(this.complexGraph, "node 6", "node 4").toString());
        }catch (NoRouteException e){
            fail("unexpected fail getRoute");
        }

        //test disconnected nodes
        assertThrows(NoRouteException.class,
                ()->GraphUtils.getRoute(this.complexGraph, "node 2", "node 5"));
        assertThrows(NoRouteException.class,
                ()->GraphUtils.getRoute(this.complexGraph, "node 1", "node 5"));
        assertThrows(NoRouteException.class,
                ()->GraphUtils.getRoute(this.complexGraph, "node 4", "node 5"));
    }


    @Test
    public void testCountSelfEdgesSimple() {
        try {
            IGraph basicGraph = new NodeEdgeGraph("a graph");
            basicGraph.addNode("node 1");
            basicGraph.addDirectedEdge("node 1", "node 1");
            Assert.assertEquals(1, basicGraph.countSelfEdges());
        } catch (NodeNameExistsException e) {
            fail("Could not create graph to test");
        }
    }


    @Test
    public void testReachesAllOthersSimple(){
        IGraph basicGraph1 = new NodeEdgeGraph("b graph");
        basicGraph1.addUndirectedEdge("node 1", "node 2");
        Assert.assertTrue(basicGraph1.reachesAllOthers("node 1"));
        Assert.assertTrue(basicGraph1.reachesAllOthers("node 2"));
    }
//    @Test
//    public void testReachesAllOthersComplex(){
//        IGraph basicGraph1 = new NodeEdgeGraph("b graph");
//        basicGraph1.addUndirectedEdge("node 1", "node 2");
//        basicGraph1.addUndirectedEdge("node 2", "node 3");
//        basicGraph1.addUndirectedEdge("node 3", "node 4");
//        basicGraph1.addUndirectedEdge("node 4" , "node 5");
//        basicGraph1.addUndirectedEdge("node 5", "node 1");
//        Assert.assertTrue(basicGraph1.reachesAllOthers("node 1"));
//        Assert.assertTrue(basicGraph1.reachesAllOthers("node 2"));
//    }
//    @Test
//    public void testReachesAllOthersNodeAddedLater(){
//        IGraph basicGraph1 = new NodeEdgeGraph("b graph");
//        basicGraph1.addUndirectedEdge("node 1", "node 2");
//        basicGraph1.addDirectedEdge("node 2", "node 3");
//        Assert.assertTrue(basicGraph1.reachesAllOthers("node 1"));
//        Assert.assertFalse(basicGraph1.reachesAllOthers("node 3"));
//        basicGraph1.addUndirectedEdge("node 3", "node 1");
//        Assert.assertTrue(basicGraph1.reachesAllOthers("node 3"));
//    }
    @Test
    public void testReachesAllOthersNoEdge(){
        IGraph basicGraph1 = new NodeEdgeGraph("b graph");
        basicGraph1.addUndirectedEdge("node 1", "node 2");
        basicGraph1.addDirectedEdge("node 3", "node 4");
        Assert.assertFalse(basicGraph1.reachesAllOthers("node 3"));
    }
}
