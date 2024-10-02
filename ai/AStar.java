package s3.ai;

import java.util.*;
import s3.base.S3;
import s3.entities.S3PhysicalEntity;
import s3.entities.S3Entity;
import s3.util.Pair;

public class AStar {

    private double startX, startY, goalX, goalY;
    private S3PhysicalEntity entity;
    private S3 game;

    private class Node implements Comparable<Node> {
        public double x, y;
        public double gCost, hCost;
        public Node parent;

        public Node(double x, double y, double gCost, double hCost, Node parent) {
            this.x = x;
            this.y = y;
            this.gCost = gCost;
            this.hCost = hCost;
            this.parent = parent;
        }

        public double fCost() {
            return gCost + hCost;
        }

        @Override
        public int compareTo(Node other) {
            return Double.compare(this.fCost(), other.fCost());
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Node node = (Node) obj;
            return Double.compare(node.x, x) == 0 && Double.compare(node.y, y) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    public AStar(double startX, double startY, double goalX, double goalY, S3PhysicalEntity entity, S3 game) {
        this.startX = startX;
        this.startY = startY;
        this.goalX = goalX;
        this.goalY = goalY;
        this.entity = entity;
        this.game = game;
    }

    private double heuristic(double x, double y) {
        return Math.sqrt((x - goalX) * (x - goalX) + (y - goalY) * (y - goalY));
    }

    public List<Pair<Double, Double>> computePath() {
        PriorityQueue<Node> openList = new PriorityQueue<>();
        HashSet<Node> closedList = new HashSet<>();

        Node startNode = new Node(startX, startY, 0, heuristic(startX, startY), null);
        openList.add(startNode);

        while (!openList.isEmpty()) {
            Node currentNode = openList.poll();

            if (currentNode.x == goalX && currentNode.y == goalY) {
                return reconstructPath(currentNode);
            }

            closedList.add(currentNode);

            for (Node neighbor : getNeighbors(currentNode)) {
                if (closedList.contains(neighbor)) {
                    continue;
                }

                double potentialGCost = currentNode.gCost + 1;

                if (!openList.contains(neighbor) || potentialGCost < neighbor.gCost) {
                    neighbor.gCost = potentialGCost;
                    neighbor.hCost = heuristic(neighbor.x, neighbor.y);
                    neighbor.parent = currentNode;

                    if (!openList.contains(neighbor)) {
                        openList.add(neighbor);
                    }
                }
            }
        }
        return null;
        
    }

    private List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();
        double[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        for (double[] dir : directions) {
            double newX = node.x + dir[0];
            double newY = node.y + dir[1];

            if (isValidCell(newX, newY)) {
                neighbors.add(new Node(newX, newY, 0, 0, null));
            }
        }

        return neighbors;
    }

    private boolean isValidCell(double x, double y) {
    	S3PhysicalEntity tempEntity = (S3PhysicalEntity) entity.clone();
        tempEntity.setX((int) x);
        tempEntity.setY((int) y);
        return game.anyLevelCollision(tempEntity) == null;
    }

    private List<Pair<Double, Double>> reconstructPath(Node node) {
        List<Pair<Double, Double>> path = new ArrayList<>();
        while (node != null) {
            path.add(new Pair<>(node.x, node.y));
            node = node.parent;
        }
        Collections.reverse(path);
        return path;
    }

    public static int pathDistance(double start_x, double start_y, double goal_x, double goal_y, S3PhysicalEntity i_entity, S3 the_game) {
        AStar a = new AStar(start_x, start_y, goal_x, goal_y, i_entity, the_game);
        List<Pair<Double, Double>> path = a.computePath();
        if (path != null) return path.size();
        return -1;
    }
}
