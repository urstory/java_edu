package sample.guestbook.dao;

import java.util.List;

import sample.guestbook.dto.GuestbookDTO;
import sample.guestbook.dto.ParamDTO;
import fw.dao.SimpleDAO;

public class GuestbookDAO extends SimpleDAO<GuestbookDTO, ParamDTO>{
	public List<GuestbookDTO> getList(int start, int end){
		
		ParamDTO param = new ParamDTO();
		String sql = "select id, name, password, content, ip, regdate from ( select rownum r, id, name, password, content, ip, regdate  from ("
				+ "select id, name, password, content, ip, regdate from guestbook regdate desc )) where r >= #start# and r <= #end#";
		List<GuestbookDTO> list = select(sql, param);
		return list;
	}
	
	public GuestbookDTO get(int id){
		
		ParamDTO param = new ParamDTO();
		param.setId(id);
		String sql = "select id, name, password, content, ip, regdate from guestbook where id = #value#";
		GuestbookDTO dto = selectOne(sql, param);
		return dto;
	}
	
	public GuestbookDTO update(GuestbookDTO guestbook){
		String sql = "update guestbook where name = #name#, password = #password#, content = #content# where id = #id#";
		guestbook = super.update(sql, guestbook);
		return guestbook;
	}
	
	public int deleteGuestbook(int id){
		ParamDTO param = new ParamDTO();
		param.setId(id);
		
		String sql = "delete from guestbook where id = #id#";
		int count = delete(sql, param);
		return count;
	}
	
	public int getGuestbookCount(){
		
		String sql = "select count(*) from guestbook";
		int count = selectCount(sql, null);
		return count;
	}
	
	public boolean isPassword(int id, String password){
		ParamDTO param = new ParamDTO();
		param.setId(id);
		param.setPassword(password);
		String sql = "select count(*) from guestbook where id = #id# and password = #password#";
		int count = selectCount(sql, param);
		if(count > 0)
			return true;
		else 
			return false;
	}
}
