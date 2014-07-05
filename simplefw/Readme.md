Simple FW
스프링과 같은 프레임워크등이 복잡해지면서, 어떤 방식으로 동작하는지 이해하는 것이 점점 어려워지고 있다. Servlet/JSP를 수업할 때 간단한 구조의 MVC framework와 DAO framework를 만들어 봄으로써 프레임워크의 동작원리와 Servlet/JSP, 어노테이션, 어노테이션 scan방법 (여기에서는 Visitor Pattern을 이용), DefaultServlet에 대한 내용에 대하여 이해한다.

스프링에서 제공하는 동명의 클래스와 어노테이션인 DispatcherServlet, RequestMapping, Controller 를 만들어 MVC Model2 아키텍처를 따르는 간단 FW을 만들었다.

본 소스는 교육을 위하여 만든 예제로 출처만 밝힌다면 어디에서든 사용할 수 있다.

* eclipse 프로젝트이다. 이클립스와 관련된 설정파일은 eclipse_project_config 에 넣어 두었으며, 설정파일들은 .gitignore에 저장하였다. workspace경로등이 다를 수 있기 때문에 이 부분은 ignore시켜놓고 별도로 저장해 놓음으로써 이클립스에서 프로젝트를 가지고 올 때 문제점이 없도록 한다. 
