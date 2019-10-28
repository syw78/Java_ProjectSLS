package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import controller.LoginController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DBUtil;

public class GoodsDAO {
	
	public void insertGoodsDB(String client, String goodsName, int price) throws SQLException {
		String dml = "insert into goodsTBL (client, goodsName, price) values (?,?,?)";

		try (Connection connection = DBUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(dml);) {
			statement.setString(1, client);
			statement.setString(2, goodsName);
			statement.setInt(3, price);
			statement.execute();
		}
	} // end of insertGoodsDB
	
	public ArrayList<GoodsVO> getGoodsTotal() throws SQLException {

		ArrayList<GoodsVO> goodsList = new ArrayList<GoodsVO>();

		String dml = "select * from goodsTBL where client = ?";
		
		try (Connection connection = DBUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(dml);) {
			statement.setString(1, LoginController.clientId);
			try(ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					GoodsVO goods = new GoodsVO(resultSet.getInt(1),
							resultSet.getString(2),
							resultSet.getString(3), 
							resultSet.getInt(4));
					goodsList.add(goods);
				}
			}
		}
		
		return goodsList;
	} // end of getGoodsTotal
	
	public boolean deleteGoods(int no) throws SQLException {

		String dml = "delete from goodsTBL where no = ?";

		try (Connection connection = DBUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(dml);) {
			statement.setInt(1, no);
			return statement.executeUpdate() > 0;
		}
	} // end of deleteGoods
	
	public boolean updateGoods(String goodsName, int price, int no) throws SQLException {

		String dml = "update goodsTBL set goodsName = ?, price = ? where no = ?";
		
		try (Connection connection = DBUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(dml);) {
			statement.setString(1, goodsName);
			statement.setInt(2, price);
			statement.setInt(3, no);
			
			return statement.executeUpdate() > 0;
		}
	} // end of updateGoods

	public boolean updateOnlyPrice(int no, int price) throws SQLException {

		String dml = "update goodsTBL set price = ? where no = ?";

		try (Connection connection = DBUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(dml);) {
			statement.setInt(1, price);
			statement.setInt(2, price);
			
			return statement.executeUpdate() > 0;
		}
	} // end of updateOnlyPrice

	public ObservableList<GoodsVO> getCheckGoods(String goodsName, String client) throws SQLException {

		String dml = "select * from goodsTBL where goodsName like ? and client = ?";
		
		ObservableList<GoodsVO> goodsList = FXCollections.observableArrayList();
		
		try (Connection connection = DBUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(dml);) {
			String likeGoods = "%" + goodsName + "%";
			statement.setString(1, likeGoods);
			statement.setString(2, client);
			
			try (ResultSet resultSet = statement.executeQuery();) {
				while (resultSet.next()) {
					GoodsVO aGoods = new GoodsVO(resultSet.getString(2), resultSet.getString(3), 
							resultSet.getInt(4));
					goodsList.add(aGoods);
				}
			}
		}
		return goodsList;
	} // end of getCheckGoods
	
	public GoodsVO getGoodsInfomation(String goodsName, String client) throws SQLException {
		
		String dml = "select * from goodsTBL where goodsName = ? and client = ?";
		GoodsVO returnGoodsVO = null;
		
		try(Connection connection = DBUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(dml);) {
			statement.setString(1, goodsName);
			statement.setString(2, client);
			
			try(ResultSet resultSet = statement.executeQuery();) {
				while(resultSet.next()) {
					returnGoodsVO = new GoodsVO(resultSet.getString(2), 
							resultSet.getString(3),
							resultSet.getInt(4));
				}
			}
			
			
		}
		return returnGoodsVO;
	} // end of getGoodsInfomation
	
} // end of GoodsDAO
