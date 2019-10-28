package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.DBVO;
import util.AlertManager;
import util.AlertManager.AlertInfo;
import util.DBUtil;


public class DBUtilDAO {

	// 고객정보 넣기
	public void insertClient(DBVO dvo) {

		String dml = "insert into clientTBL (txtId , txtFiled, txtFiled2, txtName, frontN, backN, comboFM, phoneNum, enterpriseName) values (?, ?, ?, ?, ?, ?,?,?,?)";
		// midN, lastN,
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, dvo.getTxtId());
			pstmt.setString(2, dvo.getTxtFiled());
			pstmt.setString(3, dvo.getTxtFiled2());
			pstmt.setString(4, dvo.getTxtName());
			pstmt.setInt(5, dvo.getFrontN());
			pstmt.setInt(6, dvo.getBackN());
			pstmt.setString(7, dvo.getComboFM());
			pstmt.setString(8, dvo.getPhoneNum());
			pstmt.setString(9, dvo.getEnterpriseName());

			pstmt.execute(); // 실행시켜줌 번개모양

		} catch (SQLException e) {
			AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}

	}

	// 관리자모드 들어가는값
	public ArrayList<DBVO> getCusTotal() {
		ArrayList<DBVO> list = new ArrayList<DBVO>();
		String dml = "select * from clientTBL";

		Connection con = null;
		PreparedStatement pstmt = null;
		// 데이타베이스 값을 임시로 저장하는 장소 제공하는 객체
		ResultSet rs = null;

		DBVO dbVO = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				dbVO = new DBVO(rs.getString(1), rs.getString(4), rs.getString(8), rs.getString(9),rs.getString(10));
				// rs.getInt(9), rs.getInt(10),
				list.add(dbVO);
			}
		} catch (SQLException se) {
			AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
		} catch (Exception e) {
			AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException se) {
			}
		}
		return list;
	}

	// 고객정보 전체 리스트(select)
	public ArrayList<DBVO> getCustomerTotal() {
		ArrayList<DBVO> list = new ArrayList<DBVO>();
		String dml = "select * from clientTBL ";

		Connection con = null;
		PreparedStatement pstmt = null;
		// 데이타베이스 값을 임시로 저장하는 장소 제공하는 객체
		ResultSet rs = null;

		DBVO dbVO = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				dbVO = new DBVO(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5),
						rs.getInt(6), rs.getString(7), rs.getString(8), rs.getString(9),rs.getString(10));
				// rs.getInt(9), rs.getInt(10),
				list.add(dbVO);
			}
		} catch (SQLException se) {
			AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
		} catch (Exception e) {
			AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException se) {
			}
		}
		return list;
	}
	//유저 아이디 검색 후 얻어오기
	public String getUserIdSearch(String id) {
		String saveId = null;

		String dml = "select txtId from clientTBL where txtId = ?";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {

			con = DBUtil.getConnection();

			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				saveId = rs.getString(1);

			}

		} catch (Exception e) {
			AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
		} finally {
			try {
				pstmt.close();
				con.close();
			} catch (SQLException e) {
				AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
				e.printStackTrace();
			}
		}
		return saveId;
	}

	// 중복확인 함수
	public ArrayList<DBVO> getUserIdtest(String id) {
		ArrayList<DBVO> list = new ArrayList<DBVO>();
		String dml = "select * from clientTBL where txtId=?";

		Connection con = null;
		PreparedStatement pstmt = null;
		// 데이타베이스 값을 임시로 저장하는 장소 제공하는 객체
		ResultSet rs = null;

		DBVO dbVO = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				dbVO = new DBVO(rs.getString(1), rs.getString(4), rs.getString(8), rs.getString(9));
				list.add(dbVO);
			}
		} catch (SQLException se) {
			AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
		} catch (Exception e) {
			AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException se) {
				AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
			}
		}
		return list;
	}

	// 고객정보삭제 기능(delete)
	public void getCustomerDelete(String txtId) throws Exception {
		// ② 데이터 처리를 위한 SQL 문
		String dml = "delete from clientTBL where txtId = ?";
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			// ③ DBUtil이라는 클래스의 getConnection( )메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();

			// ⑤ SQL문을 수행후 처리 결과를 얻어옴
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, txtId);

			// ⑤ SQL문을 수행후 처리 결과를 얻어옴
			int i = pstmt.executeUpdate();

			if (i == 1) {
				AlertManager.getInstance().show(AlertInfo.SUCCESS_TASK, null);
			} else {
				AlertManager.getInstance().show(AlertInfo.ERROR_TASK, null);
			}
		} catch (SQLException e) {
			AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
		} catch (Exception e) {
			AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
		} finally {
			try {
				// ⑥ 데이터베이스와의 연결에 사용되었던 오브젝트를 해제
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
			}
		}
	}

	// 수정기능(update tableName set 필드명=수정내용 where 조건내용)
	public DBVO getCustomerUpdate(DBVO svo) throws Exception {
		String dml = "update clientTBL set txtName=?, phoneNum=?, enterpriseName=?, fileName=?  where txtId = ?";

		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			// ③ DBUtil이라는 클래스의 getConnection( )메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			// ④ 수정한 학생 정보를 수정하기 위하여 SQL문장을 생성
			
			pstmt.setString(1, svo.getTxtName());
			pstmt.setString(2, svo.getPhoneNum());
			pstmt.setString(3, svo.getEnterpriseName());
			pstmt.setString(4, svo.getFileName());
			pstmt.setString(5, svo.getTxtId());

			// ⑤ SQL문을 수행후 처리 결과를 얻어옴
			int i = pstmt.executeUpdate();

			if (i == 1) {
				AlertManager.getInstance().show(AlertInfo.SUCCESS_TASK, null);
			} else {
				AlertManager.getInstance().show(AlertInfo.ERROR_TASK, null);
				return null;
			}

		} catch (SQLException e) {
			AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
		} catch (Exception e) {
			AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
		} finally {
			try {
				// ⑥ 데이터베이스와의 연결에 사용되었던 오브젝트를 해제
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}
		return svo;
	}

	// 학생찾기 기능(select * from schoolchild where name like '%길동%')
	public ArrayList<DBVO> getCustomerCheck(String name) throws Exception {
		String dml = "select * from clientTBL where txtName like ?";
		ArrayList<DBVO> list = new ArrayList<DBVO>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		DBVO retval = null;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, "%" + name + "%");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				retval = new DBVO(rs.getString(1), rs.getString(4), rs.getString(8), rs.getString(9),rs.getString(10));
				list.add(retval);
			}
		} catch (SQLException se) {
			AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
		} catch (Exception e) {
			AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException se) {
				AlertManager.getInstance().show(AlertInfo.ERROR_TASK_DB, null);
			}
		}
		return list;
	}
}
