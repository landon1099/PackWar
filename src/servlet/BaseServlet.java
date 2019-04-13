package servlet;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("all")
public class BaseServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		try {
			String methodName = request.getParameter("method");
			//获得当前被访问对象的字节码对象
			Class clazz = this.getClass();
			//获得当前字节码对象中的指定方法
			Method method = clazz.getMethod(methodName, HttpServletRequest.class,HttpServletResponse.class);
			//执行方法
			method.invoke(this, request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//转向的页面
	protected void forward(HttpServletRequest request,HttpServletResponse response,String uri)
					throws ServletException, IOException{
		response.setContentType("text/html; charset=utf-8");
		String path = "/" + uri + ".jsp";
		RequestDispatcher rd = getServletContext().getRequestDispatcher(path);
	    rd.forward(request, response);
	    return ;
	}
	
	
	//定向页面
	protected void redirect(HttpServletRequest request,HttpServletResponse response,String uri)
					throws ServletException, IOException{
		String path = "/" + uri + ".jsp";
		response.sendRedirect(request.getContextPath() + path);
		return;
	}
}