package com.mojiayi.action.algorithm.shortestpath.dijkstra;

import com.mojiayi.action.algorithm.shortestpath.IFindShortestPath;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * @author mojiayi
 */
public class Dijkstra implements IFindShortestPath {
    @Override
    public Graph findShortestPath(Graph graph, Node sourceNode) {
        sourceNode.setDistance(0);

        Set<Node> settledNodes = new HashSet<>();
        Set<Node> unsettledNodes = new HashSet<>();

        unsettledNodes.add(sourceNode);

        while (unsettledNodes.size() != 0) {
            Node currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for (Map.Entry<Node, Integer> adjacentPair : currentNode.getAdjacentNodes().entrySet()) {
                Node adjacentNode = adjacentPair.getKey();
                int edgeWeight = adjacentPair.getValue();
                if (!settledNodes.contains(adjacentNode)) {
                    calculateMinimumDistance(currentNode, adjacentNode, edgeWeight);
                    unsettledNodes.add(adjacentNode);
                }
            }
            settledNodes.add(currentNode);
        }
        return graph;
    }

    private Node getLowestDistanceNode(Set<Node> unsettledNodes) {
        Node lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (Node node : unsettledNodes) {
            int nodeDistance = node.getDistance();
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }

    private void calculateMinimumDistance(Node sourceNode, Node evaluationNode, int edgeWeight) {
        int sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeight < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeight);
            LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }
}
