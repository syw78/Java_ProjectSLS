package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import util.AlertManager;
import util.AlertManager.AlertInfo;
import util.DBUtil;

public class ClientDAO {

	public void insertClientDB(ClientVO cvo) throws SQLException{

		String dml = "insert into clientdb " + "(id,password,name,phone)" + " values " + "(?,?,?,?)";

		try (Connection connection = DBUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(dml);){
			
			statement.setString(1, cvo.getId());
			statement.setString(2, cvo.getPassword());
			statement.setString(3, cvo.getName());
			statement.setString(4, cvo.getPhone());

			statement.execute();

		}

	} // end of insertClientDB

	public ArrayList<ClientVO> getClientCheck(ClientVO cvo) throws SQLException {

		String dml = "select * from clientdb where id = ?";

		ArrayList<ClientVO> clientList = new ArrayList<ClientVO>();

		try (Connection connection = DBUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(dml);){
			
			statement.setString(1, cvo.getId());
			ResultSet resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				ClientVO clientVo = new ClientVO(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4));
				clientList.add(clientVo);
			}
		} 
		
		return clientList;

	} // end of getClientCheck

	public ArrayList<ClientVO> getClientInfo() throws SQLException{

		String dml = "select * from clientdb";
		ArrayList<ClientVO> clientList = new ArrayList<ClientVO>();

		try (Connection connection = DBUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(dml)){

			ResultSet resultSet = statement.executeQuery();
			
			while (resultSet.next()) {

				ClientVO clientVo = new ClientVO(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4));
				clientList.add(clientVo);
				
			}
		}
		
		return clientList;

	}

}
