//package com.tm.wechat.servlet;
//
//import com.tm.wechat.config.VersionProperties;
//import com.tm.wechat.dao.ApplyInfoNewRepository;
//import com.tm.wechat.dao.ApplyInfoRepository;
//import com.tm.wechat.dao.GXB01Repository;
//import com.tm.wechat.dao.GXB02Repository;
//import com.tm.wechat.domain.ApplyInfo;
//import com.tm.wechat.domain.GXB01;
//import com.tm.wechat.domain.GXB02;
//import com.tm.wechat.service.gxb.GxbService;
//import com.tm.wechat.consts.CertificationStatus;
//import org.springframework.beans.factory.annotation.Autowired;
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
//public class GXWServerInfo extends HttpServlet {
//
//	@Autowired
//	private GXB02Repository gXB02Repository;
//
////	@Autowired
////	private ApplyInfoRepository applyInfoRepository;
//
//	@Autowired
//	private ApplyInfoNewRepository applyInfoNewRepository;
//
//	@Autowired
//	private GXB01Repository gXB01Repository;
//
//	@Autowired
//	private VersionProperties versionProperties;
//
//	@Autowired
//	private GxbService gxbService;
//
//
//	public void service(HttpServletRequest request, HttpServletResponse response)
//			throws IOException, ServletException {
//		request.setCharacterEncoding("utf-8");
//		response.setCharacterEncoding("utf-8");
//		try{
//
//			String sequenceNo = String.valueOf(request.getParameter("sequenceNo"));//uuid
//			String token = String.valueOf(request.getParameter("token"));
//			String authStatus = String.valueOf(request.getParameter("authStatus"));
//			String authJson = String.valueOf(request.getParameter("authJson"));
//			String resmsg = "";
//			if(authStatus.equals("1")){
//				GXB02 gxb02 =new GXB02();
//				gxb02.setSequenceNo(sequenceNo);
//				gxb02.setToken(token);
//				gxb02.setAuthStatus(authStatus);
//				gxb02.setAuthJson(authJson);
//				gXB02Repository.save(gxb02);
//				// 更新apply_info表的认证状态值-
//				updateState(sequenceNo);
//				// 插入数据到数据库
//				gxbService.processGxb02Data(sequenceNo);
//				resmsg="{\"retCode\": 1, \"retMsg\": \"成功\"}";
//			}else if (authStatus.equals("2")){
//				resmsg="{\"retCode\": 2, \"retMsg\": \"失败\"}";
//			}
//			responseStream(response, resmsg);// 返回数据
//		}catch(Exception e){
//			e.printStackTrace();
//			responseStream(response,"{\"retCode\": 1, \"retMsg\": \"成功\"}");// 返回数据
//		}
//	}
//
//	private void updateState(String sequenceNo) {
//		GXB01 gxb01 = gXB01Repository.findBySequenceNo(sequenceNo);
//		if(gxb01 == null){
//			return;
//		}
//		String type = gxb01.getType();
//		ApplyInfo applyInfo = applyInfoRepository.findByApprovalUuidAndVersion(gxb01.getUniqueMark(), versionProperties.getVersion());
//		if (applyInfo == null) {
//			return;
//		}
//		String certificationStatus = applyInfo.getCertificationStatus();
//		if(certificationStatus == null || certificationStatus.isEmpty()){
//			certificationStatus = CertificationStatus.MOREN.code();
//		}
//		if(CertificationStatus.WEIXIN.value().equals(type)){
//			certificationStatus = "1".concat(certificationStatus.substring(1));
//		}else if(CertificationStatus.ZHIFUBAO.value().equals(type)){
//			certificationStatus = certificationStatus.substring(0,1).concat("1").concat(certificationStatus.substring(2));
//		}else if(CertificationStatus.TAOBAO.value().equals(type)){
//			certificationStatus = certificationStatus.substring(0,2).concat("1");
//		}
//		applyInfo.setCertificationStatus(certificationStatus);
//		applyInfoRepository.save(applyInfo);
//	}
//
//	/**
//	 * 返回数据流
//	 */
//	private void responseStream(HttpServletResponse response, String msg) {
//		try {
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