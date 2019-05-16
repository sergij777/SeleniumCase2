package org.mycomp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.mycomp.model.Good;

public class GoodDAO {
	public void saveGood(Good good) {
		Connection connection = null;
		PreparedStatement statement = null;
		String sql = "INSERT INTO goods(name, price) VALUES(?, ?)";		
		
		try {
			connection = ConnectionToDB.getConnection();
			statement = connection.prepareStatement(sql);			
			statement.setString(1, good.getGoodName());
			statement.setInt(2, good.getGoodPrice());					
			statement.executeUpdate();
			
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		finally {
			try {
				statement.close();
			} catch (SQLException e) {				
				e.printStackTrace();
			}
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		
	}

}
