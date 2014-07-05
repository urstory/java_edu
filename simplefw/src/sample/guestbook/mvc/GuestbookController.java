package sample.guestbook.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fw.mvc.Controller;
import fw.mvc.RequestMapping;

@Controller
public class GuestbookController {

	@RequestMapping(path="/list", method="GET", mimetype="text/html;charset=utf-8")
	public String list(HttpServletRequest request, HttpServletResponse response){
		
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
