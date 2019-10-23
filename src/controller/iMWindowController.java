package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.iMTableDAO;
import model.iMTableVO;

public class iMWindowController implements Initializable {

	@FXML
	private Button buttonIM; // 1. 재고 관리버튼
	@FXML
	private Button buttonIMClose; // 2. 재고관리 창 닫기
	@FXML
	private TextField textFieldIMSearch; // 검색값 입력 TextField
	@FXML
	private Button buttonIMSearch; // 3. 검색 버튼
	@FXML
	private Button buttonIMSearchAll; // 4. 전체 검색 버튼
	@FXML
	private StackedBarChart<String, Integer> stackedBarChartIMChart; // chart
	@FXML
	private TableView<iMTableVO> tableView; // 5. 차트
	@FXML
	private DatePicker datePickerIMSelect;
	@FXML
	private Button btDelete;

	ObservableList<iMTableVO> data; // 테이블뷰에 보여주기위해서 저장된 데이타
	public boolean flagCheck = false;
	private LocalDate local = null;
	private String localString = null;

	// 테이블 뷰에서 선택된 정보 저장
	private ObservableList<iMTableVO> selectTableViewVO = FXCollections.observableArrayList();
	private int selectTableViewIndex = 0;
	private iMTableVO selectTableView = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		handlerTableView();

		// 1. 재고 확인 버튼
		buttonIM.setOnAction(e -> {
			handlerButtonIM(e);
		});

		// 2. 닫기 버튼
		buttonIMClose.setOnAction(e -> {
			handlerButtonIMClose(e);
		});

		// 3. 검색 버튼
		buttonIMSearch.setOnAction(e -> {
			handlerButtonIMSearch(e);
		});

		// 4. 전체 검색 버튼
		buttonIMSearchAll.setOnAction(e -> {
			handlerButtonIMSearchAll(e);
		});

		// 5. 차트
		tableView.setOnMousePressed(e -> {
			selectTableViewVO = tableView.getSelectionModel().getSelectedItems();
			selectTableViewIndex = tableView.getSelectionModel().getSelectedIndex();
			selectTableView = tableView.getSelectionModel().getSelectedItem();
		});

		// 6. datepicker
		datePickerIMSelect.setOnAction(e -> handlerDatePickerAction(e));

		// 7. 삭제 버튼
		btDelete.setOnAction(e -> handlerButtonDeleteAction(e));

	}

	private void iMbarChartSetting() {

		try {

			ObservableList<XYChart.Data<String, Integer>> barChartList = FXCollections.observableArrayList();
			XYChart.Series<String, Integer> series = new XYChart.Series<>();

			stackedBarChartIMChart.setTitle(localString + "재고 현황");

			if (!(barChartList.equals(null))) {
				barChartList.removeAll(barChartList);
			}

			iMTableDAO iMTableDAO = new iMTableDAO();
			ObservableList<iMTableVO> setDataForBarChart = iMTableDAO.barChartDatabase(localString);

			for (int i = 0; i < setDataForBarChart.size(); i++) {
				barChartList.add(new XYChart.Data<String, Integer>(setDataForBarChart.get(i).getName(),
						(setDataForBarChart.get(i).getEa())));
			}

			series.getData().clear();
			stackedBarChartIMChart.getData().clear();
			
			series.setName(localString);
			series.setData(barChartList);
			stackedBarChartIMChart.getData().add(series);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void handlerButtonDeleteAction(ActionEvent e) {

		try {
			iMTableDAO iMTableDAO = new iMTableDAO();
			boolean deleteCheck = iMTableDAO.deleteData(selectTableViewVO.get(0).getNo());

			if (deleteCheck) {

				alertWaringDisplay(2, "삭제성공", "재고를 삭제하는데 성공하였습니다.", " 삭제하였습니다.");
				ObservableList<iMTableVO> deleteAfterData = FXCollections
						.observableArrayList(iMTableDAO.getiMTableTBLTotal());
				data.removeAll(data);
				data.addAll(deleteAfterData);
				tableView.setItems(data);
			} else {

				alertWaringDisplay(2, "삭제실패", "재고를 삭제하는데 실패하였습니다.", " 실패했습니다.");

			}
		} catch (ClassNotFoundException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return;
	}

	private void handlerDatePickerAction(ActionEvent e) {

		try {
			local = datePickerIMSelect.getValue();
			if (local.getDayOfMonth() < 10) {
				localString = String.valueOf(local.getYear()) + "-" + String.valueOf(local.getMonthValue()) + "-" + "0"
						+ String.valueOf(local.getDayOfMonth());
			} else {
				localString = String.valueOf(local.getYear()) + "-" + String.valueOf(local.getMonthValue()) + "-"
						+ String.valueOf(local.getDayOfMonth());
			}

			
			iMTableDAO iMTableDAO = new iMTableDAO();
			ObservableList<iMTableVO> loadToDateList = iMTableDAO.getListToDate(localString);
			
			tableView.setItems(loadToDateList);
			
			iMbarChartSetting();

		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	// 1. 재고 확인 버튼을 눌렀을 때 추가 메뉴창 열림
	private void handlerButtonIM(ActionEvent e) {

		try {
			AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/imView.fxml"));
			Stage stageDialog = new Stage(StageStyle.UTILITY);

			stageDialog.initModality(Modality.WINDOW_MODAL);
			stageDialog.initOwner(buttonIM.getScene().getWindow());
			stageDialog.setTitle("품목 등록에 오신 것을 환영합니다.");
			stageDialog.setResizable(false);

			Scene scene = new Scene(anchorPane);

			stageDialog.setScene(scene);
			stageDialog.show(); // window show

			DatePicker datethis = (DatePicker) anchorPane.lookup("#datethis");
			TextField iMNum = (TextField) anchorPane.lookup("#iMNum");
			ComboBox iMName = (ComboBox) anchorPane.lookup("#iMName");
			TextField iMPay = (TextField) anchorPane.lookup("#iMPay");
			Button iMOk = (Button) anchorPane.lookup("#iMOk");
			Button iMClose = (Button) anchorPane.lookup("#iMClose");

			/************************ 입력제한 *************************/

			DecimalFormat format = new DecimalFormat("######");
			// 단가 자릿수 또는 문자 제한(입력)
			iMPay.setTextFormatter(new TextFormatter<>(e2 -> {

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

			DecimalFormat format1 = new DecimalFormat("###");
			// 수량 자릿수 또는 문자 제한(입력)
			iMNum.setTextFormatter(new TextFormatter<>(e2 -> {

				if (e2.getControlNewText().isEmpty()) {
					return e2;
				}

				ParsePosition parsePosition = new ParsePosition(0);
				Object object = format1.parse(e2.getControlNewText(), parsePosition);

				if (object == null || parsePosition.getIndex() < e2.getControlNewText().length()
						|| e2.getControlNewText().length() == 4) {
					return null;
				} else {
					return e2;
				}

			}));

			/********************* 입력제한문 종료지점 ********************/

			// list ComboBox에 남자와 여자 리스트 값을 넣는다.
			ObservableList list = FXCollections.observableArrayList();

			list.add("생닭");
			list.add("양념");
			list.add("마늘");
			list.add("무우");
			list.add("튀김유");
			iMName.setItems(list);

			// 품목을 추가할 때 날짜 선택
			datethis.setOnAction(e1 -> {

				LocalDate date = datethis.getValue(); // 날자를 누를때마다 값을 찍어준다.
//				datethis.setText("" + date);

			});

			// 재고 리스트 작성하고 등록하기 버튼을 눌렀을 때
			iMOk.setOnAction(e1 -> {

//					// 등록 정보를 모두 입력시 성공
				if (datethis.getValue().equals("") || iMNum.getText().equals("") || iMName.getValue().equals("")
						|| iMPay.getText().equals("")) {

					alertWaringDisplay(1, "재고 등록", "재고등록에 실패하였습니다.", "정보를 모두 입력해주십시오.");
					return;

				} else {

					// DB에 입력 값들을 저장한다.
					iMTableVO imTableVO = new iMTableVO(datethis.getValue().toString(),
							iMName.getSelectionModel().getSelectedItem().toString(), Integer.parseInt(iMPay.getText()),
							Integer.parseInt(iMNum.getText()));

					iMTableDAO imTableDAO = new iMTableDAO();

					imTableDAO.insertClientIM(imTableVO);

					alertWaringDisplay(1, "축하드립니다.", "재고등록에 성공하였습니다.", "좋은 시간되십시오");
				} // end of if

				stageDialog.close(); // this is window close
				flagCheck = false; // this is 들어간 값 지우기

				iMOk.setOnAction(e2 -> {

					((Stage) iMClose.getScene().getWindow()).close();

				});
			});

			// 1) 재고등록 창 닫기 버튼
			iMClose.setOnAction(e1 -> {

				((Stage) iMClose.getScene().getWindow()).close();

			}); // 1) end of 재고등록 창 닫기 버튼

		} catch (IOException e1) {

			e1.printStackTrace();

		}

	}

	private void alertWaringDisplay(int type, String title, String headerText, String contentText) {

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

	// 2. 닫기 버튼을 눌렀을 때 재고관리 창 닫기
	private void handlerButtonIMClose(ActionEvent e) {

		((Stage) buttonIMClose.getScene().getWindow()).close();

	}

	// 3. 검색 버튼
	private void handlerButtonIMSearch(ActionEvent e) {

		try {
			if (textFieldIMSearch.getText().equals("")) {
				return;
			}
			ObservableList<iMTableVO> listIM = FXCollections.observableArrayList();
			iMTableDAO imTableDAO = new iMTableDAO();

			listIM = imTableDAO.getSearchData(textFieldIMSearch.getText());
			System.out.println(listIM.toString());
			if (listIM.isEmpty()) {

				throw new Exception("검색오류");

			} else {
				data.removeAll(data);
				data.addAll(listIM);
				tableView.setItems(data);
			}

		} catch (Exception e1) {

			alertWaringDisplay(1, "검색결과", "이름검색오류", e1.toString());

		}

	}

	// 4. 전체 검색 버튼
	private void handlerButtonIMSearchAll(ActionEvent e) {

		ArrayList<iMTableVO> listIM = null;
		iMTableDAO imTableDAO = new iMTableDAO();
		iMTableVO imTableVO = null;

		listIM = imTableDAO.getiMTableTBLTotal();

		try {

			if (listIM == null) {

				throw new Exception("검색오류");

			}

			data.removeAll(data);

			for (iMTableVO svo : listIM) {

				data.add(svo);

			}

			tableView.setItems(data);
			datePickerIMSelect.setValue(null);

		} catch (Exception e1) {

			alertWaringDisplay(1, "검색결과", "이름검색오류", e1.toString());

		}

	}

	// 5. 차트
	private void handlerTableView() {

		data = FXCollections.observableArrayList();

		// 5.1 테이블 뷰에서 리스트 보이기 작성
		TableColumn colNo = new TableColumn("NO");
		colNo.setMaxWidth(75);
		colNo.setStyle("-fx-alignment: CENTER;");
		colNo.setCellValueFactory(new PropertyValueFactory("no"));

		TableColumn colDate = new TableColumn("날짜");
		colDate.setMaxWidth(130);
		colDate.setStyle("-fx-alignment: CENTER;");
		colDate.setCellValueFactory(new PropertyValueFactory("date"));

		TableColumn colName = new TableColumn("품명");
		colName.setMaxWidth(130);
		colName.setStyle("-fx-alignment: CENTER;");
		colName.setCellValueFactory(new PropertyValueFactory("name"));

		TableColumn colPay = new TableColumn("단가");
		colPay.setMaxWidth(80);
		colPay.setStyle("-fx-alignment: CENTER;");
		colPay.setCellValueFactory(new PropertyValueFactory("pay"));

		TableColumn colEa = new TableColumn("수량");
		colEa.setMaxWidth(75);
		colEa.setStyle("-fx-alignment: CENTER;");
		colEa.setCellValueFactory(new PropertyValueFactory("ea"));

		TableColumn colRemakers = new TableColumn("비고");
		colRemakers.setMaxWidth(100);
		colRemakers.setStyle("-fx-alignment: CENTER;");
		colRemakers.setCellValueFactory(new PropertyValueFactory("remakers"));

		tableView.setItems(data);
		tableView.getColumns().addAll(colNo, colDate, colName, colPay, colEa, colRemakers);

	}

}
