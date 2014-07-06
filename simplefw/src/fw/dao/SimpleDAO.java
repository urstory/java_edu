package fw.dao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

	protected List<DTO> select(String sql, Class[] rsClasses, String[] rsProperties, SearchParam param) {
		List<DTO> list = new ArrayList<DTO>();
		
		String[] properties = SQLUtil.getProperties(sql);
		String[] getters = SQLUtil.changeGetMethods(properties);
		String[] setters = SQLUtil.changeSetMethods(properties);
		String[] rsSetters = SQLUtil.changeSetMethods(rsProperties);
		String psql = SQLUtil.changePreparedSql(sql, properties);
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			conn = getConnection();
			ps = bindProperties(param, getters, psql, conn);
			rs = ps.executeQuery();
			while(rs.next()){
				DTO dto = getResult(rsClasses, rsSetters, rs);
				list.add(dto);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			throw new RuntimeException(ex.toString());
		}finally{
			close(conn, ps, rs);
		}	
		return list;
	}

	protected DTO selectOne(String sql, Class[] rsClasses, String[] rsProperties, SearchParam param) {
		DTO dto = null;
		String[] properties = SQLUtil.getProperties(sql);
		String[] getters = SQLUtil.changeGetMethods(properties);
		String[] setters = SQLUtil.changeSetMethods(properties);
		String[] rsSetters = SQLUtil.changeSetMethods(rsProperties);
		String psql = SQLUtil.changePreparedSql(sql, properties);
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			conn = getConnection();
			ps = bindProperties(param, getters, psql, conn);
			rs = ps.executeQuery();
			if(rs.next()){
				dto = getResult(rsClasses, rsSetters, rs);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			throw new RuntimeException(ex.toString());
		}finally{
			close(conn, ps, rs);
		}		
		return dto;
	}

	private DTO getResult(Class[] rsClasses, String[] rsSetters, ResultSet rs)
			throws InstantiationException, IllegalAccessException,
			SQLException, NoSuchMethodException, InvocationTargetException {
		DTO dto;
		dto = (DTO)((Class)(((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0])).newInstance();
		for(int i = 0; i < rsSetters.length; i++){
			Object obj = null;
			if(rsClasses[i] == Integer.TYPE){
				obj = rs.getInt(i+1);
			}else if(rsClasses[i] == String.class){
				obj = rs.getString(i+1);
			}else if(rsClasses[i] == Date.class){
				obj = rs.getDate(i+1);
			}else if(rsClasses[i] == Double.class){
				obj = rs.getDouble(i+1);
			}
			Method m = dto.getClass().getMethod(rsSetters[i], new Class[]{rsClasses[i]});
			m.invoke(dto, new Object[]{obj});
		}
		return dto;
	}

	protected int selectCount(String sql, SearchParam param) {
		int count = 0;
		String[] properties = SQLUtil.getProperties(sql);
		String[] getters = SQLUtil.changeGetMethods(properties);
		String[] setters = SQLUtil.changeSetMethods(properties);
		String psql = SQLUtil.changePreparedSql(sql, properties);
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			conn = getConnection();
			ps = bindProperties(param, getters, psql, conn);
			rs = ps.executeQuery();
			rs.next();
			count = rs.getInt(1);
		}catch(Exception ex){
			ex.printStackTrace();
			throw new RuntimeException(ex.toString());
		}finally{
			close(conn, ps, rs);
		}		
		return count;
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
			ps = bindProperties(dto, getters, psql, conn);
			count = ps.executeUpdate();
		}catch(Exception ex){
			ex.printStackTrace();
			throw new RuntimeException(ex.toString());
		}finally{
			close(conn, ps);
		}
		return count;
	}

	private PreparedStatement bindProperties(Object dto, String[] getters,
			String psql, Connection conn) throws SQLException,
			NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		PreparedStatement ps;
		ps = conn.prepareStatement(psql);
		if(dto != null){
			Class clazz = dto.getClass();
			for(int i = 0; i < getters.length; i++){
				Method m = clazz.getMethod(getters[i], null);
				Object obj = m.invoke(dto, null);
				ps.setObject(i+1, obj);
			}
		}
		return ps;
	}

}

