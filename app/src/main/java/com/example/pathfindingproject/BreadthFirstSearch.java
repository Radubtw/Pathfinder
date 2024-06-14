package com.example.pathfindingproject;

import android.graphics.Color;
import android.os.Handler;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

public class BreadthFirstSearch {
    private boolean[][] visited;
    private int[][] parentRow;
    private int[][] parentCol;
    private int numRows;
    private int numCols;
    private Handler handler;

    public BreadthFirstSearch(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.visited = new boolean[numRows][numCols];
        this.parentRow = new int[numRows][numCols];
        this.parentCol = new int[numRows][numCols];
        this.handler = new Handler();
    }

    public void performBFS(int startRow, int startCol, int endRow, int endCol) {
        Queue<Pair> queue = new ArrayDeque<>();
        queue.offer(new Pair(startRow, startCol));
        visited[startRow][startCol] = true;

        while (!queue.isEmpty()) {
            Pair current = queue.poll();
            int row = current.row;
            int col = current.col;

            // Check if the current cell is the destination
            if (row == endRow && col == endCol) {
                // Destination reached, stop the BFS
                break;
            }

            // Visit neighboring cells (up, down, left, right)
            visitNeighbor(row - 1, col, queue, row, col); // up
            visitNeighbor(row + 1, col, queue, row, col); // down
            visitNeighbor(row, col - 1, queue, row, col); // left
            visitNeighbor(row, col + 1, queue, row, col); // right
        }
    }

    private void visitNeighbor(int row, int col, Queue<Pair> queue, int parentRow, int parentCol) {
        if (isValidCell(row, col) && !visited[row][col] && !isBlackCell(row, col)) {
            queue.offer(new Pair(row, col));
            visited[row][col] = true; // Mark the cell as visited
            this.parentRow[row][col] = parentRow; // Set the parent row
            this.parentCol[row][col] = parentCol; // Set the parent column
            highlightCellDelayed(row,col,Color.BLUE);
        }
    }

    private void highlightCellDelayed(final int row, final int col, final int color) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                VisualizationActivity.drawingView.highlightCell(row, col, color);
            }
        }, 250);
    }

    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < numRows && col >= 0 && col < numCols;
    }

    private boolean isBlackCell(int row, int col) {
        return VisualizationActivity.drawingView.isBlackCell(row, col);
    }

    public List<Pair> getShortestPath(int startRow, int startCol, int endRow, int endCol) {
        List<Pair> shortestPath = new ArrayList<>();
        int currentRow = endRow;
        int currentCol = endCol;

        while (!(currentRow == startRow && currentCol == startCol)) {
            shortestPath.add(new Pair(currentRow, currentCol));
            int nextRow = parentRow[currentRow][currentCol];
            int nextCol = parentCol[currentRow][currentCol];
            currentRow = nextRow;
            currentCol = nextCol;
        }

        shortestPath.add(new Pair(startRow, startCol));
        Collections.reverse(shortestPath); // Reverse the list to get the correct order

        return shortestPath;
    }

    public static class Pair {
        int row;
        int col;

        Pair(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    public boolean[][] getVisited() {
        return visited;
    }
}
