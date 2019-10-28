package controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.DBUtilDAO;
import model.DBVO;
import util.AlertManager;
import util.AlertManager.AlertInfo;
import util.DBUtil;

public class AdminMainController implements Initializable {

	@FXML
	private TableView<DBVO> tableView;
	@FXML
	private ImageView imageView;
	@FXML
	private Button btnRevise;
	@FXML
	private Button btnSearch;
	@FXML
	private Button btnDelete1;
	@FXML
	private TextField adminTF;
	@FXML
	private Button btnTotal;
	@FXML
	private Button btnExit;
	
	private int selectedIndex;
	private ObservableList<DBVO> selectClient;
	private String selectFileName = ""; // 이미지 파일명
	private String localUrl = ""; // 이미지 파일 경로
	private Image localImage;
	private File selectedFile = null;
	// 이미지 처리
	// 이미지 저장할 폴더를 매개변수로 파일 객체 생성
	private File dirSave = new File("/Users/xim/Developer/BackUpJavaProject/image");
	// 이미지 불러올 파일을 저장할 파일 객체 선언
	private File file = null;

	ObservableList<DBVO> data = FXCollections.observableArrayList();
	DBUtilDAO dbUtilDAO = new DBUtilDAO();
	// private boolean delete = false;

	// tableView select 변수
	private ObservableList<DBVO> selectDBVO = FXCollections.observableArrayList();
	private int selectDBVOIndex = 0;
	/***********************
	 * 
	 * 관리자 창
	 * 
	 * 서연우
	 * 
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		tableViewSetting();

		// 테이블뷰 항목 눌렀을때
		tableView.setOnMousePressed(event -> {
			handlerTableViewMousePressedAction(event);
		});
		// 수정버튼 눌렀을때
		btnRevise.setOnAction(event -> {
			handlerButtonReviseAction(event);

		});
		// 검색버튼 눌렀을때
		btnSearch.setOnAction(event -> {
			try {
				ArrayList<DBVO> list = new ArrayList<DBVO>();
				DBUtilDAO dbUtilDAO = new DBUtilDAO();
				list = dbUtilDAO.getCustomerCheck(adminTF.getText().trim());

				if (list == null) {
					throw new Exception("검색오류");
				}

				data.removeAll(data);
				for (DBVO svo : list) {
					data.add(svo);
				}
			} catch (Exception e1) {
				alertDisplay(1, "검색결과", "이름검색오류", e1.toString());
			}
		});

		// 전체 버튼 눌렀을때
		btnTotal.setOnAction(event -> {
			dbUtilDAO = new DBUtilDAO();
			System.out.println("ㅇㅇㅇ");
			data.removeAll(data);

			totalList();

		});
		
		btnExit.setOnAction(e -> {
			Platform.exit();
		});
		
		
	      
		// 삭제 버튼 눌렀을때
		btnDelete1.setOnAction(event -> {
			try {
				DBUtilDAO dbUtilDAO = new DBUtilDAO();
				dbUtilDAO.getCustomerDelete(selectDBVO.get(0).getTxtId());
				data.removeAll(data);
				totalList();
			} catch (Exception e1) {
				AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DELETE, null);
			}
		});

	}

	// adminMain에서 수정버튼 눌렀을때
	private void handlerButtonReviseAction(ActionEvent event) {
		try {

			Parent dialogReviseroot = FXMLLoader.load(getClass().getResource("/view/adminrevise.fxml"));
			Stage reviseStage = new Stage(StageStyle.UTILITY);
			reviseStage.initModality(Modality.WINDOW_MODAL);
			reviseStage.initOwner(btnRevise.getScene().getWindow());
			reviseStage.setTitle("정보수정");

			TextField reviseId = (TextField) dialogReviseroot.lookup("#reviseId");
			TextField reviseName = (TextField) dialogReviseroot.lookup("#reviseName");
			TextField reviseNum = (TextField) dialogReviseroot.lookup("#reviseNum");
			TextField reviseEnterprise = (TextField) dialogReviseroot.lookup("#reviseEnterprise");

			Button btnReviseAdd = (Button) dialogReviseroot.lookup("#btnReviseAdd");
			Button btnReviseExit = (Button) dialogReviseroot.lookup("#btnReviseExit");
			Button btnImageAdd = (Button) dialogReviseroot.lookup("#btnImageAdd");

			ImageView reviseImage = (ImageView) dialogReviseroot.lookup("#reviseImage");

			// 수정 창아이디
			reviseId.setPromptText(selectDBVO.get(0).getTxtId()); // 흑백으로 필드 설정
			reviseId.setEditable(false); // 글씨 못쓰게
			reviseId.setDisable(true); // 창을 활성화를 안시킴

			reviseName.setText(selectDBVO.get(0).getTxtName());
			reviseNum.setText(selectDBVO.get(0).getPhoneNum());
			reviseEnterprise.setText(selectDBVO.get(0).getEnterpriseName());

			// 수정 창 이미지추가 버튼 눌렀을때
			btnImageAdd.setOnAction(event2 -> {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("이미지추가");
				fileChooser.setInitialDirectory(new File("/Users/xim/Developer/BackUpJavaProject/image/")); // 디렉토리 위치를 지정
				ExtensionFilter imageType = new ExtensionFilter("image file", "*.jpg", "*.png", "*.gif"); // 타입을 지정
				fileChooser.getExtensionFilters().add(imageType); // 타입을 넣어주는것

				try {
					selectedFile = fileChooser.showOpenDialog(btnRevise.getScene().getWindow()); // 보여줘서 그 값을 저장

					if (selectedFile != null) {
						// 이미지 파일 경로
						localUrl = selectedFile.toURI().toURL().toString();
					}
				} catch (MalformedURLException e2) {
					AlertManager.getInstance().show(AlertInfo.FAIL_LOAD_IMAGE, null);
					e2.printStackTrace();
				}
				localImage = new Image(localUrl, false);
				imageView.setImage(localImage);
				imageView.setFitHeight(250);
				imageView.setFitWidth(230);
				reviseImage.setImage(localImage);
				if (selectedFile != null) {
					selectFileName = selectedFile.getName();
				}
			});

			// 수정 창 등록버튼 눌렀을 때
			btnReviseAdd.setOnAction(event3 -> {
				try {
					if (reviseName.getText().equals("") || reviseNum.getText().equals("")
							|| reviseEnterprise.getText().equals("")) {
						alertDisplay(1, "입력입력", "수정 값을 입력하시오", "입력요망.");
						return;
					}

					File dirMake = new File(dirSave.getAbsolutePath());
					// 이미지 저장 폴더 생성
					if (!dirMake.exists()) {
						dirMake.mkdir();
					}
					// 이미지 파일 저장
					String fileName = imageSave(selectedFile);
					DBVO dvo = new DBVO(selectDBVO.get(0).getTxtId(), reviseName.getText().trim(),
							reviseNum.getText().trim(), reviseEnterprise.getText().trim(), fileName);
					data.remove(selectDBVOIndex);
					data.add(selectDBVOIndex, dvo);

					DBUtilDAO dbUtilDAO = new DBUtilDAO();
					DBVO dbVO = dbUtilDAO.getCustomerUpdate(dvo);
					reviseStage.close();

				} catch (Exception e2) {
					e2.printStackTrace();
					AlertManager.getInstance().show(AlertInfo.ERROR_TASK, null);
					
				}
			});
			// 취소버튼을 눌렀을때
			btnReviseExit.setOnAction(e3 -> {
				reviseStage.close();
			});

			Scene scene = new Scene(dialogReviseroot);
			reviseStage.setScene(scene);
			reviseStage.show();

		} catch (IOException e) {
			AlertManager.getInstance().show(AlertInfo.ERROR_TASK, null);
			e.printStackTrace();
		}

	}
	//이미지 저장
	public String imageSave(File file) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		int data = -1;
		String fileName = null;
		try {
			// 이미지 파일명 생성
			fileName = "cusotomer" + System.currentTimeMillis() + "_" + file.getName();
			bis = new BufferedInputStream(new FileInputStream(file));
			bos = new BufferedOutputStream(new FileOutputStream(dirSave.getAbsolutePath() + "//" + fileName));

			// 선택한 이미지 파일 InputStream의 마지막에 이르렀을 경우는 -1
			while ((data = bis.read()) != -1) {
				bos.write(data);
				bos.flush();
			}
		} catch (Exception e) {
			AlertManager.getInstance().show(AlertInfo.ERROR_TASK, null);
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
				if (bis != null) {
					bis.close();
				}
			} catch (IOException e) {
				AlertManager.getInstance().show(AlertInfo.ERROR_TASK, null);
				e.getMessage();
			}
		}
		return fileName;
	}

	// 테이블 항목 눌렀을때
	private void handlerTableViewMousePressedAction(MouseEvent event) {

		selectDBVOIndex = tableView.getSelectionModel().getSelectedIndex();
		selectDBVO = tableView.getSelectionModel().getSelectedItems();

		String fileName = selectDBVO.get(0).getFileName();
		selectedFile = new File("/Users/xim/Developer/BackUpJavaProject/image/" + fileName);
		if (selectedFile != null) {
			// 이미지 파일 경로
			try {
				localUrl = selectedFile.toURI().toURL().toString();
			} catch (MalformedURLException e) {
				AlertManager.getInstance().show(AlertInfo.FAIL_LOAD_IMAGE, null);
				e.printStackTrace();
			}
			localImage = new Image(localUrl, false);
			imageView.setImage(localImage);

		}

	}

	// 컬럼셋팅
	private void tableViewSetting() {

		// 컬럼셋팅-------------------------------------------------------------------------------------

		TableColumn colId = new TableColumn("아이디");
		colId.setMaxWidth(70);
		colId.setStyle("-fx-alignment: CENTER;");
		colId.setCellValueFactory(new PropertyValueFactory("txtId"));

		TableColumn colName = new TableColumn("회원명");
		colName.setMaxWidth(70);
		colName.setStyle("-fx-alignment: CENTER;");
		colName.setCellValueFactory(new PropertyValueFactory("txtName"));

		TableColumn colNum = new TableColumn("전화번호");
		colNum.setMaxWidth(150);
		colNum.setStyle("-fx-alignment: CENTER;");
		colNum.setCellValueFactory(new PropertyValueFactory("phoneNum"));

		TableColumn colCom = new TableColumn("업체명");
		colCom.setMaxWidth(150);
		colCom.setStyle("-fx-alignment: CENTER;");
		colCom.setCellValueFactory(new PropertyValueFactory("enterpriseName"));

		TableColumn colFile = new TableColumn("이미지");
		colFile.setMaxWidth(150);
		colFile.setStyle("-fx-alignment: CENTER;");
		colFile.setCellValueFactory(new PropertyValueFactory("fileName"));

		// 2. 테이블설정 컬럼들 객체를 테이블뷰에 리스트추가 및 항목추가

		totalList();
		tableView.setItems(data);

		tableView.getColumns().addAll(colId, colName, colNum, colCom, colFile);
		// ------------------------------------------------------------------------------------------------------
	}

	public static void alertDisplay(int type, String title, String headerText, String contentText) {

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

	public void totalList() {
		ArrayList<DBVO> list = null;
		DBUtilDAO dbUtilDAO = new DBUtilDAO();
		DBVO dbVO = null;
		list = dbUtilDAO.getCusTotal();
		if (list == null) {
			AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			dbVO = list.get(i);
			data.add(dbVO);
		}
	}
}
