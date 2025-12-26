package com.example.pisovtool.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Line extends Shape {
    private double x2, y2;

    public Line(double x, double y, double x2, double y2, Color color, boolean filled) {
        super(x, y, color, filled);
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(color);
        gc.strokeLine(x, y, x2, y2);
    }

    @Override
    public boolean contains(double px, double py) {
        // Простая проверка близости к отрезку (расстояние <= 10 пикселей)
        double dx = x2 - x;
        double dy = y2 - y;
        double length = Math.sqrt(dx * dx + dy * dy);
        if (length == 0) return false;

        double t = ((px - x) * dx + (py - y) * dy) / (length * length);
        if (t < 0 || t > 1) return false;

        double projX = x + t * dx;
        double projY = y + t * dy;

        double dist = Math.sqrt((px - projX) * (px - projX) + (py - projY) * (py - projY));
        return dist <= 10;
    }

    @Override
    public String getType() {
        return "Отрезок";
    }

    // Ключевые исправления: перемещаем всю линию
    @Override
    public void setX(double newX) {
        double dx = newX - x;
        x = newX;
        x2 += dx;
    }

    @Override
    public void setY(double newY) {
        double dy = newY - y;
        y = newY;
        y2 += dy;
    }

    public double getStartX() { return x; }
    public double getStartY() { return y; }
    public double getEndX() { return x2; }
    public double getEndY() { return y2; }
}