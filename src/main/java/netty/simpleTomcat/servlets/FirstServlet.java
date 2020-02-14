package netty.simpleTomcat.servlets;

import netty.simpleTomcat.http.Request;
import netty.simpleTomcat.http.Response;
import netty.simpleTomcat.http.Servlet;

/**
 * @author wangxueming
 * @create 2020-02-14 12:13
 * @description
 */
public class FirstServlet extends Servlet {

	
	@Override
	public void doGet(Request request, Response response) {
		doPost(request, response);
	}

	
	@Override
	public void doPost(Request request, Response response) {
		String param = "name";  
	    String str = request.getParameter(param);  
	    response.write(param + ":" + str,200);
	}
	
}
