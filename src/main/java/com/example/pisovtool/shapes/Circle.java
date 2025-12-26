package com.example.pisovtool.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Circle extends Shape {
    private double radius;

    public Circle(double x, double y, double radius, Color color, boolean filled) {
        super(x, y, color, filled);
        this.radius = radius;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        gc.setStroke(color);
        double diameter = radius * 2;
        if (filled) {
            gc.fillOval(x, y, diameter, diameter);
        } else {
            gc.strokeOval(x, y, diameter, diameter);
        }
    }

    @Override
    public boolean contains(double px, double py) {
        double centerX = x + radius;
        double centerY = y + radius;
        double dx = px - centerX;
        double dy = py - centerY;
        return dx * dx + dy * dy <= radius * radius + 100;
    }

    @Override
    public String getType() {
        return "Круг";
    }

    public double getRadius() {
        return radius;
    }
}