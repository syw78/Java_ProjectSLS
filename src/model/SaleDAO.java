package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import util.DBUtil;

public class SaleDAO {

	public void insertSaleDB(SaleVO saleVO) throws SQLException {

		String dml = "insert into saleTBL (date, goods, price, count, total, coments) values (?,?,?,?,?,?)";

		try (Connection connection = DBUtil.getConnection();
		PreparedStatement statement = connection.prepareStatement(dml);) {

			statement.setString(1, saleVO.getDate());
			statement.setString(2, saleVO.getGoods());
			statement.setInt(3, saleVO.getPrice());
			statement.setInt(4, saleVO.getCount());
			statement.setInt(5, saleVO.getTotal());
			statement.setString(6, saleVO.getComents());

			statement.execute();
		} 

	} // end of insertSaleDB

	public ArrayList<SaleVO> getSaleTotal() throws SQLException {
		ArrayList<SaleVO> saleList = new ArrayList<SaleVO>();
		String dml = "select * from saleTBL";

		try (Connection connection = DBUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(dml);
				ResultSet resultSet = statement.executeQuery();) {
			while (resultSet.next()) {
				SaleVO sale = new SaleVO(resultSet.getString(2), 
						resultSet.getString(3), 
						resultSet.getInt(4), 
						resultSet.getInt(5), 
						resultSet.getInt(6),
						resultSet.getString(7));
				saleList.add(sale);
			}
		}
		return saleList;
	}

	public boolean deleteSale(SaleVO saleVO) throws SQLException {

		String dml = "delete from saleTBL where date = ? and goods = ? and count = ?";

		try (Connection connection = DBUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(dml);) {
			statement.setString(1, saleVO.getDate());
			statement.setString(2, saleVO.getGoods());
			statement.setInt(3, saleVO.getCount());
			return statement.executeUpdate() > 0;
		}
	}
	
	public ArrayList<SaleVO> getListToDate(String date) throws SQLException {

		String dml = "select date, goods, price, count, total, coments from saleTBL where date = ?";
		ArrayList<SaleVO> saleList = new ArrayList<SaleVO>();
		
		try (Connection connection = DBUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(dml);) {
			statement.setString(1, date);

			try (ResultSet resultSet = statement.executeQuery();) {
				while (resultSet.next()) {

					SaleVO sale = new SaleVO(resultSet.getString(1), 
							resultSet.getString(2), 
							resultSet.getInt(3), 
							resultSet.getInt(4), 
							resultSet.getInt(5),
							resultSet.getString(6));

					saleList.add(sale);
				}
			}
		}
		return saleList;
	}

	public ArrayList<SaleVO> searchGoodsVO(String goods, String date) throws SQLException {

		String dml = "select * from saleTBL where goods like ? and date = ?";

		ArrayList<SaleVO> saleList = new ArrayList<SaleVO>();
		
		try (Connection connection = DBUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(dml);) {
			String likeGoods = "%" + goods + "%";
			statement.setString(1, likeGoods);
			statement.setString(2, date);

			try (ResultSet resultSet = statement.executeQuery();) {
				while (resultSet.next()) {

					SaleVO sale = new SaleVO(resultSet.getString(2), 
							resultSet.getString(3), 
							resultSet.getInt(4), 
							resultSet.getInt(5), 
							resultSet.getInt(6),
							resultSet.getString(7));

					saleList.add(sale);
				}
			}
		}
		return saleList;
	}
}
