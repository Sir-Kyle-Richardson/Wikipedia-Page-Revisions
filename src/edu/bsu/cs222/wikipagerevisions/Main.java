package edu.bsu.cs222.wikipagerevisions;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
        primaryStage.setTitle("Wikipedia Page Revisions");
        primaryStage.setScene(new Scene(root, 825, 825));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
