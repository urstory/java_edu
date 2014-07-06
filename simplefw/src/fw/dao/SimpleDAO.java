package fw.dao;

import java.util.List;

public class SimpleDAO<DTO, SearchParam> {
	public List<DTO> select(String sql, SearchParam param){
		
		return null;
	}
	
	public DTO selectOne(String sql, SearchParam param){
		
		return null;
	}
	
	public int selectCount(String sql, SearchParam param){
		
		return 0;
	}
	
	public DTO add(String sql, DTO dto){
		
		return dto;
	}
	
	public DTO update(String sql, DTO dto){
		
		return dto;
	}
	public int delete(String sql, SearchParam param){
		
		return 0;
	}
}


/*

//fill in the prepared statement and
pInsertOid.executeUpdate();
ResultSet rs = pInsertOid.getGeneratedKeys();
if (rs.next()) {
int newId = rs.getInt(1);
oid.setId(newId);
}

*/