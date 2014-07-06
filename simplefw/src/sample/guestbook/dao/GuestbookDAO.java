package sample.guestbook.dao;

import java.sql.Date;
import java.util.List;

import sample.guestbook.dto.GuestbookDTO;
import sample.guestbook.dto.ParamDTO;
import fw.dao.SimpleDAO;

public class GuestbookDAO extends SimpleDAO<GuestbookDTO, ParamDTO>{
	public static int LIMIT = 3;
	
	public List<GuestbookDTO> getList(int start, int end){
		ParamDTO param = new ParamDTO();
		param.setStart(start);
		param.setEnd(end);
		
		String sql = "select id, name, password, content, ip, regdate from ( select rownum r, id, name, password, content, ip, regdate  from ("
				+ "select id, name, password, content, ip, regdate from guestbook order by regdate desc )) where r >= #start# and r <= #end#";
		List<GuestbookDTO> list = select(sql, new Class[]{Integer.TYPE, String.class, String.class, String.class, String.class, Date.class },
				new String[]{"#id#", "#name#", "#password#", "#content#", "#ip#", "#regdate#"}, param);
		return list;
	}
	
	public GuestbookDTO get(int id){
		
		ParamDTO param = new ParamDTO();
		param.setId(id);
		String sql = "select id, name, password, content, ip, regdate from guestbook where id = #id#";
		GuestbookDTO dto = selectOne(sql, new Class[]{Integer.TYPE, String.class, String.class, String.class, String.class, Date.class },
				new String[]{"#id#", "#name#", "#password#", "#content#", "#ip#", "#regdate#"}, param);
		return dto;
	}
	
	public int update(GuestbookDTO guestbook){
		String sql = "update guestbook where name = #name#, password = #password#, content = #content# where id = #id#";
		int count = super.update(sql, guestbook);
		return count;
	}
	
	public int delete(int id){
		GuestbookDTO guestbook = new GuestbookDTO();
		guestbook.setId(id);
		
		String sql = "delete from guestbook where id = #id#";
		int count = super.update(sql, guestbook);
		return count;
	}
	
	public int insert(GuestbookDTO guestbook){
		String sql = "insert into guestbook(id, name, password, content, ip, regdate) values (guestbook_seq.NEXTVAL, #name#, #password#, #content#, #ip#, sysdate)";
		int count = super.update(sql, guestbook);
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
	
	public static void main(String args[]) {
		GuestbookDAO dao = new GuestbookDAO();
		int flag = 5;
		if(flag  == 1){
			GuestbookDTO guestbook = new GuestbookDTO();
			guestbook.setName("kim");
			guestbook.setPassword("1234");
			guestbook.setContent("helllo");
			guestbook.setIp("127.0.0.1");
			dao.insert(guestbook);
		}else if(flag == 2){
			int count = dao.delete(2);
			System.out.println(count);
		}else if(flag == 3){
			int count = dao.getGuestbookCount();
			System.out.println(count);
		}else if(flag == 4){
			GuestbookDTO dto = dao.get(4);
			System.out.println(dto);
		}else if(flag == 5){
			List<GuestbookDTO> list = dao.getList(10, 12);
			for (GuestbookDTO guestbookDTO : list) {
				System.out.println(guestbookDTO);
			}
		}
	}
}
