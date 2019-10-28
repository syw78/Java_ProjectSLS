package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.CodeVO;
import util.AlertManager;
import util.DBUtil;
import util.AlertManager.AlertInfo;


public class CodeDAO {
	/*************
	 * 
	 * code 테이블의 전체 내용을 가져온다.
	 * 
	 * 서연우
	 * @return
	 */
	public ArrayList<CodeVO> getCodeTotal() {
		ArrayList<CodeVO> list = new ArrayList<CodeVO>();
		String dml = "select * from code";

		Connection con = null;
		PreparedStatement pstmt = null;
		// 데이타베이스 값을 임시로 저장하는 장소 제공하는 객체
		ResultSet rs = null;

		CodeVO codeVO = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				codeVO = new CodeVO(rs.getString(1));
				list.add(codeVO);
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
