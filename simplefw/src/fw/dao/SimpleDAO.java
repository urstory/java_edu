package fw.dao;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SimpleDAO<DTO, SearchParam> {
	protected Connection getConnection() {
		String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";
		String DB_USER = "java00";
		String DB_PASSWORD = "java00";

		Connection conn = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return conn;
	}

	protected void close(Connection conn, PreparedStatement ps) {
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	protected void close(Connection conn, PreparedStatement ps, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		close(conn, ps);
	}

	protected List<DTO> select(String sql, SearchParam param) {
		String[] properties = SQLUtil.getProperties(sql);
		String[] getters = SQLUtil.changeGetMethods(properties);
		String[] setters = SQLUtil.changeSetMethods(properties);
		String psql = SQLUtil.changePreparedSql(sql, properties);

		return null;
	}

	protected DTO selectOne(String sql, SearchParam param) {

		return null;
	}

	protected int selectCount(String sql, SearchParam param) {

		return 0;
	}

	protected int update(String sql, DTO dto) {
		String[] properties = SQLUtil.getProperties(sql);
		String[] getters = SQLUtil.changeGetMethods(properties);
		String[] setters = SQLUtil.changeSetMethods(properties);
		String psql = SQLUtil.changePreparedSql(sql, properties);

		Connection conn = null;
		PreparedStatement ps = null;
		int count = 0;
		try{
			conn = getConnection();
			ps = conn.prepareStatement(psql);
			Class clazz = dto.getClass();
			for(int i = 0; i < getters.length; i++){
				Method m = clazz.getMethod(getters[i], null);
				Object obj = m.invoke(dto, null);
				ps.setObject(i+1, obj);
			}
			count = ps.executeUpdate();
		}catch(Exception ex){
			ex.printStackTrace();
			throw new RuntimeException(ex.toString());
		}finally{
			close(conn, ps);
		}
		return count;
	}

}

/*
 * 
 * //fill in the prepared statement and pInsertOid.executeUpdate(); ResultSet rs
 * = pInsertOid.getGeneratedKeys(); if (rs.next()) { int newId = rs.getInt(1);
 * oid.setId(newId); }
 */