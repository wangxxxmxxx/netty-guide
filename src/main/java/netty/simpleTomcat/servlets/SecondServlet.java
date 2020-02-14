package netty.simpleTomcat.servlets;

import com.alibaba.fastjson.JSON;
import netty.simpleTomcat.http.Request;
import netty.simpleTomcat.http.Response;
import netty.simpleTomcat.http.Servlet;

/**
 * @author wangxueming
 * @create 2020-02-14 12:13
 * @description
 */
public class SecondServlet extends Servlet {

	@Override
	public void doGet(Request request, Response response) {
		doPost(request, response);
	}
	
	@Override
	public void doPost(Request request, Response response) {
	    String str = JSON.toJSONString(request.getParameters(),true);  
	    response.write(str,200);
	}
	
}
