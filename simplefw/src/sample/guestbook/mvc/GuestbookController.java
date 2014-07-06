package sample.guestbook.mvc;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sample.guestbook.dao.GuestbookDAO;
import sample.guestbook.dto.GuestbookDTO;
import fw.mvc.Controller;
import fw.mvc.RequestMapping;

@Controller
public class GuestbookController {

	@RequestMapping(path="/list", method="GET", mimetype="text/html;charset=utf-8")
	public String list(HttpServletRequest request, HttpServletResponse response){
		String pg = request.getParameter("pg");
		int ipg = 1;
		try{
			ipg = Integer.parseInt(pg);
		}catch(Exception ex){}
		
		GuestbookDAO dao = new GuestbookDAO();
		int count = dao.getGuestbookCount();
		int start = (GuestbookDAO.LIMIT * ipg) - GuestbookDAO.LIMIT +1;
		int end = GuestbookDAO.LIMIT * ipg;
		List<GuestbookDTO> list = dao.getList(start, end);
		int prevPage = -1;
		if(ipg > 1){
			prevPage = ipg -1;
		}
		int pageCount = count / GuestbookDAO.LIMIT;
		if(count % GuestbookDAO.LIMIT != 0){
			pageCount++;
		}
		
		int nextPage = -1;
		if(ipg < pageCount){
			nextPage = ipg + 1;
		}
		
		request.setAttribute("list", list);
		request.setAttribute("nextPage", nextPage);
		request.setAttribute("prevPage", prevPage);
		request.setAttribute("count", prevPage);
		request.setAttribute("pageCount", pageCount);
		request.setAttribute("pg", ipg);
		
		return "/WEB-INF/views/list.jsp";
	}
	
	@RequestMapping(path="/write", method="POST", mimetype="text/html;charset=utf-8")
	public String write(HttpServletRequest request, HttpServletResponse response){
		
		return "redirect:list";
	}
	
	@RequestMapping(path="/deleteform", method="GET", mimetype="text/html;charset=utf-8")
	public String deleteform(HttpServletRequest request, HttpServletResponse response){
		
		return "/WEB-INF/views/deleteform.jsp";
	}
	
	@RequestMapping(path="/delete", method="GET", mimetype="text/html;charset=utf-8")
	public String delete(HttpServletRequest request, HttpServletResponse response){
		
		return "redirect:list";
	}	
}
