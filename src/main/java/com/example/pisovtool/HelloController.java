package com.example.pisovtool;

import com.example.pisovtool.shapes.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HelloController {

    private double startX, startY;
    private boolean drawing = false;
    private final List<Shape> shapes = new ArrayList<>();

    @FXML private Canvas canvass;
    @FXML private ComboBox<String> cmb_figures;
    @FXML private ComboBox<String> type_figures;
    @FXML private ColorPicker clr;
    @FXML private ToggleButton btnCursor;
    @FXML private ToggleButton btnBrush;

    // Множественный выбор
    private final MemoSelect selectedMementos = new MemoSelect();
    private double dragStartX, dragStartY; // Для расчёта смещения группы

    private Tool currentTool = Tool.BRUSH;

    @FXML
    public void initialize() {
        cmb_figures.getItems().addAll("Отрезок", "Круг", "Прямоугольник", "Треугольник");
        type_figures.getItems().addAll("Заполненный", "Пустой");

        ToggleGroup toolsGroup = new ToggleGroup();
        btnCursor.setToggleGroup(toolsGroup);
        btnBrush.setToggleGroup(toolsGroup);
        btnBrush.setSelected(true);

        toolsGroup.selectedToggleProperty().addListener((obs, old, newToggle) ->
                currentTool = (newToggle == btnCursor) ? Tool.CURSOR : Tool.BRUSH);

        // Привязываем размер Canvas к размеру родителя (Pane)
        Platform.runLater(() -> {
            if (canvass.getParent() != null) {
                canvass.getParent().layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
                    canvass.setWidth(newBounds.getWidth());
                    canvass.setHeight(newBounds.getHeight());
                    redrawAll();
                });

                var bounds = canvass.getParent().getLayoutBounds();
                canvass.setWidth(bounds.getWidth());
                canvass.setHeight(bounds.getHeight());

                System.out.println("Canvas размер: " + canvass.getWidth() + " x " + canvass.getHeight());
                redrawAll();
            }
        });

        // === МНОЖЕСТВЕННЫЙ ВЫБОР И ГРУППОВОЕ ПЕРЕМЕЩЕНИЕ ===
        canvass.setOnMousePressed(e -> {
            System.out.println("MousePressed: X=" + e.getX() + " Y=" + e.getY() + " Tool=" + currentTool);

            if (currentTool == Tool.BRUSH) {
                startX = e.getX();
                startY = e.getY();
                drawing = true;
                selectedMementos.clear(); // Сбрасываем выделение при рисовании
            } else if (currentTool == Tool.CURSOR) {
                boolean found = false;

                // Ищем фигуру под курсором (верхняя)
                for (int i = shapes.size() - 1; i >= 0; i--) {
                    Shape s = shapes.get(i);
                    if (s.contains(e.getX(), e.getY())) {
                        ShapeMemento memento = new ShapeMemento(s);
                        selectedMementos.add(memento);
                        System.out.println("Добавлена в выделение: " + s.getType() + " (всего: " + selectedMementos.getSelected().size() + ")");
                        found = true;
                        break; // Добавляем только одну за клик (верхнюю)
                    }
                }

                if (found) {
                    dragStartX = e.getX();
                    dragStartY = e.getY();
                } else {
                    selectedMementos.clear();
                    System.out.println("Выделение сброшено (клик по пустому месту)");
                }
            }
        });

        canvass.setOnMouseDragged(e -> {
            if (currentTool == Tool.BRUSH && drawing) {
                redrawWithPreview(e.getX(), e.getY());
            } else if (currentTool == Tool.CURSOR && !selectedMementos.isEmpty()) {
                double dx = e.getX() - dragStartX;
                double dy = e.getY() - dragStartY;

                for (ShapeMemento m : selectedMementos.getSelected()) {
                    Shape s = m.getShape();
                    s.setX(m.getOriginalX() + dx);
                    s.setY(m.getOriginalY() + dy);
                }
                redrawAll();
            }
        });

        canvass.setOnMouseReleased(e -> {
            if (currentTool == Tool.BRUSH && drawing) {
                drawing = false;
                addFinalShape(e.getX(), e.getY());
            }
            // Положение уже применено в Dragged — ничего не делаем
        });
    }

    private void redrawWithPreview(double endX, double endY) {
        GraphicsContext gc = canvass.getGraphicsContext2D();
        gc.clearRect(0, 0, canvass.getWidth(), canvass.getHeight());

        for (Shape s : shapes) s.draw(gc);

        String selected = cmb_figures.getValue();
        if (selected == null) return;

        Color color = clr.getValue();
        boolean filled = "Заполненный".equals(type_figures.getValue());

        Shape preview = ShapeFactory.createShape(selected, startX, startY, endX, endY, color, filled);
        if (preview != null) preview.draw(gc);
    }

    private void addFinalShape(double endX, double endY) {
        String selected = cmb_figures.getValue();
        if (selected == null) return;

        Color color = clr.getValue();
        boolean filled = "Заполненный".equals(type_figures.getValue());

        Shape finalShape = ShapeFactory.createShape(selected, startX, startY, endX, endY, color, filled);
        if (finalShape != null) {
            shapes.add(finalShape);
            System.out.println("Добавлена фигура: " + selected);
        }
        redrawAll();
    }

    private void redrawAll() {
        GraphicsContext gc = canvass.getGraphicsContext2D();
        gc.clearRect(0, 0, canvass.getWidth(), canvass.getHeight());
        for (Shape s : shapes) s.draw(gc);
    }

    @FXML
    protected void onClearCanvas() {
        shapes.clear();
        selectedMementos.clear();
        redrawAll();
        System.out.println("Холст очищен");
    }

    @FXML
    protected void onUndo() {
        if (!shapes.isEmpty()) {
            shapes.removeLast();
            redrawAll();
            System.out.println("Отменено последнее действие");
        }
    }

    @FXML
    protected void onSaveToJson() {
        List<ShapeData> shapeDataList = new ArrayList<>();

        for (Shape s : shapes) {
            ShapeData data = new ShapeData();
            data.type = s.getType();
            data.color = s.getColor().toString();
            data.filled = s.isFilled();

            if (s instanceof Line line) {
                data.x = line.getStartX();
                data.y = line.getStartY();
                data.x2 = line.getEndX();
                data.y2 = line.getEndY();
            } else if (s instanceof Circle c) {
                data.x = c.getX();
                data.y = c.getY();
                data.radius = c.getRadius();
            } else if (s instanceof Rectangle r) {
                data.x = r.getX();
                data.y = r.getY();
                data.width = r.getWidth();
                data.height = r.getHeight();
            } else if (s instanceof Triangle t) {
                data.x = t.getX();
                data.y = t.getY();
                data.x2 = t.getX2();
                data.y2 = t.getY2();
                data.x3 = t.getX3();
                data.y3 = t.getY3();
            }
            shapeDataList.add(data);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter("shapes.json")) {
            gson.toJson(shapeDataList, writer);
            System.out.println("Сохранено " + shapeDataList.size() + " фигур");
        } catch (IOException e) {
            System.err.println("Ошибка сохранения: " + e.getMessage());
        }
    }

    @FXML
    protected void onLoadFromJson() {
        try (FileReader reader = new FileReader("shapes.json")) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<ShapeData>>(){}.getType();
            List<ShapeData> shapeDataList = gson.fromJson(reader, listType);

            if (shapeDataList == null) return;

            shapes.clear();

            for (ShapeData data : shapeDataList) {
                Color color = Color.valueOf(data.color);
                Shape s = switch (data.type) {
                    case "Отрезок" -> new Line(data.x, data.y, data.x2, data.y2, color, data.filled);
                    case "Круг" -> new Circle(data.x, data.y, data.radius, color, data.filled);
                    case "Прямоугольник" -> new Rectangle(data.x, data.y, data.width, data.height, color, data.filled);
                    case "Треугольник" -> new Triangle(data.x, data.y, data.x2, data.y2, data.x3, data.y3, color, data.filled);
                    default -> null;
                };

                if (s != null) shapes.add(s);
            }

            redrawAll();
            System.out.println("Загружено " + shapes.size() + " фигур");
        } catch (IOException e) {
            System.out.println("Файл shapes.json не найден");
        }
    }
}