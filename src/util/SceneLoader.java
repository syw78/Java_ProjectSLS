package util;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class SceneLoader {
	static private SceneLoader shared = new SceneLoader();

	private SceneLoader() {
	}

	static public SceneLoader getInstance() {
		return shared;
	}

	public Scene makeLoginScene() throws IOException {
		return new Scene(FXMLLoader.load(getClass().getResource("/view/login.fxml")));
	}

	public Scene makeMainScene() throws IOException {
		return new Scene(FXMLLoader.load(getClass().getResource("/view/main.fxml")));
	}

	public Scene makeMenuAddScene() throws IOException {
		return new Scene(FXMLLoader.load(getClass().getResource("/view/menuadd.fxml")));
	}

	public Scene makeMenuCheckScene() throws IOException {
		return new Scene(FXMLLoader.load(getClass().getResource("/view/menucheck.fxml")));
	}

	public Scene makeMenuEditScene() throws IOException {
		return new Scene(FXMLLoader.load(getClass().getResource("/view/menuedit.fxml")));
	}

	public Scene makeSaleAddScene() throws IOException {
		return new Scene(FXMLLoader.load(getClass().getResource("/view/saleadd.fxml")));
	}

	public Scene makeSignUpAddScene() throws IOException {
		return new Scene(FXMLLoader.load(getClass().getResource("/view/signup.fxml")));
	}
}
