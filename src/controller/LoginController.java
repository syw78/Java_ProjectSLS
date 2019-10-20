package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.ClientDAO;
import model.ClientVO;
import util.AlertManager;
import util.AlertManager.AlertInfo;
import util.SceneLoader;

public class LoginController implements Initializable {

	@FXML
	private TextField tfId;
	@FXML
	private TextField tfPassword;
	@FXML
	private Button btLogin;
	@FXML
	private Button btSignUp;
	@FXML
	private Button btCancle;

	private ClientDAO clientDVO;
	private ArrayList<ClientVO> clientList = new ArrayList<ClientVO>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		initSetting();

		btLogin.setOnAction(e -> handlerButtonLoginAction(e));
		btCancle.setOnAction(e -> handlerButtonCancleAction(e));
		btSignUp.setOnAction(e -> handlerButtonSignUpAction(e));

	} // end of initialize

	private void handlerButtonLoginAction(ActionEvent e) {

		if (tfId.getText().equals("") || tfPassword.getText().equals("")) {
			
			AlertManager.getInstance().show(AlertInfo.FAIL_LOGIN, null);

		} else if ((tfId.getText().equals("admin") && tfPassword.getText().equals("1234"))
				|| (checkId(tfId.getText(), clientList) && checkPassword(tfPassword.getText(), clientList))) {
			
			Stage mainStage = null;

			try {

				Scene scene = SceneLoader.getInstance().makeMainScene();
				mainStage = new Stage();
				mainStage.setTitle("MAIN");
				mainStage.setScene(scene);
				mainStage.setResizable(false);

				((Stage) btLogin.getScene().getWindow()).close();

				mainStage.show();
				
			} catch (Exception e1) {
				e1.printStackTrace();
				AlertManager.getInstance().show(AlertInfo.ERROR_LOAD_SCENE, null);
			}

		} else {
			AlertManager.getInstance().show(AlertInfo.FAIL_LOGIN, null);
		}

	} // end of handlerButtonLoginAction

	private void handlerButtonCancleAction(ActionEvent e) {

		Platform.exit();

	} // end of handlerButtonCancleAction

	private void handlerButtonSignUpAction(ActionEvent e) {

		try {

			Scene scene = SceneLoader.getInstance().makeSignUpAddScene();
			Parent signUpRoot = scene.getRoot();
			Stage dialogStage = new Stage(StageStyle.UTILITY);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(btSignUp.getScene().getWindow());
			dialogStage.setTitle("수정");

			TextField tfDialogId = (TextField) signUpRoot.lookup("#tfDialogId");
			PasswordField tfDialogPassword = (PasswordField) signUpRoot.lookup("#tfDialogPassword");
			TextField tfDialogName = (TextField) signUpRoot.lookup("#tfDialogName");
			TextField tfDialogPhone = (TextField) signUpRoot.lookup("#tfDialogPhone");
			Button btDialogOk = (Button) signUpRoot.lookup("#btDialogOk");
			Button btDialogCancle = (Button) signUpRoot.lookup("#btDialogCancle");

			btDialogOk.setOnAction(e1 -> {

				try {

					if (tfDialogId.getText().equals("") || tfDialogPassword.getText().equals("")
							|| tfDialogName.getText().equals("") || tfDialogPhone.getText().equals("")) {
						AlertManager.getInstance().show(AlertInfo.FAIL_SIGNUP_INCOMPLETE_FORM, null);

					} else {

						ClientVO cvo = new ClientVO(tfDialogId.getText().trim(), tfDialogPassword.getText().trim(),
								tfDialogName.getText().trim(), tfDialogPhone.getText().trim());

						clientDVO = new ClientDAO();

						clientList = clientDVO.getClientCheck(cvo);

						if (duplicateCheckId(cvo.getId(), clientList) || clientList == null) {
							clientDVO.insertClientDB(cvo);
							
							AlertManager.getInstance().show(AlertInfo.SUCCESS_SIGNUP, buttonType -> {
								dialogStage.close();
							});
						} else {
							AlertManager.getInstance().show(AlertInfo.FAIL_SIGNUP_DUPLICATE_ID, null);
						}

					}

				} catch (Exception e2) {
					
					e2.printStackTrace();
					AlertManager.getInstance().show(AlertInfo.ERROR_UNKNOWN, null);
				}

			});

			btDialogCancle.setOnAction(e2 -> {

				dialogStage.close();

			});

			dialogStage.setScene(scene);
			dialogStage.setResizable(false);
			dialogStage.show();

		} catch (Exception e1) {
			
			e1.printStackTrace();
			AlertManager.getInstance().show(AlertInfo.ERROR_LOAD_SCENE, null);
			
		}

	} // end of handlerButtonSignUpAction

	// end of handler Method

	private void initSetting() {

		clientDVO = new ClientDAO();

		clientList = clientDVO.getClientInfo();

	} // end of initSetting

	public boolean duplicateCheckId(String id, ArrayList<ClientVO> cvoList) {

		if (cvoList.equals(null)) {

			return true;

		}

		for (int i = 0; i < cvoList.size(); i++) {

			if (cvoList.get(i).getId().equals(id)) {
				return false;
			}

		}

		return true;

	} // end of duplicateCheckId

	public boolean checkPassword(String password, ArrayList<ClientVO> cvoList) {

		if (cvoList.equals(null)) {

			return false;

		}

		for (int i = 0; i < cvoList.size(); i++) {

			if (cvoList.get(i).getPassword().equals(password)) {
				return true;
			}

		}

		return false;

	} // end of checkPassword
	
	public boolean checkId(String id, ArrayList<ClientVO> cvoList) {
		
		if (cvoList.equals(null)) {

			return false;

		}

		for (int i = 0; i < cvoList.size(); i++) {

			if (cvoList.get(i).getId().equals(id)) {
				return true;
			}

		}
		return false;
	}
}
