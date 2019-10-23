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

public class SaleDAO {

	public boolean insertSaleDB(SaleVO saleVO) throws SQLException {

		String dml = "insert into saleTBL (client, date, goodsName, price, count, total, coments) values (?,?,?,?,?,?,?)";

		try (Connection connection = DBUtil.getConnection();
		PreparedStatement statement = connection.prepareStatement(dml);) {

			statement.setString(1, LoginController.clientId);
			statement.setString(2, saleVO.getDate());
			statement.setString(3, saleVO.getGoods());
			statement.setInt(4, saleVO.getPrice());
			statement.setInt(5, saleVO.getCount());
			statement.setInt(6, saleVO.getTotal());
			statement.setString(7, saleVO.getComents());

			return statement.execute();
		} 

	} // end of insertSaleDB

	public ArrayList<SaleVO> getSaleTotal() throws SQLException {
		ArrayList<SaleVO> saleList = new ArrayList<SaleVO>();
		String dml = "select * from saleTBL";

		try (Connection connection = DBUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(dml);
				ResultSet resultSet = statement.executeQuery();) {
			while (resultSet.next()) {
				SaleVO sale = new SaleVO(
						resultSet.getInt(1),
						resultSet.getString(2), 
						resultSet.getString(3), 
						resultSet.getString(4), 
						resultSet.getInt(5), 
						resultSet.getInt(6),
						resultSet.getString(7));
				saleList.add(sale);
			}
		}
		return saleList;
	}

	public boolean deleteSale(SaleVO saleVO) throws SQLException {

		String dml = "delete from saleTBL where no = ?";

		try (Connection connection = DBUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(dml);) {
			statement.setInt(1, saleVO.getNo());
			System.out.println(saleVO.getNo());
			return statement.executeUpdate() > 0;
		}
	}
	
	public boolean deleteSale(String date) throws SQLException {

		String dml = "delete from saleTBL where date = ? ";

		try (Connection connection = DBUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(dml);) {
			statement.setString(1, date);
			return statement.executeUpdate() > 0;
		}
	}
	
	public ArrayList<SaleVO> getListToDate(String date) throws SQLException {

		String dml = "select * from saleTBL where date = ? and client = ?";
		ArrayList<SaleVO> saleList = new ArrayList<SaleVO>();
		
		try (Connection connection = DBUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(dml);) {
			statement.setString(1, date);
			statement.setString(2, LoginController.clientId);

			try (ResultSet resultSet = statement.executeQuery();) {
				while (resultSet.next()) {

					SaleVO sale = new SaleVO(
							resultSet.getInt(1),
							resultSet.getString(2), 
							resultSet.getString(3), 
							resultSet.getString(4), 
							resultSet.getInt(5), 
							resultSet.getInt(6),
							resultSet.getInt(7), resultSet.getString(8));

					saleList.add(sale);
				}
			}
		}
		return saleList;
	}
	
	public ObservableList<SaleVO> barChartData(String date) throws SQLException{
		
		String dml = "select goodsName, sum(count), sum(total) from saleTBL where date = ? and client = ? group by goodsName";
		ObservableList<SaleVO> resultList = FXCollections.observableArrayList();
		
		try(Connection connection = DBUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(dml);){
			statement.setString(1, date);
			statement.setString(2, LoginController.clientId);
			
			try(ResultSet resultSet = statement.executeQuery()) {
				
				while(resultSet.next()) {
					SaleVO sale = new SaleVO(resultSet.getString(1),
							resultSet.getInt(2), 
							resultSet.getInt(3));
					resultList.add(sale);
				}
				
			}
			
		}
		return resultList;
	}
}
