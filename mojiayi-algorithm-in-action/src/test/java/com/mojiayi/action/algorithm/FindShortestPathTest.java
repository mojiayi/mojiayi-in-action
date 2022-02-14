package com.mojiayi.action.algorithm;

import com.mojiayi.action.algorithm.shortestpath.IFindShortestPath;
import com.mojiayi.action.algorithm.shortestpath.dijkstra.Dijkstra;
import com.mojiayi.action.algorithm.shortestpath.dijkstra.Graph;
import com.mojiayi.action.algorithm.shortestpath.dijkstra.Node;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FindShortestPathTest {
    @Test
    public void findByDijkstra() {
        Graph graph = init();

        Map<String, Node> availableNodes = graph.getNodes().stream().collect(Collectors.toMap(Node::getName, Function.identity()));
        Node sourceNode = availableNodes.get("A");

        IFindShortestPath handler = new Dijkstra();
        Graph shortestPath = handler.findShortestPath(graph, sourceNode);

        Assert.assertNotNull(shortestPath);
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
