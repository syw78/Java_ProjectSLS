package controller;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.DBUtilDAO;
import model.DBVO;

public class LoginController implements Initializable {
	@FXML
	private TextField txtField; // 아이디 입력창
	@FXML
	private PasswordField passwordField; // 비밀번호 입력창
	@FXML
	private Button btnOk;
	@FXML
	private Button btnExit;
	@FXML
	private Button btnSignUP;
	@FXML
	private Button btnMaster;
	@FXML
	private Button btnExp1;

	private DBUtilDAO dbUtilDAO;
	ArrayList<DBVO> data = new ArrayList<DBVO>();
	private boolean flagCheckID = false;
	private DBVO dbVO;
	private DBVO saveDBVO;
	private boolean flagLogin = false;

	@FXML
	private Button btMaster;

	public static String clientId = null;

	String str;
	
	/******************************
	 * 
	 *  로그인 창 화면
	 * 
	 *  서연우
	 *  
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// 그림 눌렀을때 관리자 모드
		btnMaster.setOnAction((event) -> {
			Parent adminView = null;
			Stage adminStage = null;

			try {
				adminView = FXMLLoader.load(getClass().getResource("/view/adminlogin.fxml"));
				Scene scene = new Scene(adminView);
				adminStage = new Stage();
				adminStage.setTitle("Admin");
				adminStage.setScene(scene);
				adminStage.setResizable(true);
				adminStage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		
		//돋보기 버튼 눌렀을때 
	      btnExp1.setOnAction(event -> {
	         try {
	            Parent dialogView = FXMLLoader.load(getClass().getResource("/view/explain.fxml"));
	            Stage explainStage = new Stage(StageStyle.UTILITY);
	            explainStage.initModality(Modality.WINDOW_MODAL);
	            explainStage.initOwner(btnOk.getScene().getWindow());
	            explainStage.setTitle("설명 창");
	            Scene scene = new Scene(dialogView);
	            
	            TextArea textArea = (TextArea)dialogView.lookup("#textArea");
	            
	            textArea.setEditable(false);
	            
	            explainStage.setScene(scene);
	            explainStage.setResizable(false);
	            explainStage.show();
	            
	         } catch (IOException e1) {
	            // TODO Auto-generated catch block
	            e1.printStackTrace();
	         }
	         
	      });

		// 로그인 버튼 눌렀을때
		btnOk.setOnAction(e -> {
			totalList(); // 디비 데이터 가져온다.

			// 아이디 비밀번호 미입력시
			if (txtField.getText().equals("") || passwordField.getText().equals("")) {
				alertDisplay(1, "로그인실패", "아이디,패스워드 미입력", "입력해주세요");
				return;
			}

			boolean nametest = false;

			for (int i = 0; i < data.size(); i++) {

				nametest = txtField.getText().equals(data.get(i).getTxtId());

				if (txtField.getText().equals(data.get(i).getTxtId())) { // DB에있는 아이디와 로그인 아이디창이 일치하면 실행
					if (!(passwordField.getText().equals(data.get(i).getTxtFiled()))) {
						alertDisplay(1, "경고경고", "아이디 혹은 비밀번호 불일치", "다시 입력바랍니다.");
						return;
					}

					Parent mainView = null;
					Stage mainStage = null;

					try {
						clientId = txtField.getText();
						mainView = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
						Scene scene = new Scene(mainView);
						mainStage = new Stage();
						mainStage.setTitle("new");
						mainStage.setScene(scene);
						mainStage.setResizable(true);
						mainStage.show();
						break;

					} catch (Exception e1) {

						alertDisplay(1, "메인창 콜실패", "메인창 부르기 실패", e.toString() + e1.getMessage());
					}

				}
			} // end of for
			if (!nametest) {
				alertDisplay(1, "경고경고", "등록되지 않은 아이디입니다", "회원가입을 해주세요");
			}
		});// end of login Button

		// 취소 버튼
		btnExit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Platform.exit();
			}
		});

		// 회원가입 버튼 눌렀을 경우
		btnSignUP.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				try {
					Parent dialogView = FXMLLoader.load(getClass().getResource("/view/signup.fxml"));
					Stage mainStage2 = new Stage(StageStyle.UTILITY);
					mainStage2.initModality(Modality.WINDOW_MODAL);
					mainStage2.initOwner(btnOk.getScene().getWindow());
					mainStage2.setTitle("회원가입");
					TextField txtId = (TextField) dialogView.lookup("#txtId");
					PasswordField txtField = (PasswordField) dialogView.lookup("#txtField");
					PasswordField txtField2 = (PasswordField) dialogView.lookup("#txtField2");
					TextField txtName = (TextField) dialogView.lookup("#txtName");
					TextField frontN = (TextField) dialogView.lookup("#frontN");
					TextField backN = (TextField) dialogView.lookup("#backN");
					ComboBox comboFM = (ComboBox) dialogView.lookup("#comboFM");
					TextField phoneNum = (TextField) dialogView.lookup("#phoneNum");
					TextField enterpriseName = (TextField) dialogView.lookup("#tfEnterpriseName");

					Button overlap = (Button) dialogView.lookup("#overlap");
					Button btnUP = (Button) dialogView.lookup("#btnUP");
					Button btnClose = (Button) dialogView.lookup("#btnClose");

					btnUP.setDisable(true);

					// 중복확인버튼
					overlap.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {

							// 아무것도 입력을 안했을때
							if (txtId.getText().equals("") != false) {
								alertDisplay(1, "경고경고", "아이디를 입력해주세요", "아이디 입력이 안됬습니다.");
							}
							DBUtilDAO tt = new DBUtilDAO();
							ArrayList<DBVO> test = tt.getUserIdtest(txtId.getText());
							if (test.size() == 1) {
								alertDisplay(1, "중복중복", "이미 사용중인 아이디 입니다", "다른아이디 입력바람");
							} else {
								alertDisplay(2, "가능가능", "사용가능한 아이디입니다.", "사용가능 사용가능");
								btnUP.setDisable(false);
							}

						}

					});

					DecimalFormat format = new DecimalFormat("######");
					// 주민 앞자리 제한
					frontN.setTextFormatter(new TextFormatter<>(e2 -> {
						if (e2.getControlNewText().isEmpty()) {
							return e2;
						}
						ParsePosition parsePosition = new ParsePosition(0);
						Object object = format.parse(e2.getControlNewText(), parsePosition);
						if (object == null || parsePosition.getIndex() < e2.getControlNewText().length()
								|| e2.getControlNewText().length() == 7) {
							return null;
						} else {
							return e2;
						}
					}));
					// 주민 뒷자리 제한
					backN.setTextFormatter(new TextFormatter<>(e3 -> {
						if (e3.getControlNewText().isEmpty()) {
							return e3;
						}
						ParsePosition parsePosition = new ParsePosition(0);
						Object object = format.parse(e3.getControlNewText(), parsePosition);
						if (object == null || parsePosition.getIndex() < e3.getControlNewText().length()
								|| e3.getControlNewText().length() == 8) {
							return null;
						} else {
							return e3;
						}
					}));
					// 폰번호 앞자리
					phoneNum.setTextFormatter(new TextFormatter<>(e3 -> {
						if (e3.getControlNewText().isEmpty()) {
							return e3;
						}
						ParsePosition parsePosition = new ParsePosition(0);
						Object object = format.parse(e3.getControlNewText(), parsePosition);
						if (object == null || parsePosition.getIndex() < e3.getControlNewText().length()
								|| e3.getControlNewText().length() == 12) {
							return null;
						} else {
							return e3;
						}
					}));

					// 콤보박스 남자여자 항목 추가
					ObservableList MF = FXCollections.observableArrayList();
					MF.add(0, "남자");
					MF.add(1, "여자");
					comboFM.setValue("선택");
					comboFM.setItems(MF);

					// 회원가입창에서 등록버튼 눌렀을때
					btnUP.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							if (!(txtId.getText().equals("") || txtField.getText().equals("")
									|| txtField2.getText().equals("") || txtName.getText().equals("")
									|| frontN.getText().equals("") || backN.getText().equals("")
									|| phoneNum.getText().equals("") || enterpriseName.getText().equals(""))) {

								// || midN.getText().equals("") || lastN.getText().equals("")

								if (checkbackN(Integer.parseInt(backN.getText()))) {
									alertDisplay(1, "경고경고", "이미 가입한 주민번호입니다", "다시 입력해주세요.");
									return;
								} else if (txtField.getText().equals(txtField2.getText())) {

									DBVO dvo = new DBVO(txtId.getText(), txtField.getText(), txtField2.getText(),
											txtName.getText(), Integer.parseInt(frontN.getText()),
											Integer.parseInt(backN.getText()),
											comboFM.getSelectionModel().getSelectedItem().toString(),
											phoneNum.getText(), enterpriseName.getText());
									// , Integer.parseInt(midN.getText()),Integer.parseInt(lastN.getText())

									dbUtilDAO = new DBUtilDAO();
									dbUtilDAO.insertClient(dvo); // 등록
									alertDisplay(1, "가입완료", "가입이 완료되었습니다", "감사합니다");
									mainStage2.close();
								} else {
									alertDisplay(1, "경고경고", "비밀번호를 확인해주세요", "비밀번호가 일치하지 않습니다.");
								}

							} else {
								alertDisplay(1, "경고경고", "모두 입력해주세요", "모두 입력 바랍니다.");
								return;

							}
						}
					});
					// 회원가입창에서 취소 눌렀을때
					btnClose.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							mainStage2.close();
						}
					});

					Scene scene = new Scene(dialogView);
					mainStage2.setScene(scene);
					mainStage2.setResizable(false);
					mainStage2.show();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	// 경고창
	protected void alertDisplay(int type, String title, String headerText, String contentText) {
		Alert alert = null;
		switch (type) {
		case 1:
			alert = new Alert(AlertType.WARNING);
			break;
		case 2:
			alert = new Alert(AlertType.CONFIRMATION);
			break;
		case 3:
			alert = new Alert(AlertType.ERROR);
			break;
		case 4:
			alert = new Alert(AlertType.NONE);
			break;
		case 5:
			alert = new Alert(AlertType.INFORMATION);
			break;
		}
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(headerText + "\n" + contentText);
		alert.setResizable(false);
		alert.show();
	}

	// 회원정보DB에있는 정보 가져오기
	public void totalList() {

		DBUtilDAO dbUtilDAO = new DBUtilDAO();

		data = dbUtilDAO.getCustomerTotal();
		System.out.println(data.size());
	}

	// 아이디 중복확인
	public boolean checkID(String id) {
		totalList();
		System.out.println(data.size());
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).getTxtId().equals(id)) {
				flagCheckID = false;
				System.out.println(data.get(i).getTxtId());
				return flagCheckID;
			} else {
				flagCheckID = true;
			}
		}
		System.out.println(data);
		return flagCheckID;

	}

	// 주민 뒷자리 중복확인
	public boolean checkbackN(int backN) {
		totalList();
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).getBackN() == backN) {
				flagCheckID = true;
				return flagCheckID;
			} else {
				flagCheckID = false;
			}
		}
		return flagCheckID;
	}

}
