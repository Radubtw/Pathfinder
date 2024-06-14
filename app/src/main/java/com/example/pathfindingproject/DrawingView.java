package com.example.pathfindingproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View {
    private int numCellsX = 30; // Number of cells horizontally
    private int numCellsY = 40; // Number of cells vertically
    private int cellWidth;
    private int cellHeight;
    private boolean[][] grid;

    private Paint drawPaint;
    private Paint fixedGreenPaint;
    private Paint fixedRedPaint;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;
    private final int greenX = 0;
    private final int greenY = 15;
    private final int redX = 29;
    private final int redY = 15;

    private float lastTouchX;
    private float lastTouchY;

    private boolean eraserMode = false; // Initial mode is drawing mode

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
    }

    private void setupDrawing() {
        drawPaint = new Paint();
        drawPaint.setColor(Color.BLACK);
        drawPaint.setAntiAlias(true);
        drawPaint.setStyle(Paint.Style.FILL);

        // Set the default background color to white
        fixedGreenPaint = new Paint();
        fixedGreenPaint.setColor(Color.GREEN);
        fixedGreenPaint.setStyle(Paint.Style.FILL);

        fixedRedPaint = new Paint();
        fixedRedPaint.setColor(Color.RED);
        fixedRedPaint.setStyle(Paint.Style.FILL);

        grid = new boolean[numCellsX][numCellsY];
        // Initialize grid and set fixed cells
        for (int i = 0; i < numCellsX; i++) {
            for (int j = 0; j < numCellsY; j++) {
                grid[i][j] = false;
            }
        }
        grid[greenX][greenY] = true; // Green cell
        grid[redX][redY] = true; // Red cell

        // Set the default background color to white
        setBackgroundColor(Color.WHITE);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        cellWidth = w / numCellsX;
        cellHeight = h / numCellsY;
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
        resetCanvas();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Draw the bitmap
        canvas.drawBitmap(canvasBitmap, 0, 0, null);

        if (getContext() instanceof VisualizationActivity) {
            drawFixedPoints(canvas);
        }
    }

    private void drawFixedPoints(Canvas canvas) {
        // Draw the green fixed point
        drawFixedCell(canvas, greenX, greenY, fixedGreenPaint);
        // Draw the red fixed point
        drawFixedCell(canvas, redX, redY, fixedRedPaint);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float touchX = event.getX();
        float touchY = event.getY();

        // Convert touch coordinates to grid coordinates
        int gridX = (int) (touchX / cellWidth);
        int gridY = (int) (touchY / cellHeight);

        // Ensure that the touch is within the bounds of the grid
        if (gridX >= 0 && gridX < numCellsX && gridY >= 0 && gridY < numCellsY) {
            if (!isFixedCell(gridX, gridY)) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastTouchX = touchX;
                        lastTouchY = touchY;
                        if (eraserMode) {
                            eraseCell(gridX, gridY);
                        } else {
                            drawCell(gridX, gridY);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (eraserMode) {
                            eraseCell(gridX, gridY);
                        } else {
                            float prevX = lastTouchX;
                            float prevY = lastTouchY;
                            // Draw line between previous touch point and current touch point
                            while (true) {
                                drawBetween(prevX, prevY, touchX, touchY);
                                float dist = (float) Math.sqrt(Math.pow(touchX - prevX, 2) + Math.pow(touchY - prevY, 2));
                                if (dist < cellWidth / 2) {
                                    break;
                                }
                                float ratio = cellWidth / dist;
                                prevX += (touchX - prevX) * ratio;
                                prevY += (touchY - prevY) * ratio;
                            }
                            lastTouchX = touchX;
                            lastTouchY = touchY;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        // Implement any additional logic if needed
                        break;
                }
            }
        }

        invalidate();
        return true;
    }


    private void drawCell(int gridX, int gridY) {
        float startX = gridX * cellWidth;
        float startY = gridY * cellHeight;
        float endX = startX + cellWidth;
        float endY = startY + cellHeight;
        grid[gridX][gridY] = true;
        drawCanvas.drawRect(startX, startY, endX, endY, drawPaint);
    }


    private void eraseCell(int gridX, int gridY) {
        float startX = gridX * cellWidth;
        float startY = gridY * cellHeight;
        float endX = startX + cellWidth;
        float endY = startY + cellHeight;

        // Create a new Paint object and set its color to white
        Paint erasePaint = new Paint();
        erasePaint.setColor(Color.WHITE);

        grid[gridX][gridY] = false;
        drawCanvas.drawRect(startX, startY, endX, endY, erasePaint);
    }



    private void drawBetween(float startX, float startY, float endX, float endY) {
        // Calculate the distance between the start and end points
        float dx = endX - startX;
        float dy = endY - startY;

        // Calculate the number of steps based on the maximum distance between dx and dy
        int steps = (int) Math.max(Math.abs(dx), Math.abs(dy));

        // Calculate the increment values for each step
        float xIncrement = dx / steps;
        float yIncrement = dy / steps;

        // Draw cells between the start and end points
        for (int i = 0; i < steps; i++) {
            float x = startX + i * xIncrement;
            float y = startY + i * yIncrement;
            int gridX = (int) (x / cellWidth);
            int gridY = (int) (y / cellHeight);
            int cellIndex = gridY * numCellsX + gridX;
            if (eraserMode) {
                eraseCell(gridX, gridY);
            } else {
                drawCell(gridX, gridY);
            }
        }
    }
    private void drawFixedCell(Canvas canvas, int cellX, int cellY, Paint paint) {
        float startX = cellX * cellWidth;
        float startY = cellY * cellHeight;
        float endX = startX + cellWidth;
        float endY = startY + cellHeight;
        canvas.drawRect(startX, startY, endX, endY, paint);
    }

    // Method to check if a cell is fixed
    private boolean isFixedCell(int x, int y) {
        return (x == greenX && y == greenY) || (x == redX && y == redY);
    }

    public void setEraserMode(boolean eraserMode) {
        this.eraserMode = eraserMode;
    }

    public void resetCanvas() {
        // Only reset the canvas and grid if the view is in the visualization activity
        if (getContext() instanceof VisualizationActivity) {
            for (int i = 0; i < numCellsX; i++) {
                for (int j = 0; j < numCellsY; j++) {
                    grid[i][j] = false;
                }
            }
            // Clear the canvas
            drawCanvas.drawColor(Color.WHITE);
            invalidate();
        }
    }

    public void highlightPath(boolean[][] visited) {
        Paint highlightPaint = new Paint();
        highlightPaint.setColor(Color.BLUE);
        highlightPaint.setStyle(Paint.Style.FILL);

        // Iterate over the grid and check if each cell was visited
        for (int i = 0; i < numCellsX; i++) {
            for (int j = 0; j < numCellsY; j++) {
                if (visited[i][j] && !isBlackCell(i, j)) {
                    // If the cell was visited during BFS and is not black, draw it with the highlight color
                    drawFixedCell(drawCanvas, i, j, highlightPaint);
                }
            }
        }
        // Refresh the view to reflect the changes
        invalidate();
    }

    public int highlightPathAStar(boolean[][] visited) {
        Paint highlightPaint = new Paint();
        highlightPaint.setColor(Color.MAGENTA);
        highlightPaint.setStyle(Paint.Style.FILL);
        int path_length = 0;
        // Iterate over the grid and check if each cell was visited
        for (int i = 0; i < numCellsX; i++) {
            for (int j = 0; j < numCellsY; j++) {
                if (visited[i][j] && !isBlackCell(i, j)) {
                    // If the cell was visited during BFS and is not black, draw it with the highlight color
                    drawFixedCell(drawCanvas, i, j, highlightPaint);
                    path_length++;
                }
            }
        }
        // Refresh the view to reflect the changes
        invalidate();
        return path_length;
    }

    public void highlightCell(int gridX, int gridY, int color) {
        float startX = gridX * cellWidth;
        float startY = gridY * cellHeight;
        float endX = startX + cellWidth;
        float endY = startY + cellHeight;

        Paint highlightPaint = new Paint();
        highlightPaint.setColor(color);
        highlightPaint.setStyle(Paint.Style.FILL);

        drawCanvas.drawRect(startX, startY, endX, endY, highlightPaint);
        invalidate();
    }

    // Method to check if a cell is black
    public boolean isBlackCell(int x, int y) {
        return grid[x][y]; // Assuming grid stores whether a cell is painted black
    }
    public int getNumCols() {
        return numCellsY;
    }

    public int getNumRows() {
        return numCellsX;
    }
    public boolean[][] getGrid(){
        return grid;
    }

    public int getGreenX() {
        return greenX;
    }
    public int getGreenY() {
        return greenY;
    }
    public int getRedX() {
        return redX;
    }
    public int getRedY() {
        return redY;
    }
}
