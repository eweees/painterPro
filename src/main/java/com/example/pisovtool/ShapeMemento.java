package com.example.pisovtool;

import com.example.pisovtool.shapes.Shape;

public class ShapeMemento {
    private final Shape shape;
    private final double originalX;
    private final double originalY;

    public ShapeMemento(Shape shape) {
        this.shape = shape;
        this.originalX = shape.getX();
        this.originalY = shape.getY();
    }

    public Shape getShape() {
        return shape;
    }

    public double getOriginalX() {
        return originalX;
    }

    public double getOriginalY() {
        return originalY;
    }
}