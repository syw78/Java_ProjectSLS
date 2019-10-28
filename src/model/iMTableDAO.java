package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.iMTableVO;
import util.AlertManager;
import util.DBUtil;
import util.AlertManager.AlertInfo;

public class iMTableDAO {

	// 1. 데이터 입력
	public void insertClientIM(iMTableVO ivo) {

		String dml = "insert into iMTableTBL " + "(no, date, name, pay, ea, remakers) "
				+ "values (null, ?, ?, ?, ?, null);";

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			// ③ DBUtil 클래스의 getConnection( )메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();
			// ④ 입력받은 품목 정보를 처리하기 위하여 SQL문장을 생성
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, ivo.getDate());
			pstmt.setString(2, ivo.getName());
			pstmt.setInt(3, ivo.getPay());
			pstmt.setInt(4, ivo.getEa());

			pstmt.execute();

		} catch (SQLException e) {

			AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);

		} finally {

			try {
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);

			}
		}
	}

	public ArrayList<iMTableVO> getClientTotal() {
		ArrayList<iMTableVO> list = new ArrayList<iMTableVO>();
		String dml = "select * from Client";

		Connection con = null;
		PreparedStatement pstmt = null;
		// 데이타베이스 값을 임시로 저장하는 장소 제공하는 객체
		ResultSet rs = null;
		iMTableVO iMTableVO = null;

		try {

			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			rs = pstmt.executeQuery();

			while (rs.next()) {

				iMTableVO = new iMTableVO(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5),
						rs.getString(6));
				list.add(iMTableVO);

			}

		} catch (SQLException se) {
			AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
		} catch (Exception e1) {
			AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
		} finally {
			try {

				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
			}
		}
		return list;
	}

	// 넘버로 데이터베이스에 저장된 값을 검색해서 가져오기
	public ObservableList<iMTableVO> getSearchData(String name) {
		ObservableList<iMTableVO> list = FXCollections.observableArrayList();

		String dml = "select * from iMTableTBL where name like ?";

		Connection con = null;
		PreparedStatement pstmt = null;
		// 데이타베이스 값을 임시로 저장하는 장소 제공하는 객체
		ResultSet rs = null;
		iMTableVO iMTableVO = null;

		try {

			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, "%" + name + "%");
			rs = pstmt.executeQuery();
			while (rs.next()) {

				iMTableVO = new iMTableVO(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5),
						rs.getString(6));
				list.add(iMTableVO);

			}

		} catch (SQLException se) {
			AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
		} catch (Exception e1) {
			AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
		} finally {

			try {

				if (rs != null) {

					rs.close();
				}
				if (pstmt != null) {

					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
			}
		}
		return list;
	}

	// 데이터베이스에 저장된 전체 재고 값을 검색해서 가져오기
	public ArrayList<iMTableVO> getiMTableTBLTotal() {
		ArrayList<iMTableVO> list = new ArrayList<iMTableVO>();
		String dml = "select * from iMTableTBL";

		Connection con = null;
		PreparedStatement pstmt = null;
		// 데이타베이스 값을 임시로 저장하는 장소 제공하는 객체
		ResultSet rs = null;
		iMTableVO iMTableVO = null;

		try {

			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			rs = pstmt.executeQuery();

			while (rs.next()) {

				iMTableVO = new iMTableVO(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5),
						rs.getString(6));
				list.add(iMTableVO);

			}

		} catch (SQLException se) {
			AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
		} catch (Exception e1) {
			AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
		} finally {
			try {

				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
			}
		}
		return list;
	}

	// 날짜를 가지고 데이터 베이스에서 값을 가져온다.
	public ObservableList<iMTableVO> getListToDate(String date) throws SQLException, ClassNotFoundException {

		String dml = "select * from iMTableTBL where date = ?";
		ObservableList<iMTableVO> returnList = FXCollections.observableArrayList();

		try (Connection connection = DBUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(dml);) {
			statement.setString(1, date);
			try (ResultSet resultSet = statement.executeQuery();) {
				while (resultSet.next()) {
					iMTableVO iMTableVO = new iMTableVO(resultSet.getInt(1), resultSet.getString(2),
							resultSet.getString(3), resultSet.getInt(4), resultSet.getInt(5), resultSet.getString(6));
					returnList.add(iMTableVO);
				}
			}
		}

		return returnList;
	}

	public boolean deleteData(int no) throws ClassNotFoundException, SQLException {

		String dml = "delete from iMTableTBL where no = ?";

		try (Connection connection = DBUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(dml)) {
			statement.setInt(1, no);
			return statement.executeUpdate() > 0;
		}
	}

	public ObservableList<iMTableVO> barChartDatabase(String date) throws ClassNotFoundException, SQLException {

		String dml = "select name, sum(ea) from iMTableTBL where date = ? group by name";
		ObservableList<iMTableVO> returnList = FXCollections.observableArrayList();

		try (Connection connection = DBUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(dml)) {
			statement.setString(1, date);
			
			try(ResultSet resultSet = statement.executeQuery()) {
				while(resultSet.next()) {
					iMTableVO iMTableVO = new iMTableVO(resultSet.getString(1),
							resultSet.getInt(2));
					returnList.add(iMTableVO);
				}
			}
		}

		return returnList;
	}

}
