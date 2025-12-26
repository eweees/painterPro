package com.example.pisovtool;

import com.example.pisovtool.shapes.*;
import javafx.scene.paint.Color;

public class ShapeFactory {
    public static Shape createShape(String name, double startX, double startY, double endX, double endY, Color color, boolean filled) {
        return switch (name) {
            case "Отрезок" -> new Line(startX, startY, endX, endY, color, filled);
            case "Круг" -> {
                double radius = Math.hypot(endX - startX, endY - startY) / 2.0;
                double cx = Math.min(startX, endX);
                double cy = Math.min(startY, endY);
                yield new Circle(cx, cy, radius, color, filled);
            }
            case "Прямоугольник" -> {
                double width = Math.abs(endX - startX);
                double height = Math.abs(endY - startY);
                double rx = Math.min(startX, endX);
                double ry = Math.min(startY, endY);
                yield new Rectangle(rx, ry, width, height, color, filled);
            }
            case "Треугольник" -> {
                double baseX = Math.min(startX, endX);
                double baseY = Math.max(startY, endY);
                double w = Math.abs(endX - startX);
                yield new Triangle(startX, startY, baseX, baseY, baseX + w, baseY, color, filled);
            }
            default -> null;
        };
    }
}