package netty.tomcat.http;
/**
 * @author wangxueming
 * @create 2020-02-14 12:13
 * @description
 */
public abstract class Servlet {
	
	public void doGet(Request request,Response response){}
	
	public void doPost(Request request,Response response){}
}
