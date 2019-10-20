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

	public Scene makeLoginScene() {
		try {
			return new Scene(FXMLLoader.load(getClass().getResource("/view/login.fxml")));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Scene makeMainScene() {
		try {
			return new Scene(FXMLLoader.load(getClass().getResource("/view/main.fxml")));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	public Scene makeMenuAddScene() {
		try {
			return new Scene(FXMLLoader.load(getClass().getResource("/view/menuadd.fxml")));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Scene makeMenuCheckScene() {
		try {
			return new Scene(FXMLLoader.load(getClass().getResource("/view/menucheck.fxml")));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Scene makeMenuEditScene() {
		try {
			return new Scene(FXMLLoader.load(getClass().getResource("/view/menuedit.fxml")));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Scene makeSaleAddScene() {
		try {
			return new Scene(FXMLLoader.load(getClass().getResource("/view/saleadd.fxml")));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Scene makeSignUpAddScene() {
		try {
			return new Scene(FXMLLoader.load(getClass().getResource("/view/signup.fxml")));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
