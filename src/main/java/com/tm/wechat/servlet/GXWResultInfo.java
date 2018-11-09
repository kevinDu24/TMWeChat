//package com.tm.wechat.servlet;
//
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.support.SpringBeanAutowiringSupport;
//
//import javax.servlet.ServletConfig;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//public class GXWResultInfo extends HttpServlet {
//
//
//	public void service(HttpServletRequest request, HttpServletResponse response)
//			throws IOException, ServletException {
//		request.setCharacterEncoding("utf-8");
//		response.setCharacterEncoding("utf-8");
//		try{
//			String success = String.valueOf(request.getParameter("success"));//授权结果
//
//			String resmsg = "";
//			if(success.equals("1")){
//				resmsg = "<html><head><title>认证成功</title></head><body><div align=\"center\" style=\"padding:150px 0;\">" +
//						"<img src=\"img/happy.png\" align=\"center\" alt=\".\" height=\"100px\"></h1>" +
//						"<h1 align=\"center\">恭喜您，优质认证成功！</h1>" +
//						"</div></body></html>";
//
//			}else if (success.equals("0")){
//				resmsg = "<html><head><title>认证失败</title></head><body><div align=\"center\" style=\"padding:150px 0;\">" +
//						"<img src=\"img/sad.png\" align=\"center\" alt=\".\" height=\"100px\"></h1>" +
//						"<h1 align=\"center\">很抱歉，认证失败！</h1>" +
//						"</div></body></html>";
//			}
//		   	responseStream(response, resmsg);// 返回数据
//		}catch(Exception e){
//			e.printStackTrace();
//			responseStream(response,"");// 返回数据
//		}
//	}
//
//
//	/**
//	 * 返回数据流
//	 */
//	private void responseStream(HttpServletResponse response, String msg) {
//		try {
//			response.setContentType("text/html");
//			response.getOutputStream().write(msg.getBytes("utf-8"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	public void destroy() {
//		super.destroy();
//	}
//
//	public void init(ServletConfig config) throws ServletException {
//		super.init(config);
//		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
//	}
//
//
//}