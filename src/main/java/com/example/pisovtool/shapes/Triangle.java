package com.example.pisovtool.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Triangle extends Shape {
    private double x2, y2, x3, y3;

    public Triangle(double x, double y, double x2, double y2, double x3, double y3, Color color, boolean filled) {
        super(x, y, color, filled);
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        gc.setStroke(color);

        double[] xPoints = {x, x2, x3};
        double[] yPoints = {y, y2, y3};

        if (filled) {
            gc.fillPolygon(xPoints, yPoints, 3);
        } else {
            gc.strokePolygon(xPoints, yPoints, 3);
        }
    }

    @Override
    public boolean contains(double px, double py) {
        // Улучшенная проверка попадания в треугольник с "буфером" 10 пикселей
        double area = 0.5 * Math.abs((x2 - x) * (y3 - y) - (x3 - x) * (y2 - y));

        double area1 = 0.5 * Math.abs((px - x) * (y2 - py) - (x2 - px) * (py - y));
        double area2 = 0.5 * Math.abs((px - x2) * (y3 - py) - (x3 - px) * (py - y2));
        double area3 = 0.5 * Math.abs((px - x3) * (y - py) - (x - px) * (py - y3));

        // Добавляем буфер для удобства клика (10 пикселей)
        return Math.abs(area1 + area2 + area3 - area) < 10;
    }

    @Override
    public String getType() {
        return "Треугольник";
    }

    // Перемещение всей фигуры целиком
    @Override
    public void setX(double newX) {
        double dx = newX - x;
        x += dx;
        x2 += dx;
        x3 += dx;
    }

    @Override
    public void setY(double newY) {
        double dy = newY - y;
        y += dy;
        y2 += dy;
        y3 += dy;
    }

    public double getX2() { return x2; }
    public double getY2() { return y2; }
    public double getX3() { return x3; }
    public double getY3() { return y3; }
}