package com.example.pathfindingproject;

import android.graphics.Color;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;
import android.os.Handler;

public class AStarSearch {
    private boolean[][] grid;
    private int numRows;
    private int numCols;
    private Handler handler;

    public AStarSearch(boolean[][] grid) {
        this.grid = grid;
        this.numRows = grid.length;
        this.numCols = grid[0].length;
        this.handler = new Handler();
    }

    public boolean[][] findPath(int startRow, int startCol, int endRow, int endCol) {
        PriorityQueue<Cell> openSet = new PriorityQueue<>(Comparator.comparingInt(Cell::getF));
        Map<Cell, Cell> cameFrom = new HashMap<>();
        Map<Cell, Integer> gScore = new HashMap<>();
        Map<Cell, Integer> fScore = new HashMap<>();

        Cell start = new Cell(startRow, startCol);
        Cell end = new Cell(endRow, endCol);

        openSet.add(start);
        gScore.put(start, 0);
        fScore.put(start, heuristic(start, end));

        while (!openSet.isEmpty()) {
            Cell current = openSet.poll();
            if (current.equals(end)) {
                return reconstructPath(cameFrom, current);
            }

            for (Cell neighbor : getNeighbors(current)) {
                int tentativeGScore = gScore.getOrDefault(current, Integer.MAX_VALUE) + 1;
                if (tentativeGScore < gScore.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeGScore);
                    fScore.put(neighbor, tentativeGScore + heuristic(neighbor, end));
                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                VisualizationActivity.drawingView.highlightCell(neighbor.row, neighbor.col, Color.GRAY);
                            }
                        }, 250);

                    }
                }
            }
        }

        return null;
    }

    private int heuristic(Cell a, Cell b) {
        return Math.abs(a.row - b.row) + Math.abs(a.col - b.col);
    }

    private Set<Cell> getNeighbors(Cell cell) {
        Set<Cell> neighbors = new HashSet<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int newRow = cell.row + dir[0];
            int newCol = cell.col + dir[1];
            if (isValidCell(newRow, newCol) && grid[newRow][newCol] == false) { // Check if it's not a black cell
                neighbors.add(new Cell(newRow, newCol));
            }
        }
        return neighbors;
    }


    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < numRows && col >= 0 && col < numCols;
    }

    private boolean[][] reconstructPath(Map<Cell, Cell> cameFrom, Cell end) {
        boolean[][] path = new boolean[numRows][numCols];
        Cell current = end;
        while (cameFrom.containsKey(current)) {
            Cell prev = cameFrom.get(current);
            path[current.row][current.col] = true; // Marking the path
            current = prev;
        }
        path[current.row][current.col] = true; // Marking the start cell
        return path;
    }


    public static class Cell {
        int row;
        int col;

        Cell(int row, int col) {
            this.row = row;
            this.col = col;
        }

        int getF() {
            return row + col; // Just a placeholder, you may need to adjust this
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Cell cell = (Cell) o;
            return row == cell.row && col == cell.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
    }
}
