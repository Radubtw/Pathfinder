package com.example.pathfindingproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class VisualizationActivity extends AppCompatActivity {

    public static DrawingView drawingView;
    private ToggleButton toggleButton;
    private Button resetButton;
    private Button startBFSButton; // Add this button
    private Button startAStarButton;
    private BreadthFirstSearch bfs;
    private TextView shortestPathLengthTextView; // Initialize TextView


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualization);

        drawingView = findViewById(R.id.drawingView);
        toggleButton = findViewById(R.id.toggleButton);
        resetButton = findViewById(R.id.resetButton);
        startBFSButton = findViewById(R.id.startBFSButton); // Initialize start BFS button
        startAStarButton = findViewById(R.id.startAStarButton);
        shortestPathLengthTextView = findViewById(R.id.shortestPathLengthTextView);
        // Initialize BFS object with number of rows and columns
        // Set click listener for the toggle button
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle eraser mode when the button is clicked
                drawingView.setEraserMode(toggleButton.isChecked());
            }
        });

        // Set click listener for the reset button
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset the canvas when the button is clicked
                drawingView.resetCanvas();
            }
        });

        // Set click listener for the start BFS button
        startBFSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bfs = new BreadthFirstSearch(drawingView.getNumRows(), drawingView.getNumCols());
                startBFS();
            }
        });
        startAStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAStar();
            }
        });
    }

    public void goBackToMainMenu(View view) {
        // Navigate back to the MainActivity without finishing the current activity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // Method to start the BFS algorithm
    private void startBFS() {
        // Perform BFS to find the shortest path
        bfs.performBFS(drawingView.getGreenX(), drawingView.getGreenY(), drawingView.getRedX(), drawingView.getRedY());
        //drawingView.highlightPath(bfs.getVisited());
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<BreadthFirstSearch.Pair> shortestPath = bfs.getShortestPath(drawingView.getGreenX(), drawingView.getGreenY(), drawingView.getRedX(), drawingView.getRedY());
                highlightShortestPath(shortestPath);
                displayShortestPathLength(shortestPath.size());

            }
        }, 250);
        /*List<BreadthFirstSearch.Pair> shortestPath = bfs.getShortestPath(drawingView.getGreenX(), drawingView.getGreenY(), drawingView.getRedX(), drawingView.getRedY());
        highlightShortestPath(shortestPath);*/
    }

    private void startAStar() {
        // Define start and end points (you can obtain these from your UI)
        int startX = 0;
        int startY = 15;
        int endX = 29;
        int endY = 15;

        // Create the grid (2D array representing your map)
        boolean[][] grid = createGrid(); // Implement this method to create your grid

        // Create an instance of the AStarSearch class
        AStarSearch aStarSearch = new AStarSearch(grid);

        // Find the path using A* algorithm
        boolean[][] path = aStarSearch.findPath(startX, startY, endX, endY);
        // Update DrawingView to highlight the path
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int shortest_path = drawingView.highlightPathAStar(path);
                displayShortestPathLength(shortest_path);
            }
        }, 250);

    }

    private void displayShortestPathLength(int shortestPathLength) {
        TextView shortestPathLengthTextView = findViewById(R.id.shortestPathLengthTextView);
        shortestPathLengthTextView.setText("Shortest Path Length: " + shortestPathLength);
    }


    // Method to create the grid (2D array representing your map)
    private boolean[][] createGrid() {
        return drawingView.getGrid();
    }
    private void highlightShortestPath(List<BreadthFirstSearch.Pair> shortestPath) {
        for (BreadthFirstSearch.Pair cell : shortestPath) {
            drawingView.highlightCell(cell.row, cell.col, Color.YELLOW); // Highlight with yellow color
        }
    }

}
