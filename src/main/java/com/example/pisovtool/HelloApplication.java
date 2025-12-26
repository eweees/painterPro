package com.example.pisovtool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("Рисовалка Pro");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("Нажмите Esc для выхода из полноэкранного режима");

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}