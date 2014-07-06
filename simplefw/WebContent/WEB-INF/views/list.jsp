<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>list.jsp</title>
</head>
<body>
	<h1>방명록</h1>
		
		request.setAttribute("list", list);    ${list }<br>
		request.setAttribute("nextPage", nextPage); ${nextPage }<br>
		request.setAttribute("prevPage", prevPage); ${prevPage }<br>
		request.setAttribute("count", prevPage); ${count }<br>
		request.setAttribute("pageCount", pageCount); ${pageCount }<br>
		request.setAttribute("pg", ipg); ${pg }<br>
		
		
</body>
</html>