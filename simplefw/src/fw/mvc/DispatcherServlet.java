package fw.mvc;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

public class DispatcherServlet extends HttpServlet {
	private Map<String, ControllerInfo> getMap;
	private Map<String, ControllerInfo> postMap;
	private Map<Class, Object> controllerObjMap;
	private String resourceDir;
	public DispatcherServlet() {
		// 같은 path로 get과 post방식으로 요청할 수 있기 때문에
		getMap = new HashMap<String, ControllerInfo>();
		postMap = new HashMap<String, ControllerInfo>();
		controllerObjMap = new HashMap<Class, Object>();
	}

	// Controller 를 추가하면 WAS를 재시작 하거나 웹 어플리케이션을 재시작 해야한다..
	@Override
	public void init(ServletConfig sc) throws ServletException {

		// Controller 를 찾는다.
		String prevPackage = sc.getInitParameter("prev-package");
		resourceDir = sc.getInitParameter("resource-dir");
		ControllerVisitor visitor = new ControllerVisitor(prevPackage);
		ClassFinder.findClasses(visitor);
		List<Class> controllerClassList = visitor.getControllerClassList();
		//System.out.println("size : " + controllerClassList.size());
		// Controller가 가지고 있는 RequestMapping정보를 읽어들여서 GET방식의 경우 getMap에 POST방식의
		// 경우 postMap에 각기 저장한다.
		for (Class controllerClass : controllerClassList) {
			try{
				if(!controllerObjMap.containsKey(controllerClass.getName())){
					Object controllerObj = controllerClass.newInstance();
					controllerObjMap.put(controllerClass, controllerObj);
				}
			}catch(Exception ex){
				System.out.println("error : " + controllerClass.getName() + " 에 대한 인스턴스를 생성할 수 없습니다.");
				continue;
			}
			Method methods[] = controllerClass.getDeclaredMethods();
			for (Method method : methods) {
				Object annonObj = method.getAnnotation(RequestMapping.class);
				if(annonObj instanceof RequestMapping){
					RequestMapping rm = (RequestMapping)annonObj;
					ControllerInfo ci = new ControllerInfo();
					ci.setPath(rm.path());
					ci.setControllerClass(controllerClass);
					ci.setMethod(method);
					ci.setMimeType(rm.mimetype());				
					if("get".equals(rm.method().toLowerCase())){
						getMap.put(rm.path(), ci);
					}else if("post".equals(rm.method().toLowerCase())){
						postMap.put(rm.path(), ci);
					}else{
						System.out.println(controllerClass.getName() + "." + method.getName() + "의 RequestMapping 속성 method에 GET or POST가 아닌 값이 설정되어 있습니다.");
					}
				}
			} // for
		} // for
	}

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String path = request.getRequestURI().substring(
				request.getContextPath().length());
		
		if(path.startsWith(resourceDir)){
			RequestDispatcher rd = request.getServletContext().getNamedDispatcher("default");
			rd.forward(new DefaultRequestWrapper(request), response);
			return;
		}
		
		if(path.startsWith("/WEB-INF") && path.indexOf(".jsp") > 0){
			RequestDispatcher rd = request.getServletContext().getNamedDispatcher("jsp");
			rd.forward(request, response);			
			return;
		}
		
		ControllerInfo ci = null;
		if("get".equals(request.getMethod().toLowerCase())){
			ci = getMap.get(path);
		}else if("post".equals(request.getMethod().toLowerCase())){
			ci = postMap.get(path);
		}else{
			System.out.println("Simple fw 은 get과 post방식만 지원합니다.");
			throw new RuntimeException("Simple fw 은 get과 post방식만 지원합니다.");
		}
		
		if(ci == null){
			System.out.println("ci is null");
			response.sendError(404);
			return;			
		}
		
		Method method = ci.getMethod();
		Object controllerObj = controllerObjMap.get(ci.getControllerClass());
		String result = null;
		try{
			Object obj = method.invoke(controllerObj, new Object[]{request, response});
			result = (String)obj;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		try{
			if(result != null){
				if(result.indexOf("redirect:") == 0){
					response.sendRedirect(result.substring("redirect:".length()));
				}else{
					RequestDispatcher dispatcher = request.getRequestDispatcher(result);
					dispatcher.forward(request, response);
				}
			}else{
				if(ci.getMimeType() != null)
					response.setContentType(ci.getMimeType());
			}
		}catch(Exception ex){
			System.out.println("redirect, forward를 시도하다 오류 발생 : " + ci);
			ex.printStackTrace();
		}
	} // service
	

}

class DefaultRequestWrapper extends HttpServletRequestWrapper {
	private HttpServletRequest request;
	
	public DefaultRequestWrapper(HttpServletRequest request){
		super(request);
		this.request = request;
	}
};

class ControllerInfo {
	private String path; // URL path
	private String mimeType; // mime type
	private Method method; // controller method
	private Class controllerClass; 

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}



	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public Class getControllerClass() {
		return controllerClass;
	}

	public void setControllerClass(Class controllerClass) {
		this.controllerClass = controllerClass;
	}

	@Override
	public String toString() {
		return "ControllerInfo [path=" + path + ", mimeType=" + mimeType
				+ ", method=" + method + ", controllerClass=" + controllerClass
				+ "]";
	}


	

}

class ControllerVisitor implements Visitor<String>{
	private String prevPackage;
	private List<Class> controllerClassList;
	public ControllerVisitor(String prevPackage){
		this.prevPackage = prevPackage;
		controllerClassList = new ArrayList<Class>();
	}

	@Override
	public boolean visit(String visitClass) {
		if(visitClass != null){
			if(visitClass.indexOf(prevPackage) == 0){
				try{
					Class clazz = Class.forName(visitClass);
					Object obj = clazz.getAnnotation(Controller.class);
					if(obj != null){
						controllerClassList.add(clazz);
					}
				}catch(ClassNotFoundException cnfe){
				}
			}
		}
		return true;
	}

	public List<Class> getControllerClassList() {
		return controllerClassList;
	}

	
}


// Visitor 패턴을 이용한 파일 검색 
interface Visitor<T> {
    public boolean visit(T t);
}

class ClassFinder {
    public static void findClasses(Visitor<String> visitor) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL[] urls = ((URLClassLoader) cl).getURLs();


        File file = null;
        for (URL url : urls) {
            file = new File(url.getFile());
            System.out.println(file.getAbsolutePath());
            if (file.exists()) {
                findClasses(file, file, false, visitor);
            }
        }
    }

    private static boolean findClasses(File root, File file, boolean includeJars, Visitor<String> visitor) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                if (!findClasses(root, child, includeJars, visitor)) {
                    return false;
                }
            }
        } else {
            if (file.getName().toLowerCase().endsWith(".jar") && includeJars) {
                JarFile jar = null;
                try {
                    jar = new JarFile(file);
                } catch (Exception ex) {

                }
                if (jar != null) {
                    Enumeration<JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        String name = entry.getName();
                        int extIndex = name.lastIndexOf(".class");
                        if (extIndex > 0) {
                            if (!visitor.visit(name.substring(0, extIndex).replace("/", "."))) {
                                return false;
                            }
                        }
                    }
                }
            }
            else if (file.getName().toLowerCase().endsWith(".class")) {
                if (!visitor.visit(createClassName(root, file))) {
                    return false;
                }
            }
        }

        return true;
    }

    private static String createClassName(File root, File file) {
        StringBuffer sb = new StringBuffer();
        String fileName = file.getName();
        sb.append(fileName.substring(0, fileName.lastIndexOf(".class")));
        file = file.getParentFile();
        while (file != null && !file.equals(root)) {
            sb.insert(0, '.').insert(0, file.getName());
            file = file.getParentFile();
        }
        return sb.toString();
    }
}