package sample.guestbook.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fw.mvc.Controller;
import fw.mvc.RequestMapping;

@Controller
public class IndexController {
	@RequestMapping(path="/", method="GET", mimetype="text/html;charset=utf-8")
	public String index(HttpServletRequest request, HttpServletResponse response){
		
		return "redirect:list";
	}
	
}
