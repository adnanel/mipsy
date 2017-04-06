package mipsy;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import mipsy.core.MIPSCore;

public class Main extends Application {
    public static Stage PrimaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        PrimaryStage = primaryStage;

        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("MIPSy " + AppInfo.VERSION);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                MIPSCore.Dispose();
            }
        });
    }


    public static void main(String[] args) {
        launch(args);
        
        daskdčaskčdlaskldsakč
    }
}
