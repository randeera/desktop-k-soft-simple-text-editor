package lk.ijse.dep11.app;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AppInitializer extends Application {
    public static SimpleStringProperty observableTitle = new SimpleStringProperty("untitled");

    public static void main(String[] args) {
        launch(args);
    }
    public static Stage stage;
    @Override
    public void start(Stage primaryStage) throws IOException {
        AnchorPane root = FXMLLoader.load(getClass().getResource("/view/TextForm.fxml"));
        Scene scene = new Scene(root);
        stage = primaryStage;
        primaryStage.setScene(scene);
        primaryStage.titleProperty().bind(observableTitle);
        primaryStage.show();
        primaryStage.setResizable(false);
    }
}
