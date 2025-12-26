package com.example.pisovtool.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Rectangle extends Shape {
    private double width;
    private double height;
    private boolean filled;

    public Rectangle(double x, double y, double width, double height, Color color, boolean filled) {
        super(x, y, color, filled);
        this.width = width;
        this.height = height;
        this.filled = filled;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        gc.setStroke(color);

        if (filled) {
            gc.fillRect(x, y, width, height);
        } else {
            gc.strokeRect(x, y, width, height);
        }
    }

    @Override
    public boolean contains(double px, double py) {
        return px >= x && px <= x + width &&
                py >= y && py <= y + height;
    }

    @Override
    public String getType() {
        return "Прямоугольник";
    }

    public double getWidth()
    {
        return width;
    }
    public double getHeight()
    {
        return height;
    }
}