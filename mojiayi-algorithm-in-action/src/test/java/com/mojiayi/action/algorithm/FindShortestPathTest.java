package com.mojiayi.action.algorithm;

import com.mojiayi.action.algorithm.shortestpath.IFindShortestPath;
import com.mojiayi.action.algorithm.shortestpath.dijkstra.Dijkstra;
import com.mojiayi.action.algorithm.shortestpath.dijkstra.Graph;
import com.mojiayi.action.algorithm.shortestpath.dijkstra.Node;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FindShortestPathTest {
    @Test
    public void findByDijkstra() {
        Graph graph = init();
        Map<String, Node> availableNodes = graph.getNodes().stream().collect(Collectors.toMap(Node::getName, Function.identity()));

        String sourceNodeName = "A";
        Node sourceNode = availableNodes.get(sourceNodeName);

        IFindShortestPath handler = new Dijkstra();
        Map<String, Node> calculatedGraph = handler.findShortestPath(graph, sourceNode)
                .getNodes().stream().collect(Collectors.toMap(Node::getName, Function.identity()));

        String terminalNodeName = "E";
        Node terminalNode = calculatedGraph.get(terminalNodeName);
        Assert.assertNotNull(terminalNode);
        Assert.assertEquals(3L, terminalNode.getShortestPath().size());
        List<Node> shortestPath = terminalNode.getShortestPath();
        Assert.assertEquals("A", shortestPath.get(0).getName());
        Assert.assertEquals("B", shortestPath.get(1).getName());
        Assert.assertEquals("D", shortestPath.get(2).getName());

        terminalNodeName = "F";
        terminalNode = calculatedGraph.get(terminalNodeName);
        Assert.assertNotNull(terminalNode);
        Assert.assertEquals(3L, terminalNode.getShortestPath().size());
        shortestPath = terminalNode.getShortestPath();
        Assert.assertEquals("A", shortestPath.get(0).getName());
        Assert.assertEquals("B", shortestPath.get(1).getName());
        Assert.assertEquals("D", shortestPath.get(2).getName());
    }

    private Graph init() {
        Node nodeA = new Node("A");
        Node nodeB = new Node("B");
        Node nodeC = new Node("C");
        Node nodeD = new Node("D");
        Node nodeE = new Node("E");
        Node nodeF = new Node("F");

        nodeA.addDestination(nodeB, 10);
        nodeA.addDestination(nodeC, 15);

        nodeB.addDestination(nodeD, 12);
        nodeB.addDestination(nodeF, 15);

        nodeC.addDestination(nodeE, 10);

        nodeD.addDestination(nodeE, 2);
        nodeD.addDestination(nodeF, 1);

        nodeF.addDestination(nodeE, 5);

        Graph graph = new Graph();

        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addNode(nodeC);
        graph.addNode(nodeD);
        graph.addNode(nodeE);
        graph.addNode(nodeF);
        return graph;
    }
}
