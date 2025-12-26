package com.example.pisovtool;

import java.util.ArrayList;
import java.util.List;

public class MemoSelect {
    private final List<ShapeMemento> selected = new ArrayList<>();

    public void add(ShapeMemento memento) {
        if (!selected.contains(memento)) { // Чтобы не дублировать
            selected.add(memento);
        }
    }

    public void remove(ShapeMemento memento) {
        selected.remove(memento);
    }

    public void clear() {
        selected.clear();
    }

    public List<ShapeMemento> getSelected() {
        return selected;
    }

    public boolean isEmpty() {
        return selected.isEmpty();
    }
}