package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.GoodsDAO;
import model.GoodsVO;
import model.SaleDAO;
import model.SaleVO;
import util.AlertManager;
import util.AlertManager.AlertInfo;
import util.SceneLoader;

public class MainController implements Initializable {

	@FXML
	private TextField tfGoodsName;
	@FXML
	private TextField tfTotalSalePrice;
	@FXML
	private Button btSearch;
	@FXML
	private Button btSaleAdd;
	@FXML
	private Button btSaleDelete;
	@FXML
	private Button btMenuCheck;
	@FXML
	private Button btSaleTotalPrice;
	@FXML
	private DatePicker datePicker;
	@FXML
	private TableView<SaleVO> tableView;
	@FXML
	private BarChart<String, Integer> barChart;

	/********************
	 * 기능 : 필요한 saleVO 모델의 변수 2019 10 월 18 일
	 * 
	 * 작성자 : 심재현
	 * 
	 */
	private ObservableList<SaleVO> saleVOList = FXCollections.observableArrayList();
	private ObservableList<SaleVO> addSaleVOList = FXCollections.observableArrayList();
	private ObservableList<SaleVO> selectSaleVOList = FXCollections.observableArrayList();
	private ObservableList<SaleVO> dateSelectList = FXCollections.observableArrayList();
	private int selectSaleIndex = 0;
	private ArrayList<SaleVO> selectSaleVO = new ArrayList<SaleVO>();
	private ArrayList<SaleVO> searchDateSaleVO = new ArrayList<SaleVO>();
	private SaleVO selectSale = null;
	private SaleDAO saleDAO = null;
	private SaleVO saveSaleVO = new SaleVO();
	private String saveGoodsString = null;

	/*******************************
	 * 기능 : 필요한 goodsVO의 변수들 2019 10 월 18 일
	 * 
	 * 작성자 : 심재현
	 * 
	 */
	private ArrayList<GoodsVO> goodsList = new ArrayList<GoodsVO>();
	private ObservableList<GoodsVO> goodsVOList = FXCollections.observableArrayList();
	private ObservableList<String> goodsNameList = FXCollections.observableArrayList();;
	private GoodsDAO goodsDVO = null;;
	private String editGoods = null;;
	private ObservableList<GoodsVO> selectMenuEditGoodsVOList = FXCollections.observableArrayList();
	private GoodsVO selectMenuEditGoodsVO = null;

	/**********************************
	 * 기능 : 필요한 그 외의 변수들 2019 10 월 18 일
	 * 
	 * 작성자 : 심재현
	 * 
	 */
	private LocalDate localDate = null;
	private int savePrice = 0;
	private int saveTotalPrice = 0;
	private String localDateStr = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		tableViewSetting();

		// loadTotalSaleDB();

		loadTotalGoodsDB();

		btSearch.setOnAction(e -> handlerButtonSaleSearchAction(e));

		btSaleAdd.setOnAction(e -> handlerButtonSaleAddAction(e));

		btSaleDelete.setOnAction(e -> handlerButtonSaleDelete(e));

		btMenuCheck.setOnAction(e -> handlerButtonMenuCheck(e));

		datePicker.setOnAction(e -> handlerDatePicker(e));

		btSaleTotalPrice.setOnAction(e -> handlerButtonSaleTotalPrice(e));

		tableView.setOnMousePressed(e1 -> {

			selectSaleVOList = tableView.getSelectionModel().getSelectedItems();
			selectSale = tableView.getSelectionModel().getSelectedItem();
			selectSaleIndex = tableView.getSelectionModel().getSelectedIndex();

		}); // end of tableView select

	} // end of initialize

	private void handlerButtonSaleSearchAction(ActionEvent e) {

		try {

			String searchGoods = tfGoodsName.getText().trim();
			localDate = datePicker.getValue();
			String localDateStr = localDate.getYear() + "-" + localDate.getMonthValue() + "-"
					+ localDate.getDayOfMonth();

			ArrayList<SaleVO> list = new ArrayList<SaleVO>();

			saleDAO = new SaleDAO();
			list = saleDAO.searchGoodsVO(searchGoods, localDateStr);

			saleVOList = FXCollections.observableArrayList(list);
			tableView.setItems(saleVOList);

		} catch (Exception e1) {

			e1.printStackTrace();
			AlertManager.getInstance().show(AlertInfo.ERROR_TASK, null);
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

			localDateStr = localDate.getYear() + "-" + localDate.getMonthValue() + "-" + localDate.getDayOfMonth();

			saleDAO = new SaleDAO();

			searchDateSaleVO = saleDAO.getListToDate(localDateStr);

			dateSelectList = FXCollections.observableArrayList(searchDateSaleVO);

			tableView.setItems(dateSelectList);

			barChartSetting(dateSelectList);

		} catch (Exception e1) {

			e1.printStackTrace();
			AlertManager.getInstance().show(AlertInfo.ERROR_TASK, null);

		}
	} // end of handlerDatePicker

	/**************************
	 * 기능 : 등록 버튼을 누르면 등록 창을 연다. 2019 10월 18 일
	 * 
	 * 작성자 :심재현
	 */
	private void handlerButtonSaleAddAction(ActionEvent e) {

		try {

			Scene scene = SceneLoader.getInstance().makeSaleAddScene();
			Parent saleAddRoot = scene.getRoot();
			Stage saleAddStage = new Stage(StageStyle.UTILITY);
			saleAddStage.initModality(Modality.WINDOW_MODAL);
			saleAddStage.initOwner(btSaleAdd.getScene().getWindow());
			saleAddStage.setResizable(false);
			saleAddStage.setTitle("매출 등록");

			DatePicker dpSaleDate = (DatePicker) saleAddRoot.lookup("#dpSaleDate");
			ComboBox<String> cbGoodsList = (ComboBox<String>) saleAddRoot.lookup("#cbGoodsList");
			TextField tfSalePrice = (TextField) saleAddRoot.lookup("#tfSalePrice");
			TextField tfSaleCount = (TextField) saleAddRoot.lookup("#tfSaleCount");
			TextField tfSaleTotal = (TextField) saleAddRoot.lookup("#tfSaleTotal");
			TextField tfSaleComents = (TextField) saleAddRoot.lookup("#tfSaleComents");
			TableView<SaleVO> tableView = (TableView<SaleVO>) saleAddRoot.lookup("#tableView");
			Button btSaleAdd = (Button) saleAddRoot.lookup("#btSaleAdd");
			Button btSaleDelete = (Button) saleAddRoot.lookup("#btSaleDelete");
			Button btSaleBack = (Button) saleAddRoot.lookup("#btSaleBack");

			goodsDVO = new GoodsDAO();
			goodsList = goodsDVO.getGoodsTotal();

			ArrayList<String> saveGoodsName = new ArrayList<String>();

			for (int i = 0; i < goodsList.size(); i++) {

				saveGoodsName.add(goodsList.get(i).getGoods());

			}

			goodsNameList = FXCollections.observableArrayList(saveGoodsName);

			cbGoodsList.setItems(goodsNameList);

			/********************
			 * 2019 10 월 18일 cbGoodsList.setOnAction 이벤트 메소드 작성자 : 심재현
			 * 
			 * 기능 : comboBox의 값을 얻어서 해당 품목의 가격을 찾아 온다.
			 */
			cbGoodsList.setOnAction(e2 -> {

				saveGoodsString = cbGoodsList.getSelectionModel().getSelectedItem();

				for (int i = 0; i < goodsList.size(); i++) {

					if (goodsList.get(i).getGoods().equals(saveGoodsString)) {

						savePrice = goodsList.get(i).getPrice();
						tfSalePrice.setPromptText(String.valueOf(savePrice));
						break;

					}

				}

			}); // end of cbGoodsList

			/*********************
			 * 2019 10 월 18일 작성자 : 심재현
			 * 
			 * 기능 : 테이블 뷰 컬럼 생성 후 컬럼과 값 세팅
			 */
			tableView.setEditable(true);

			TableColumn columnDate = new TableColumn("날짜");
			columnDate.setMaxWidth(110);
			columnDate.setStyle("-fx-alignment: CENTER;");
			columnDate.setCellValueFactory(new PropertyValueFactory("date"));

			TableColumn columnGoods = new TableColumn("물품");
			columnGoods.setMaxWidth(110);
			columnGoods.setStyle("-fx-alignment: CENTER;");
			columnGoods.setCellValueFactory(new PropertyValueFactory("goods"));

			TableColumn columnPrice = new TableColumn("가격");
			columnPrice.setMaxWidth(110);
			columnPrice.setStyle("-fx-alignment: CENTER;");
			columnPrice.setCellValueFactory(new PropertyValueFactory("price"));

			TableColumn columnCount = new TableColumn("개수");
			columnCount.setMaxWidth(110);
			columnCount.setStyle("-fx-alignment: CENTER;");
			columnCount.setCellValueFactory(new PropertyValueFactory("count"));

			TableColumn columnTotal = new TableColumn("총액");
			columnTotal.setMaxWidth(110);
			columnTotal.setStyle("-fx-alignment: CENTER;");
			columnTotal.setCellValueFactory(new PropertyValueFactory("total"));

			TableColumn columnComents = new TableColumn("비고");
			columnComents.setMaxWidth(110);
			columnComents.setStyle("-fx-alignment: CENTER;");
			columnComents.setCellValueFactory(new PropertyValueFactory("coments"));

			if (saleVOList != null) {
				saleVOList.removeAll(saleVOList);

			}

			tableView.setItems(saleVOList);

			tableView.getColumns().addAll(columnDate, columnGoods, columnPrice, columnCount, columnTotal,
					columnComents);

			/*************************
			 * 2019 10 월 18 일 작성자 : 심재현 수정 20일 심재현
			 * 
			 * 기능 : 비고 텍스트필드에서 키 이벤트 메소드를 걸어 놓음
			 * 
			 * 수정 내용 : 중복된 물품 이름이 있을 시 합쳐서 테이블 뷰에 하나만 보이게 함
			 * 
			 */
			tfSaleComents.setOnKeyPressed(new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent ke) {

					KeyCode keyCode = ke.getCode();

					if (keyCode.equals(KeyCode.ENTER)) {

						boolean duplicateGoodsName = false;

						SaleVO notDuplicateSaleVO = null;
						SaleVO pastSaleVO = null;

						int notDuplicateSaleVOTotal = 0;
						int notDuplicateSaleVOCount = 0;
						int count = Integer.parseInt(tfSaleCount.getText().trim());
						int price = Integer.parseInt(tfSalePrice.getPromptText());
						System.out.println(count);
						System.out.println(price);
						saveTotalPrice = count * price;
						System.out.println(saveTotalPrice);

						tfSaleTotal.setText(String.valueOf(saveTotalPrice));
						tfSaleTotal.setEditable(false);

						if ((tfSaleTotal != null) && (dpSaleDate != null)) {

							saveSaleVO = new SaleVO(dpSaleDate.getValue().toString(), saveGoodsString,
									Integer.parseInt(tfSalePrice.getPromptText()),
									Integer.parseInt(tfSaleCount.getText().trim()), saveTotalPrice,
									tfSaleComents.getText());

							System.out.println(saveTotalPrice);
							System.out.println(saveSaleVO.getCount());

							if (saleVOList.isEmpty()) {

								saleVOList.add(saveSaleVO);

							} else {

								for (int i = 0; i < saleVOList.size(); i++) {

									if (saleVOList.get(i).getGoods().equals(saveSaleVO.getGoods())) {

										duplicateGoodsName = false;
										notDuplicateSaleVOCount = saleVOList.get(i).getCount() + saveSaleVO.getCount();
										notDuplicateSaleVOTotal = saleVOList.get(i).getTotal() + saveSaleVO.getTotal();
										pastSaleVO = saleVOList.get(i);
										break;

									} else {

										duplicateGoodsName = true;

									}

								}

								if (duplicateGoodsName) {

									saleVOList.add(saveSaleVO);

									duplicateGoodsName = false;

								} else {

									saleVOList.remove(pastSaleVO);
									saveSaleVO.setCount(notDuplicateSaleVOCount);
									saveSaleVO.setTotal(notDuplicateSaleVOTotal);

									saleVOList.add(saveSaleVO);

								}
							}

							tableView.setItems(saleVOList);

						}
					}
				}

			}); // end of tfSaleCount key Event

			/*******************
			 * 2019 10 월 18일 작성자 : 심재현
			 * 
			 * 기능 : 등록 버튼을 누르면 테이블에 써있는 값들을 데이터베이스에 등록한다.
			 * 
			 */
			btSaleAdd.setOnAction(e2 -> {

				try {

					saleDAO = new SaleDAO();

					for (int i = 0; i < saleVOList.size(); i++) {

						saleDAO.insertSaleDB(saleVOList.get(i));

					}

					AlertManager.getInstance().show(AlertInfo.SUCCESS_TASK, buttonType -> {
						saleVOList.removeAll(saleVOList);
						tableView.setItems(selectSaleVOList);
					});

				} catch (Exception e3) {
					e3.printStackTrace();
					AlertManager.getInstance().show(AlertInfo.ERROR_TASK, null);
				}

			}); // end of btSaleAdd

			/***********************
			 * 2019 10 월 18 일 작성자 : 심재현
			 * 
			 * 기능 : 테이블 뷰를 클릭한 곳의 값을 저장한다. (인덱스 값은 포함하지 않았다.)
			 * 
			 */
			tableView.setOnMousePressed(e3 -> {

				selectSale = tableView.getSelectionModel().getSelectedItem();
				selectSaleVOList = tableView.getSelectionModel().getSelectedItems();

			}); // end of tableView select

			/***********************
			 * 2019 10 월 18 일 작성자 : 심재현
			 * 
			 * 기능 : 삭제 버튼을 누르면 항목이 삭제 된다. (테이블뷰에서 선택한 항목을 삭제함)
			 * 
			 */
			btSaleDelete.setOnAction(e2 -> {

				if (selectSaleVO == null) {
					AlertManager.getInstance().show(AlertInfo.ERROR_TASK, null);
					return;
				}

				try {

					saleDAO = new SaleDAO();

					AlertManager.getInstance().show(AlertType.CONFIRMATION, "항목 삭제", "선택한 항목을 삭제하시겠습니까?",
							buttonType -> {
								if (buttonType != ButtonType.OK) {
									return;
								}

								addSaleVOList.addAll(saleVOList);
								saleVOList.remove(selectSale);
								tableView.setItems(saleVOList);

							});

				} catch (Exception e3) {
					e3.printStackTrace();
					AlertManager.getInstance().show(AlertInfo.ERROR_TASK, null);
				}

			}); // end of btSaleDelete

			/********************
			 * 2019 10 월 18일 작성자 : 심재현
			 * 
			 * 기능 : 버튼을 누르면 값을 초기화 하고 창을 닫는다.
			 * 
			 */
			btSaleBack.setOnAction(e2 -> {

				addSaleVOList.addAll(saleVOList);// 있어야하는지 기억안남...
				saleVOList.removeAll(saleVOList);

				saleAddStage.close();

			});

			saleAddStage.setScene(scene);
			saleAddStage.show();

		} catch (Exception e2) {

			e2.printStackTrace();
			AlertManager.getInstance().show(AlertInfo.ERROR_UNKNOWN, null);

		} // end of handlerButtonSaleAddAction

	} // end of handlerButtonSaleAddAction

	/************************************
	 * 기능 : 버튼을 누르면 항목을 지운다. 2019 10 월 18 일
	 * 
	 * 작성자 : 심재현
	 * 
	 */
	private void handlerButtonSaleDelete(ActionEvent e) {

		if (selectSale == null) {
			AlertManager.getInstance().show(AlertType.ERROR, "에러", "항목을 선택해주세요.", null);
			return;
		}

		saleDAO = new SaleDAO();

		AlertManager.getInstance().show(AlertType.CONFIRMATION, "항목 삭제", "선택한 항목을 삭제하시겠습니까?", buttonType -> {
			if (buttonType != ButtonType.OK) {
				return;
			}

			int i = saleDAO.deleteSale(selectSale);

			if (i == 1) {

				AlertManager.getInstance().show(AlertInfo.SUCCESS_TASK, null);

				// 삭제후 날짜 값은 선택했던 값 그대로를 유지 해야 하므로 현재의 값을 얻어와 다시 테이블뷰를 세팅한다.
				localDate = datePicker.getValue();
				String localDateStr = localDate.getYear() + "-" + localDate.getMonthValue() + "-"
						+ localDate.getDayOfMonth();

				saleDAO = new SaleDAO();
				searchDateSaleVO = saleDAO.getListToDate(localDateStr);
				saleVOList.removeAll(saleVOList);
				ObservableList<SaleVO> list2 = FXCollections.observableArrayList(searchDateSaleVO);
				tableView.setItems(list2);

				barChartSetting(list2);

			} else {
				AlertManager.getInstance().show(AlertInfo.ERROR_TASK, null);

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
		columnGoods.setCellValueFactory(new PropertyValueFactory("goods"));

		TableColumn columnPrice = new TableColumn("가격");
		columnPrice.setMaxWidth(100);
		columnPrice.setStyle("-fx-alignment: CENTER;");
		columnPrice.setCellValueFactory(new PropertyValueFactory("price"));

		goodsVOList.removeAll(goodsVOList);
		loadTotalGoodsDB();

		tableView.setItems(goodsVOList);
		tableView.getColumns().addAll(columnGoods, columnPrice);

		/*******************
		 * 기능 : 검색버튼 2019 10 월 18 일
		 * 
		 * 작성자 : 심재현
		 */
		btSearch.setOnAction(e1 -> {

			ObservableList<GoodsVO> list = FXCollections.observableArrayList();
			goodsDVO = new GoodsDAO();

			try {

				list = goodsDVO.getCheckGoods(tfGoods.getText());
				tableView.setItems(list);

			} catch (Exception e2) {
				AlertManager.getInstance().show(AlertInfo.ERROR_UNKNOWN, null);
				e2.printStackTrace();

			}

		}); // end of btSearch

		/**********************
		 * 기능 : 테이블뷰 새로 고침 버튼 2019 10 월 18 일
		 * 
		 * 작성자 : 심재현
		 */
		btMenuRefresh.setOnAction(e1 -> {

			goodsVOList.removeAll(goodsVOList);
			loadTotalGoodsDB();

			tableView.setItems(goodsVOList);

		});

		/******************************
		 * 기능 : 메뉴를 등록한다. 2019 10 월 18 일
		 * 
		 * 작성자 : 심재현
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

					goodsDVO = new GoodsDAO();

					GoodsVO insertGoods = new GoodsVO(tfAddGoods.getText(), Integer.parseInt(tfAddPrice.getText()));

					goodsDVO.insertGoodsDB(insertGoods);

					AlertManager.getInstance().show(AlertInfo.SUCCESS_TASK, null);

				});

				btAddCancle.setOnAction(e3 -> {

					dialMenuAddStage.close();

				});

				dialMenuAddStage.setScene(addMenuScene);
				dialMenuAddStage.setResizable(false);
				dialMenuAddStage.show();

			} catch (Exception e2) {
				AlertManager.getInstance().show(AlertInfo.ERROR_UNKNOWN, null);
				e2.printStackTrace();

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

				editGoods = selectMenuEditGoodsVOList.get(0).getGoods();

				tfEditGoods.setPromptText(editGoods);
				tfEditGoods.setEditable(false);
				tfEditPrice.setPromptText(String.valueOf(selectMenuEditGoodsVOList.get(0).getPrice()));

				/********************************
				 * 2019 10 월 18 일 작성자 : 심재현
				 * 
				 * 기능 : 수정한 내용을 업데이트 한다.
				 * 
				 */
				btEditOk.setOnAction(e2 -> {
					try {
						goodsDVO = new GoodsDAO();

						if (tfEditGoods.getPromptText() == null) {

							goodsDVO.updateGoods(selectMenuEditGoodsVO, tfEditGoods.getText(),
									Integer.parseInt(tfEditPrice.getText()));

						} else {

							goodsDVO.updateOnlyPrice(selectMenuEditGoodsVO, Integer.parseInt(tfEditPrice.getText()));

						}
					} catch (Exception e3) {
						e3.printStackTrace();
						AlertManager.getInstance().show(AlertInfo.ERROR_TASK, null);
					}

				}); // end of btEditOk

				/********************************************
				 * 2019 10 월 18 일 작성자 : 심재현
				 * 
				 * 기능 : 수정 창을 닫는다.
				 * 
				 */
				btEditBack.setOnAction(e2 -> {
					try {
						goodsVOList.removeAll(goodsVOList);
						loadTotalGoodsDB();

						tableView.setItems(goodsVOList);

						dialogEditStage.close();
					} catch (Exception e3) {
						e3.printStackTrace();
						AlertManager.getInstance().show(AlertInfo.ERROR_TASK, null);
					}
				}); // end of btEditBack

				dialogEditStage.setScene(editMenuScene);
				dialogEditStage.setResizable(false);
				dialogEditStage.show();

			} catch (Exception e3) {

				AlertManager.getInstance().show(AlertInfo.ERROR_UNKNOWN, null);
				e3.printStackTrace();

			}

		}); // end of btEdit

		/***********************
		 * 2019 10 월 18 일 작성자 : 심재현
		 * 
		 * 기능 : 테이블 뷰에서 선택 된 항목을 테이블 뷰에서 삭제한다.
		 * 
		 */
		btEditDelete.setOnAction(e1 -> {

			goodsDVO = new GoodsDAO();

			AlertManager.getInstance().show(AlertType.CONFIRMATION, "항목 삭제", "선택한 항목을 삭제하시겠습니까?", buttonType -> {
				if (buttonType != ButtonType.OK) {
					return;
				}

				try {
					int i = goodsDVO.deleteGoods(selectMenuEditGoodsVO.getGoods());
					System.out.println(selectMenuEditGoodsVO.getGoods());
					if (i == 1) {
						AlertManager.getInstance().show(AlertInfo.SUCCESS_TASK, null);

					} else {
						AlertManager.getInstance().show(AlertInfo.ERROR_TASK, null);
					}

				} catch (Exception e2) {
					AlertManager.getInstance().show(AlertInfo.ERROR_UNKNOWN, null);
					e2.printStackTrace();
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

			dialogStage.close();

		}); // end of btBack

		dialogStage.setScene(scene);
		dialogStage.setResizable(false);
		dialogStage.show();

	} // end of handlerButtonMenuCheck

	/****************************
	 * 기능 : 테이블 뷰의 표시된 금액을 전부 합해서 보여준다. 2019 10 월 19
	 * 
	 * 작성자 : 심재현
	 * 
	 */
	private void handlerButtonSaleTotalPrice(ActionEvent e) {

		int saleTotalPrice = 0;

		for (int i = 0; i < dateSelectList.size(); i++) {

			saleTotalPrice = dateSelectList.get(i).getTotal() + saleTotalPrice;

		}

		tfTotalSalePrice.setText(String.valueOf(saleTotalPrice));

	} // end of handlerButtonSaleTotalPrice

	/************************************
	 * 기능 : 데이터 베이스에 저장된 goodsVO 정보를 모두 불러온다. 2019 10 월 18 일
	 * 
	 * 작성자 : 심재현
	 * 
	 */

	private void loadTotalGoodsDB() {

		ArrayList<GoodsVO> list = null;
		GoodsVO goodsVO = null;
		GoodsDAO goodsDVO = new GoodsDAO();
		list = goodsDVO.getGoodsTotal();

		if (list == null) {
			AlertManager.getInstance().show(AlertInfo.ERROR_UNKNOWN, null);
			return;

		}

		for (int i = 0; i < list.size(); i++) {

			goodsVO = list.get(i);
			goodsVOList.add(goodsVO);
			goodsList.add(goodsVO);

		}

	} // end of loadTotalGoodsDB

	/************************
	 * 기능 : 메인창의 테이블 뷰를 세팅한다. 2019 10 월 18 일 작성자 : 심재현
	 * 
	 */

	private void tableViewSetting() {

		tableView.setEditable(true);

		TableColumn columnDate = new TableColumn("날짜");
		columnDate.setMaxWidth(110);
		columnDate.setStyle("-fx-alignment: CENTER;");
		columnDate.setCellValueFactory(new PropertyValueFactory("date"));

		TableColumn columnGoods = new TableColumn("상품");
		columnGoods.setMaxWidth(70);
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

		ObservableList<XYChart.Data<String, Integer>> barChartList = FXCollections.observableArrayList();
		XYChart.Series<String, Integer> series = new XYChart.Series<>();

		barChart.setTitle(localDateStr + "매출");

		if (!(barChartList.equals(null))) {

			barChartList.removeAll(barChartList);

		}

		observableList = observableList.sorted();

		for (int i = 0; i < observableList.size(); i++) {

			barChartList.add(new XYChart.Data<String, Integer>(dateSelectList.get(i).getGoods(),
					(dateSelectList.get(i).getTotal())));

		}

		series.getData().clear();
		barChart.getData().clear();

		series.setName(localDateStr);
		series.setData(barChartList);
		barChart.getData().add(series);

		System.out.println(observableList.toString());

	} // end of barChartSetting

}
