package com.example.pisovtool.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Shape
{
    protected double x;
    protected double y;
    protected Color color;
    protected boolean filled;

    public Shape(double x, double y, Color color, boolean filled) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.filled = filled;
    }

    public Color getColor() {
        return color;
    }

    public boolean isFilled() {
        return filled;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }


    public abstract void draw(GraphicsContext gc);

    public abstract boolean contains(double px, double py);

    public abstract String getType();
}