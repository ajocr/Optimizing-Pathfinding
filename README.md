# Optimizing Pathfinding

Demo: [youtube link](https://youtu.be/DwQp7BPeTSQ)

In this project, I worked on the AStar file

The AStar code finds the shortest path between a start and goal point in a game world using a
priority queue to efficiently manage checking nodes based on their total cost, i.e. the sum of the
actual cost from the start node and a heuristic estimate to the goal. The Node class represents each
position in the grid, with methods for comparing costs and tracking the path. The algorithm
initializes the start node, then iteratively explores the least costly node, adds the neighbors to the open list if they haven't been closed, updates costs, and sets the parent node for path reconstruction.
The heuristic function finds the distance, estimating the cost to the goal. The ‘isValidCell’ method checks if a node's position is navigable in the game world. Once the goal is reached, the
path is reconstructed from the goal back to the start node. If no path is found, the method returns
null. This ensures an optimal path is found efficiently by prioritizing nodes that appear closer to
the goal based on the actual and heuristic costs.


**repo can be used to test map generator**
