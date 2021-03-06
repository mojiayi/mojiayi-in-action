package com.mojiayi.action.algorithm.shortestpath;

import com.mojiayi.action.algorithm.shortestpath.dijkstra.Graph;
import com.mojiayi.action.algorithm.shortestpath.dijkstra.Node;

/**
 * @author mojiayi
 */
public interface IFindShortestPath {
    Graph findShortestPath(Graph graph, Node sourceNode);
}
