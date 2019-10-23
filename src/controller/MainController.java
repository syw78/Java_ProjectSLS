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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.ClientDAO;
import model.ClientVO;
import model.GoodsDAO;
import model.GoodsVO;
import model.SaleDAO;
import model.SaleVO;
import model.iMTableDAO;
import model.iMTableVO;
import util.AlertManager;
import util.AlertManager.AlertInfo;
import util.SceneLoader;

public class MainController implements Initializable {

	/*****************
	 * 태성
	 */
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
	private BarChart<String, Integer> stackedBarChart; // chart
	@FXML
	private TableView<iMTableVO> iMtableView; // 5. 차트
	@FXML
	private DatePicker datePickerIMSelect;
	@FXML
	private Button btDelete;
	
	ObservableList<iMTableVO> data; // 테이블뷰에 보여주기위해서 저장된 데이타
	public boolean flagIMCheck = false;
	private LocalDate local = null;
	private String localString = null;

	// 테이블 뷰에서 선택된 정보 저장
	private ObservableList<iMTableVO> selectTableViewVO = FXCollections.observableArrayList();
	private int selectTableViewIndex = 0;
	private iMTableVO selectTableView = null;

	/********************
	 * 기능 : 필요한 saleVO 모델의 변수 2019 10 월 18 일
	 * 
	 * 작성자 : 심재현
	 * 
	 */
	@FXML
	private TextField tfCount;
	@FXML
	private TextField tfTotalSalePrice;
	@FXML
	private TextField tfComents;
	@FXML
	private ComboBox<String> comboBox;
	@FXML
	private Button btCheck;
	@FXML
	private Button btSaleAdd;
	@FXML
	private Button btSaleDelete;
	@FXML
	private Button btSelectRowDelete;
	@FXML
	private Button btMenuCheck;
	@FXML
	private Button btSaleTotalPrice;
	@FXML
	private Button btRemoveTable;
	@FXML
	private DatePicker datePicker;
	@FXML
	private TableView<SaleVO> tableView;
	@FXML
	private BarChart<String, Integer> barChart;
	
	private ArrayList<SaleVO> searchDateSaleVO = new ArrayList<SaleVO>();
	private String saveGoodsString = null;

	// 삭제 후 테이블 뷰에 표시 할 리스트
	private ObservableList<SaleVO> afterDeleteSaleVOList = FXCollections.observableArrayList();
	// 디비에서 불러온 리스트 (날짜로 가져옴)
	private ObservableList<SaleVO> loadSaleVOList = FXCollections.observableArrayList();
	// 추가된 항목들을 담고있는 리스트
	private ObservableList<SaleVO> addSaleVOList = FXCollections.observableArrayList();
	// 테이블뷰 선택 시 가져오는 것
	private ObservableList<SaleVO> tableSelectSaleVOList = FXCollections.observableArrayList();
	private SaleVO tableSelectSaleVO = null;
	private int tableSelectIndex = 0;
	// 확인 버튼을 눌렀을떄 중복이 있으면 false 중복이 없으면 true
	private boolean flagCheck = false;
	// 매출 삭제 후 총액을 확인하기 위한 플래그 삭제 된 후라면 true
	private boolean flagDeleteCheck = false;

	/*******************************
	 * 기능 : 필요한 goodsVO의 변수들 2019 10 월 18 일
	 * 
	 * 작성자 : 심재현
	 * 
	 */
	private ObservableList<GoodsVO> goodsVOList = FXCollections.observableArrayList();
	private ObservableList<String> goodsNameList = FXCollections.observableArrayList();;
	private String editGoods = null;
	private ObservableList<GoodsVO> selectMenuEditGoodsVOList = FXCollections.observableArrayList();
	private GoodsVO selectMenuEditGoodsVO = null;

	/**********************************
	 * 기능 : 필요한 그 외의 변수들 2019 10 월 18 일
	 * 
	 * 작성자 : 심재현
	 * 
	 */
	private LocalDate localDate = null;
	private String localDateStr = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		buttonInitSetting(false, true, true, true, false, true);

		tableViewSetting();

		loadTotalGoodsDB();

		datePicker.setOnAction(e -> handlerDatePicker(e));

		comboBoxSettion();

		comboBox.setOnAction(e -> {

			saveGoodsString = comboBox.getSelectionModel().getSelectedItem();

		}); // end of cbGoodsList

		tfCount.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent keyEvent) {
				KeyCode keyCode = keyEvent.getCode();
				if (keyCode.equals(KeyCode.ENTER)) {
					if (localDate == null || saveGoodsString.equals(null) || tfCount.getText().equals(null)) {
						AlertManager.getInstance().show(AlertInfo.ERROR_TASK_EMPTY, null);
						buttonInitSetting(false, true, true, true, false, true);
						return;
					}

					try {
						GoodsDAO goodsDAO = new GoodsDAO();
						GoodsVO addGoodsVO = goodsDAO.getGoodsInfomation(saveGoodsString, LoginController.clientId);
						int count = Integer.parseInt(tfCount.getText());
						int total = count * addGoodsVO.getPrice();

						SaleVO addSaleVO = new SaleVO(localDateStr, addGoodsVO.getGoodsName(), addGoodsVO.getPrice(),
								count, total, tfComents.getText());
						
						addSaleVOList.add(addSaleVO);
						loadSaleVOList.add(addSaleVO);
						tableView.setItems(loadSaleVOList);
						barChart.getData().clear();
						tfTotalSalePrice.setText(null);
						buttonInitSetting(false, true, true, false, false, true);

					} catch (SQLException e) {
						AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
						e.printStackTrace();
					}
				}

			}
		});

		btSelectRowDelete.setOnAction(e -> handlerButtonRowDeleteAction(e));

		btCheck.setOnAction(e -> handlerButtonCheckAction(e));

		btSaleAdd.setOnAction(e -> handlerButtonSaleAddAction(e));

		btSaleDelete.setOnAction(e -> handlerButtonSaleDelete(e));

		btMenuCheck.setOnAction(e -> handlerButtonMenuCheck(e));

		btSaleTotalPrice.setOnAction(e -> handlerButtonSaleTotalPrice(e));

		btRemoveTable.setOnAction(e -> handlerButtonRemobeTableAction(e));

		tableView.setOnMousePressed(e1 -> {

			tableSelectSaleVOList = tableView.getSelectionModel().getSelectedItems();
			tableSelectSaleVO = tableView.getSelectionModel().getSelectedItem();
			tableSelectIndex = tableView.getSelectionModel().getSelectedIndex();

		}); // end of tableView select
		
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
		iMtableView.setOnMousePressed(e -> {
			selectTableViewVO = iMtableView.getSelectionModel().getSelectedItems();
			selectTableViewIndex = iMtableView.getSelectionModel().getSelectedIndex();
			selectTableView = iMtableView.getSelectionModel().getSelectedItem();
		});

		// 6. datepicker
		datePickerIMSelect.setOnAction(e -> handlerDatePickerAction(e));

		// 7. 삭제 버튼
		btDelete.setOnAction(e -> handlerButtonDeleteAction(e));

	} // end of initialize

	private void handlerButtonRowDeleteAction(ActionEvent e) {
		
		if(addSaleVOList.contains(tableSelectSaleVO)) {
			AlertManager.getInstance().show(AlertType.CONFIRMATION, "항목 삭제", "선택한 항목을 삭제하시겠습니까?", buttonType -> {
				if (buttonType != ButtonType.OK) {
					return;
				}

				loadSaleVOList.remove(tableSelectIndex);
				
				tableView.setItems(loadSaleVOList);

			});
		} else {
			AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DELETE, null);
			
		}

	}

	private void handlerButtonCheckAction(ActionEvent e) {

		if (loadSaleVOList.isEmpty()) {
			AlertManager.getInstance().show(AlertInfo.ERROR_TASK_EMPTY, null);
			return;
		}
//		for (int i = 0; i < loadSaleVOList.size(); i++) {
//			for (int j = 0; j < i; j++) {
//				if (loadSaleVOList.get(i).getGoods().equals(loadSaleVOList.get(j).getGoods())) {
//					flagCheck = false;
//					AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DUPLICATE, null);
//					return;
//				}
//			}
//		}

		flagCheck = true;
		AlertManager.getInstance().show(AlertInfo.SUCCESS_CHECK, null);
		buttonInitSetting(true, false, true, false, false, false);
	}

	private void comboBoxSettion() {

		try {

			GoodsDAO goodsDAO = new GoodsDAO();
			ArrayList<GoodsVO> goodsList = goodsDAO.getGoodsTotal();

			ArrayList<String> saveGoodsName = new ArrayList<String>();

			for (int i = 0; i < goodsList.size(); i++) {

				saveGoodsName.add(goodsList.get(i).getGoodsName());

			}

			goodsNameList = FXCollections.observableArrayList(saveGoodsName);

			comboBox.setItems(goodsNameList);

		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}

	}

	/*************************
	 * 
	 * 기능 : datePicker의 날짜를 선택하면 선택한 날짜의 SaleVO를 가져와서 테이블뷰에 표시한다. 2019 10 월 18 일
	 * 
	 * 작성자 : 심재현
	 * 
	 */
	private void handlerDatePicker(ActionEvent e) {

		try {

			localDate = datePicker.getValue();

			if (localDate.getDayOfMonth() < 10) {
				localDateStr = localDate.getYear() + "-" + localDate.getMonthValue() + "-" + "0"
						+ localDate.getDayOfMonth();
			} else {
				localDateStr = localDate.getYear() + "-" + localDate.getMonthValue() + "-" + localDate.getDayOfMonth();
			}

			SaleDAO saleDAO = new SaleDAO();

			loadSaleVOList = FXCollections.observableArrayList(saleDAO.getListToDate(localDateStr));

			if (!loadSaleVOList.isEmpty()) {
				buttonInitSetting(true, true, false, true, false, false);
			}

			tableView.setItems(loadSaleVOList);
			System.out.println(loadSaleVOList.toString());
			barChart.getData().clear();
			barChartSetting(loadSaleVOList);

		} catch (SQLException sqle) {
			sqle.printStackTrace();
			AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);

		}

	} // end of handlerDatePicker

	private void handlerButtonSaleAddAction(ActionEvent e) {

		if (!flagCheck) {
			AlertManager.getInstance().show(AlertInfo.FAIL_SALE_ADD_UNCHECK, null);
			return;
		} else {
			AlertManager.getInstance().show(AlertType.CONFIRMATION, "매출 등록", "매출을 등록하시겠습니까?", buttonType -> {
				if (buttonType != ButtonType.OK) {
					return;
				}
				try {
					SaleDAO saleDAO = new SaleDAO();
					for (int i = 0; i < addSaleVOList.size(); i++) {
						saleDAO.insertSaleDB(addSaleVOList.get(i));
					}
					addSaleVOList.removeAll(addSaleVOList);
					AlertManager.getInstance().show(AlertInfo.SUCCESS_TASK, null);
					buttonInitSetting(true, true, false, true, false, false);
					saleDAO.getListToDate(localDateStr);
					barChartSetting(loadSaleVOList);
				} catch (SQLException sqle) {
					AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
				}
			});
		}

	} // end of handlerButtonSaleAddAction

	/************************************
	 * 기능 : 버튼을 누르면 항목을 지운다. 2019 10 월 18 일
	 * 
	 * 작성자 : 심재현
	 * 
	 */
	private void handlerButtonSaleDelete(ActionEvent e) {

		// System.out.println(tableSelectSaleVO.toString());

		if (tableSelectSaleVO == null) {
			AlertManager.getInstance().show(AlertType.ERROR, "에러", "항목을 선택해주세요.", null);
			return;
		}

		SaleDAO saleDAO = new SaleDAO();

		AlertManager.getInstance().show(AlertType.CONFIRMATION, "항목 삭제", "선택한 항목을 삭제하시겠습니까?", buttonType -> {
			if (buttonType != ButtonType.OK) {
				return;
			}

			try {

				boolean deleted = saleDAO.deleteSale(tableSelectSaleVO);

				if (deleted) {

					AlertManager.getInstance().show(AlertInfo.SUCCESS_TASK, null);
					tableSelectSaleVO = null;

					// 삭제후 날짜 값은 선택했던 값 그대로를 유지 해야 하므로 현재의 값을 얻어와 다시 테이블뷰를 세팅한다.

					searchDateSaleVO = saleDAO.getListToDate(localDateStr);
					loadSaleVOList = FXCollections.observableArrayList(searchDateSaleVO);
					tableView.setItems(loadSaleVOList);
					barChartSetting(loadSaleVOList);
					System.out.println(loadSaleVOList.toString());

				}

			} catch (SQLException sqle) {
				sqle.printStackTrace();
				AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
			}

		});

	} // end of handlerButtonSaleDelete

	/***************************
	 * 기능 : 버튼을 누르면 메뉴 확인 다이얼로그 창을 띄운다. 2019 10 월 18 일
	 * 
	 * 작성자 : 심재현
	 * 
	 */
	private void handlerButtonMenuCheck(ActionEvent e) {
		try {
			Scene scene = SceneLoader.getInstance().makeMenuCheckScene();
			Parent menuCheckRoot = scene.getRoot();
			Stage dialogStage = new Stage(StageStyle.UTILITY);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(btMenuCheck.getScene().getWindow());
			dialogStage.setTitle("수정");

			TableView<GoodsVO> tableView = (TableView<GoodsVO>) menuCheckRoot.lookup("#tableView");
			TextField tfGoods = (TextField) menuCheckRoot.lookup("#tfGoods");
			Button btSearch = (Button) menuCheckRoot.lookup("#btSearch");
			Button btMenuRefresh = (Button) menuCheckRoot.lookup("#btMenuRefresh");
			Button btAdd = (Button) menuCheckRoot.lookup("#btAdd");
			Button btEdit = (Button) menuCheckRoot.lookup("#btEdit");
			Button btEditDelete = (Button) menuCheckRoot.lookup("#btEditDelete");
			Button btBack = (Button) menuCheckRoot.lookup("#btBack");

			tableView.setEditable(false);

			TableColumn columnGoods = new TableColumn("물품");
			columnGoods.setMaxWidth(100);
			columnGoods.setStyle("-fx-alignment: CENTER;");
			columnGoods.setCellValueFactory(new PropertyValueFactory("goodsName"));

			TableColumn columnPrice = new TableColumn("가격");
			columnPrice.setMaxWidth(100);
			columnPrice.setStyle("-fx-alignment: CENTER;");
			columnPrice.setCellValueFactory(new PropertyValueFactory("price"));

			goodsVOList.removeAll(goodsVOList);
			loadTotalGoodsDB();

			tableView.setItems(goodsVOList);
			tableView.getColumns().addAll(columnGoods, columnPrice);

			/*******************
			 * 2019 10 월 18 일 작성자 : 심재현
			 * 
			 * 기능 : 검색버튼
			 * 
			 */
			btSearch.setOnAction(e1 -> {

				ObservableList<GoodsVO> list = FXCollections.observableArrayList();
				GoodsDAO goodsDVO = new GoodsDAO();

				try {

					list = goodsDVO.getCheckGoods(tfGoods.getText(), LoginController.clientId);
					tableView.setItems(list);

				} catch (SQLException sqle) {
					sqle.printStackTrace();
					AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
				}

			}); // end of btSearch

			/**********************
			 * 2019 10 월 18 일 작성자 : 심재현
			 * 
			 * 기능 : 테이블뷰 새로 고침 버튼
			 * 
			 */
			btMenuRefresh.setOnAction(e1 -> {

				goodsVOList.removeAll(goodsVOList);
				loadTotalGoodsDB();

				tableView.setItems(goodsVOList);

			});

			/******************************
			 * 2019 10 월 18 일 작성자 : 심재현
			 * 
			 * 기능 : 메뉴를 등록한다.
			 * 
			 */
			btAdd.setOnAction(e1 -> {

				try {

					Scene addMenuScene = SceneLoader.getInstance().makeMenuAddScene();
					Parent menuAddRoot = addMenuScene.getRoot();
					Stage dialMenuAddStage = new Stage(StageStyle.UTILITY);
					dialMenuAddStage.initModality(Modality.WINDOW_MODAL);
					dialMenuAddStage.initOwner(btMenuCheck.getScene().getWindow());
					dialMenuAddStage.setTitle("메뉴 등록");

					TextField tfAddGoods = (TextField) menuAddRoot.lookup("#tfAddGoods");
					TextField tfAddPrice = (TextField) menuAddRoot.lookup("#tfAddPrice");
					Button btAddGoods = (Button) menuAddRoot.lookup("#btAddGoods");
					Button btAddCancle = (Button) menuAddRoot.lookup("#btAddCancle");

					btAddGoods.setOnAction(e3 -> {

						GoodsDAO goodsDVO = new GoodsDAO();

						GoodsVO insertGoods = new GoodsVO(tfAddGoods.getText(), Integer.parseInt(tfAddPrice.getText()));
						try {
							goodsDVO.insertGoodsDB(LoginController.clientId, insertGoods.getGoodsName(),
									insertGoods.getPrice());
							AlertManager.getInstance().show(AlertInfo.SUCCESS_TASK, null);
						} catch (SQLException sqle) {
							sqle.printStackTrace();
							AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
						}
					});

					btAddCancle.setOnAction(e3 -> {

						dialMenuAddStage.close();

					});

					dialMenuAddStage.setScene(addMenuScene);
					dialMenuAddStage.setResizable(false);
					dialMenuAddStage.show();

				} catch (IOException ioe) {
					ioe.printStackTrace();
					AlertManager.getInstance().show(AlertInfo.ERROR_LOAD_SCENE, null);
				}

			}); // end of btAdd

			/**************************
			 * 2019 10 월 18 일 작성자 : 심재현
			 * 
			 * 기능 : 테이블뷰 선택 시 정보를 저장한다.
			 * 
			 */
			tableView.setOnMousePressed(e1 -> {

				selectMenuEditGoodsVO = tableView.getSelectionModel().getSelectedItem();
				selectMenuEditGoodsVOList = tableView.getSelectionModel().getSelectedItems();

			}); // end of tableView select

			/************************************
			 * 2019 10 월 18일 작성자 : 심재현
			 * 
			 * 기능 : 테이블 뷰에서 선택된 항목을 수정하는 창을 보여준다.
			 * 
			 */
			btEdit.setOnAction(e1 -> {

				try {
					Scene editMenuScene = SceneLoader.getInstance().makeMenuEditScene();
					Parent menuEditRoot = editMenuScene.getRoot();
					Stage dialogEditStage = new Stage(StageStyle.UTILITY);
					dialogEditStage.initModality(Modality.WINDOW_MODAL);
					dialogEditStage.initOwner(btMenuCheck.getScene().getWindow());
					dialogEditStage.setTitle("메뉴 수정");

					TextField tfEditGoods = (TextField) menuEditRoot.lookup("#tfEditGoods");
					TextField tfEditPrice = (TextField) menuEditRoot.lookup("#tfEditPrice");
					Button btEditOk = (Button) menuEditRoot.lookup("#btEditOk");
					Button btEditBack = (Button) menuEditRoot.lookup("#btEditBack");

					editGoods = selectMenuEditGoodsVOList.get(0).getGoodsName();

					tfEditGoods.setPromptText(editGoods);
					tfEditPrice.setPromptText(String.valueOf(selectMenuEditGoodsVOList.get(0).getPrice()));

					/********************************
					 * 2019 10 월 18 일 작성자 : 심재현
					 * 
					 * 기능 : 수정한 내용을 업데이트 한다.
					 * 
					 */
					btEditOk.setOnAction(e2 -> {

						GoodsDAO goodsDAO = new GoodsDAO();
						boolean updateCheck = false;

						try {
							if(!(tfEditGoods.getText().equals("")) && !(tfEditPrice.getText().equals(""))) {
								updateCheck = goodsDAO.updateGoods(tfEditGoods.getText(), 
										Integer.parseInt(tfEditPrice.getText()), selectMenuEditGoodsVO.getNo());
							} else if ((tfEditGoods.getText().equals("")) && !(tfEditPrice.getText().equals(""))) {
								updateCheck = goodsDAO.updateGoods(tfEditGoods.getPromptText(),
										Integer.parseInt(tfEditPrice.getText()), selectMenuEditGoodsVO.getNo());
							} else if (!(tfEditGoods.getText().equals("")) && (tfEditPrice.getText().equals(""))) {
								updateCheck = goodsDAO.updateGoods(tfEditGoods.getText(),
										Integer.parseInt(tfEditPrice.getPromptText()), selectMenuEditGoodsVO.getNo());
							} else {
								AlertManager.getInstance().show(AlertInfo.ERROR_TASK_EMPTY, null);
							}

							if (updateCheck) {
								AlertManager.getInstance().show(AlertInfo.SUCCESS_TASK, null);
							} else {
								AlertManager.getInstance().show(AlertInfo.ERROR_TASK, null);
							}
							updateCheck = false;

						} catch (NumberFormatException | SQLException e3) {
							// TODO Auto-generated catch block
							e3.printStackTrace();
						}

					}); // end of btEditOk

					/********************************************
					 * 2019 10 월 18 일 작성자 : 심재현
					 * 
					 * 기능 : 수정 창을 닫는다.
					 * 
					 */
					btEditBack.setOnAction(e2 -> {
						goodsVOList.removeAll(goodsVOList);
						loadTotalGoodsDB();

						tableView.setItems(goodsVOList);

						dialogEditStage.close();
					}); // end of btEditBack

					dialogEditStage.setScene(editMenuScene);
					dialogEditStage.setResizable(false);
					dialogEditStage.show();

				} catch (IOException ioe) {
					ioe.printStackTrace();
					AlertManager.getInstance().show(AlertInfo.ERROR_LOAD_SCENE, null);
				}

			}); // end of btEdit

			/***********************
			 * 2019 10 월 18 일 작성자 : 심재현
			 * 
			 * 기능 : 테이블 뷰에서 선택 된 항목을 테이블 뷰에서 삭제한다.
			 * 
			 */
			btEditDelete.setOnAction(e1 -> {

				GoodsDAO goodsDVO = new GoodsDAO();

				AlertManager.getInstance().show(AlertType.CONFIRMATION, "항목 삭제", "선택한 항목을 삭제하시겠습니까?", buttonType -> {
					if (buttonType != ButtonType.OK) {
						return;
					}

					try {
						boolean deleted = goodsDVO.deleteGoods(selectMenuEditGoodsVO.getNo());
						if (deleted) {
							AlertManager.getInstance().show(AlertInfo.SUCCESS_TASK, null);

						} else {
							AlertManager.getInstance().show(AlertInfo.ERROR_TASK, null);
						}

					} catch (SQLException sqle) {
						sqle.printStackTrace();
						AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
					}
				});
			}); // end of btEditDelete
			/*****************
			 * 2019 10 월 18 일 작성자 : 심재현
			 * 
			 * 기능 : 메뉴 확인 창을 닫는다.
			 * 
			 */
			btBack.setOnAction(e1 -> {
				comboBoxSettion();
				dialogStage.close();

			}); // end of btBack

			dialogStage.setScene(scene);
			dialogStage.setResizable(false);
			dialogStage.show();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			AlertManager.getInstance().show(AlertInfo.ERROR_LOAD_SCENE, null);
		}
	} // end of handlerButtonMenuCheck

	/****************************
	 * 기능 : 테이블 뷰의 표시된 금액을 전부 합해서 보여준다. 2019 10 월 19
	 * 
	 * 작성자 : 심재현
	 * 
	 */
	private void handlerButtonSaleTotalPrice(ActionEvent e) {
		
		int saleTotalPrice = 0;
		
		for(int i = 0; i < loadSaleVOList.size(); i++) {
			saleTotalPrice = loadSaleVOList.get(i).getTotal() + saleTotalPrice;
		}
		
		tfTotalSalePrice.setText(String.valueOf(saleTotalPrice));

	} // end of handlerButtonSaleTotalPrice

	private void handlerButtonRemobeTableAction(ActionEvent e) {

		ObservableList<SaleVO> removeList = FXCollections.observableArrayList();
		ObservableList<String> removeComboList = FXCollections.observableArrayList();
		tableView.setItems(removeList);

		loadSaleVOList.removeAll(loadSaleVOList);
		addSaleVOList.removeAll(addSaleVOList);
		afterDeleteSaleVOList.removeAll(afterDeleteSaleVOList);

		tfCount.setText(null);
		tfComents.setText(null);
		tfComents.setPromptText("비고 (10글자 이내)");
		tfCount.setPromptText("개수");
		comboBox.setItems(removeComboList);
		datePicker.setValue(null);
		comboBoxSettion();
		barChartSetting(removeList);

		buttonInitSetting(false, true, true, true, false, true);

	}

	private void buttonInitSetting(boolean btCheck, boolean btSaleAdd, boolean btSaleDelete, boolean btSelectRowDelete,
			boolean btMenuCheck, boolean btSaleTotalPrice) {

		this.btCheck.setDisable(btCheck);
		this.btSaleAdd.setDisable(btSaleAdd);
		this.btSaleDelete.setDisable(btSaleDelete);
		this.btSelectRowDelete.setDisable(btSelectRowDelete);
		this.btMenuCheck.setDisable(btMenuCheck);
		this.btSaleTotalPrice.setDisable(btSaleTotalPrice);

	}

	private void loadTotalGoodsDB() {
		try {
			GoodsDAO goodsDVO = new GoodsDAO();
			ArrayList<GoodsVO> fetchedList = goodsDVO.getGoodsTotal();
			ArrayList<GoodsVO> goodsList = new ArrayList<GoodsVO>();
			for (int i = 0; i < fetchedList.size(); i++) {
				GoodsVO goods = fetchedList.get(i);
				goodsVOList.add(goods);
				goodsList.add(goods);
			}

		} catch (SQLException sqle) {
			sqle.printStackTrace();
			AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
		}

	} // end of loadTotalGoodsDB

	/************************
	 * 기능 : 메인창의 테이블 뷰를 세팅한다. 2019 10 월 18 일 작성자 : 심재현
	 * 
	 */

	private void tableViewSetting() {

		tableView.setEditable(false);

		TableColumn columnDate = new TableColumn("날짜");
		columnDate.setMaxWidth(150);
		columnDate.setStyle("-fx-alignment: CENTER;");
		columnDate.setCellValueFactory(new PropertyValueFactory("date"));

		TableColumn columnGoods = new TableColumn("상품");
		columnGoods.setMaxWidth(160);
		columnGoods.setStyle("-fx-alignment: CENTER;");
		columnGoods.setCellValueFactory(new PropertyValueFactory("goods"));

		TableColumn columnPrice = new TableColumn("가격");
		columnPrice.setMaxWidth(80);
		columnPrice.setStyle("-fx-alignment: CENTER;");
		columnPrice.setCellValueFactory(new PropertyValueFactory("price"));

		TableColumn columnCount = new TableColumn("개수");
		columnCount.setMaxWidth(60);
		columnCount.setStyle("-fx-alignment: CENTER;");
		columnCount.setCellValueFactory(new PropertyValueFactory("count"));

		TableColumn columnTotal = new TableColumn("판매 금액");
		columnTotal.setMaxWidth(100);
		columnTotal.setStyle("-fx-alignment: CENTER;");
		columnTotal.setCellValueFactory(new PropertyValueFactory("total"));

		TableColumn columnComents = new TableColumn("비고");
		columnComents.setMaxWidth(200);
		columnComents.setStyle("-fx-alignment: CENTER;");
		columnComents.setCellValueFactory(new PropertyValueFactory("coments"));

		tableView.getColumns().addAll(columnDate, columnGoods, columnPrice, columnCount, columnTotal, columnComents);

	} // end of tableViewSetting

	/************************
	 * 
	 * 기능 : 바 차트에 내용 표시 2019 10 월 19 일 수정 : 2019 10 월 20 일 심재현 수정 내용 : 해당
	 * ObservableList 를 받아서 차트를 보여준다.
	 * 
	 * 작성자 : 심재현
	 * 
	 * @param dateSelectList2
	 * 
	 */
	private void barChartSetting(ObservableList<SaleVO> observableList) {

		try {

			ObservableList<XYChart.Data<String, Integer>> barChartList = FXCollections.observableArrayList();
			XYChart.Series<String, Integer> series = new XYChart.Series<>();

			barChart.setTitle(localDateStr + "매출");

			if (!(barChartList.equals(null))) {

				barChartList.removeAll(barChartList);

			}

			observableList = observableList.sorted();

			SaleDAO saleDAO = new SaleDAO();
			ObservableList<SaleVO> barChartData = saleDAO.barChartData(observableList.get(0).getDate());
			
			for (int i = 0; i < barChartData.size(); i++) {

				barChartList.add(new XYChart.Data<String, Integer>(barChartData.get(i).getGoods(),
						(barChartData.get(i).getTotal())));

			}

			series.getData().clear();
			barChart.getData().clear();

			series.setName(localDateStr);
			series.setData(barChartList);
			barChart.getData().add(series);

		} catch (SQLException e) {

		}

	} // end of barChartSetting

	/**************************************
	 * 태성
	 */
	private void iMbarChartSetting() {

		try {

			ObservableList<XYChart.Data<String, Integer>> barChartList = FXCollections.observableArrayList();
			XYChart.Series<String, Integer> series = new XYChart.Series<>();

			stackedBarChart.setTitle(localString + "재고 현황");

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
			stackedBarChart.getData().clear();
			
			series.setName(localString);
			series.setData(barChartList);
			stackedBarChart.getData().add(series);
			
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
				iMtableView.setItems(data);
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
			
			iMtableView.setItems(loadToDateList);
			
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
				iMtableView.setItems(data);
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

			iMtableView.setItems(data);
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

		iMtableView.setItems(data);
		iMtableView.getColumns().addAll(colNo, colDate, colName, colPay, colEa, colRemakers);

	}
}
