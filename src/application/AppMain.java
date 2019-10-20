package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.SceneLoader;

public class AppMain extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		Scene scene = SceneLoader.getInstance().makeLoginScene();
		primaryStage.setResizable(false);
		primaryStage.setTitle("LoginWindow");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {

		launch(args);

	}

}
