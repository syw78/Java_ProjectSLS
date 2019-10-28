package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.CodeDAO;
import model.CodeVO;
import util.AlertManager;
import util.AlertManager.AlertInfo;

public class AdminLoginController implements Initializable {
	@FXML
	PasswordField passwordCode;
	ArrayList<CodeVO> data;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		codeList();
		
		/*********************
		 * 
		 *  기능 : 엔터 키를 누르면 비밀번호 확인 후 창 열기
		 *  
		 *  서연우
		 *  
		 */
		passwordCode.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent keyEvent) {
				KeyCode key = keyEvent.getCode();

				if (key.equals(KeyCode.ENTER)) {
					System.out.println("1");

					for (int i = 0; i < data.size(); i++) {
						if (passwordCode.getText().equals(data.get(i).getCodeId())) {
							Parent adminView = null;
							Stage adminStage = null;
							System.out.println("2");
							try {
								adminView = FXMLLoader.load(getClass().getResource("/view/adminmain.fxml"));
								Scene scene = new Scene(adminView);
								adminStage = new Stage();
								adminStage.setTitle("AdminMain");
								adminStage.setScene(scene);
								adminStage.setResizable(true);
								adminStage.show();

							} catch (IOException e) {
								AlertManager.getInstance().show(AlertInfo.ERROR_TASK, null);
							}
							break;
						}
					}
				}
			}
		});
	}
	/*********************
	 * 
	 * 기능 : 코드의 데이터를 전부 가져온다.
	 * 
	 * 서연우
	 * 
	 */
	public void codeList() {

		CodeDAO codeDAO = new CodeDAO();

		data = codeDAO.getCodeTotal();

	}

}
