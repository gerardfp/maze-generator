import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class MazeGenerator {
    public static void main(String[] args) {
        System.out.println(MazeGenerator.generateMaze(30));
    }

    public static class Node {
        public final int x, y;

        Node(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    
    public final Stack<Node> stack = new Stack<>();
    public final Random rand = new Random();
    public final int[][] maze;
    public final int dimension;

    public MazeGenerator(int dim) {
        maze = new int[dim][dim];
        dimension = dim;
    }

    public static String generateMaze(int dim) {
        MazeGenerator mazeGenerator = new MazeGenerator(dim);
        mazeGenerator.generateMaze();
        return mazeGenerator.getSymbolicMaze();
    }

    public void generateMaze() {
        stack.push(new Node(0,0));
        while (!stack.empty()) {
            Node next = stack.pop();
            if (validNextNode(next)) {
                maze[next.y][next.x] = 1;
                ArrayList<Node> neighbors = findNeighbors(next);
                randomlyAddNodesToStack(neighbors);
            }
        }
    }

    public String getSymbolicMaze() {
        StringBuilder sb = new StringBuilder();
        sb.append("█".repeat(dimension+2)).append("\n");
        for (int i = 0; i < dimension; i++) {
            sb.append("█");
            for (int j = 0; j < dimension; j++) {
                sb.append(maze[i][j] == 1 ? " " : "█");
            }
            sb.append("█\n");
        }
        sb.append("█".repeat(dimension+2)).append("\n");
        return sb.toString();
    }

    public boolean validNextNode(Node node) {
        int numNeighboringOnes = 0;
        for (int y = node.y-1; y < node.y+2; y++) {
            for (int x = node.x-1; x < node.x+2; x++) {
                if (pointOnGrid(x, y) && pointNotNode(node, x, y) && maze[y][x] == 1) {
                    numNeighboringOnes++;
                }
            }
        }
        return (numNeighboringOnes < 3) && maze[node.y][node.x] != 1;
    }

    public void randomlyAddNodesToStack(ArrayList<Node> nodes) {
        int targetIndex;
        while (!nodes.isEmpty()) {
            targetIndex = rand.nextInt(nodes.size());
            stack.push(nodes.remove(targetIndex));
        }
    }

    public ArrayList<Node> findNeighbors(Node node) {
        ArrayList<Node> neighbors = new ArrayList<>();
        for (int y = node.y-1; y < node.y+2; y++) {
            for (int x = node.x-1; x < node.x+2; x++) {
                if (pointOnGrid(x, y) && pointNotCorner(node, x, y)
                    && pointNotNode(node, x, y)) {
                    neighbors.add(new Node(x, y));
                }
            }
        }
        return neighbors;
    }

    public Boolean pointOnGrid(int x, int y) {
        return x >= 0 && y >= 0 && x < dimension && y < dimension;
    }

    public Boolean pointNotCorner(Node node, int x, int y) {
        return (x == node.x || y == node.y);
    }
    
    public Boolean pointNotNode(Node node, int x, int y) {
        return !(x == node.x && y == node.y);
    }
}
