package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DBUtil;

public class GoodsDAO {

	public void insertGoodsDB(GoodsVO goodsVO) throws SQLException {
		String dml = "insert into goodsTBL (goods, price) values (?,?)";

		try (Connection connection = DBUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(dml);) {
			statement.setString(1, goodsVO.getGoods());
			statement.setInt(2, goodsVO.getPrice());
			statement.execute();
		}
	}
	
	public ArrayList<GoodsVO> getGoodsTotal() throws SQLException {

		ArrayList<GoodsVO> goodsList = new ArrayList<GoodsVO>();

		String dml = "select * from goodsTBL";
		
		try (Connection connection = DBUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(dml);
				ResultSet resultSet = statement.executeQuery();) {
			while (resultSet.next()) {
				GoodsVO goods = new GoodsVO(resultSet.getString(1), resultSet.getInt(2));
				goodsList.add(goods);
			}
		}
		
		return goodsList;
	}
	
	public boolean deleteGoods(String goods) throws SQLException {

		String dml = "delete from goodsTBL where goods = ?";

		try (Connection connection = DBUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(dml);) {
			statement.setString(1, goods);
			return statement.executeUpdate() > 0;
		}
	}
	
	public boolean updateGoods(GoodsVO goodsVO, String goods, int price) throws SQLException {

		String dml = "update goodsTBL set goods = ?, price = ? where goods = ?";
		
		try (Connection connection = DBUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(dml);) {
			statement.setString(1, goods);
			statement.setInt(2, price);
			statement.setString(3, goodsVO.getGoods());
			
			return statement.executeUpdate() > 0;
		}
	}

	public boolean updateOnlyPrice(GoodsVO goodsVO, int price) throws SQLException {

		String dml = "update goodsTBL set price = ? where goods = ?";

		try (Connection connection = DBUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(dml);) {
			statement.setInt(1, price);
			statement.setString(2, goodsVO.getGoods());
			
			return statement.executeUpdate() > 0;
		}
	}

	public ObservableList<GoodsVO> getCheckGoods(String goods) throws SQLException {

		String dml = "select * from goodsTBL where goods like ?";
		
		ObservableList<GoodsVO> goodsList = FXCollections.observableArrayList();
		
		try (Connection connection = DBUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(dml);) {
			String likeGoods = "%" + goods + "%";
			statement.setString(1, likeGoods);
			
			try (ResultSet resultSet = statement.executeQuery();) {
				while (resultSet.next()) {
					GoodsVO aGoods = new GoodsVO(resultSet.getString(1), resultSet.getInt(2));
					goodsList.add(aGoods);
				}
			}
		}
		return goodsList;
	}
}
