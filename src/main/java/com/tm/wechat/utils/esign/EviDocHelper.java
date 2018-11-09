package com.tm.wechat.utils.esign;

import com.tm.wechat.dto.contractsign.ApplicantContractSignDto;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class EviDocHelper {
	private static Logger LOG = LoggerFactory.getLogger(EviDocHelper.class);
	private static String encoding = null;
	private static String algorithm = null;
	private static String projectId = null;
	private static String projectSecret = null;
	// 文档保全测试环境地址
	private static String eviUrl = null;
	// 待保全文档路径
	private static String filePath = null;

	/**
	 * 文档保全方法
	 *
	 * @param personalInfo
	 * @param type 0：存证确认函 1：存证合同 2：存证抵押 3：存证还款计划表 4：风险告知书
	 * @return
	 */
	public static boolean eviDoc(ApplicantContractSignDto personalInfo, int type) {
		String pdfUrl;
		switch (type) {
			case 1:
				pdfUrl = personalInfo.getContactSignedPdf();
				break;
			case 0:
				pdfUrl = personalInfo.getConfirmationSignedPdf();
				break;
			case 2:
				pdfUrl = personalInfo.getMortgageContractSignedPdf();
				break;
			case 3:
				pdfUrl = personalInfo.getRepaymentPlanSignedPdf();
				break;
			case 4:
				pdfUrl = personalInfo.getRiskNotificationSignedPdf();
				break;
			default:
				pdfUrl = personalInfo.getContactSignedPdf();
				break;
		}
//		if(f){
//			pdfUrl = personalInfo.getContactSignedPdf();
//		} else {
//			pdfUrl = personalInfo.getConfirmationSignedPdf();
//		}
		if(pdfUrl == null || pdfUrl.isEmpty()){
			return false;
		}
		String[] arr = pdfUrl.split("http://wx.xftm.com/files/download/signedPdf/"); //http://192.168.1.115:8588/files/download/signedPdf/
		if(arr.length != 2){
			return false;
		}
		String path = "/var/www/html/signedPdf/" + arr[1];
		// 初始化全部参数
		initGlobalParameters(path);

		// 用户获取文档保全Url和存证编号
		Map<String, String> eviMap = getEviUrlAndEvId(personalInfo, type);

		// 上传需要保全的文档
		if ("0".equals(eviMap.get("errCode"))) {
			String updateUrl = eviMap.get("文档保全上传Url");
			String evId = eviMap.get("存证编号");
			System.out.println("存证编号= " + evId);
			System.out.println("文件上传地址= " + updateUrl);
			// 文件上传
			return updateFileRequestByPost(updateUrl,filePath);
		} else {
			return false;
		}
	}

	/***
	 * 初始化全局参数
	 */
	private static void initGlobalParameters(String path) {
		encoding = "UTF-8";
		algorithm = "HmacSHA256";
		projectId = "1111564463";// 1111563517
		projectSecret = "eb61b9c5a45a5b3cde5f5eecb6dd0c27"; // 95439b0863c241c63a861b87d1e647b7
		// 文档保全测试环境地址
		eviUrl = "http://evislb.tsign.cn:8080/evi-service/evidence/v1/preservation/original/url"; //http://smlcunzheng.tsign.cn:8083/evi-service/evidence/v1/preservation/original/url
		// 待保全文档 默认路径在项目下的files文件夹下
		filePath = path;
	}

	/***
	 * 用户获取文档保全Url和存证编号
	 *
	 * @return
	 */
	public static Map<String, String> getEviUrlAndEvId(ApplicantContractSignDto personalInfo, int type) {
		Map<String, String> hashMap = null;
		JSONObject jsonObj = eviRequestByPost(personalInfo, type);
		String errCode = jsonObj.get("errCode").toString();
		if ("0".equals(errCode)) {
			hashMap = new HashMap<String, String>();
			String evId = jsonObj.get("evid").toString();
			String updateUrl = jsonObj.get("url").toString();
			hashMap.put("errCode", errCode);
			hashMap.put("存证编号", evId);
			hashMap.put("文档保全上传Url", updateUrl);
		} else {
			hashMap = new HashMap<String, String>();
			hashMap.put("errCode", errCode);
			System.out.println("errCode = " + errCode + "msg = " + jsonObj.get("msg"));
		}
		return hashMap;
	}

	/***
	 * 模拟发送文档保全Url请求 ，请求方式：POST
	 *
	 * @return
	 */
	public static JSONObject eviRequestByPost(ApplicantContractSignDto personalInfo, int type) {
		StringBuffer strBuffer = null;
		try {
			// 建立连接
			URL url = new URL(eviUrl);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			initPostHeaders(httpURLConnection, personalInfo, type);
			// 连接会话
			httpURLConnection.connect();
			// 建立输入流，向指向的URL传入参数
			DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
			// 设置请求参数
			dos.write(setPostJSONStr(personalInfo, type).getBytes("UTF-8"));
			System.out.println("body = " + setPostJSONStr(personalInfo, type));
			dos.flush();
			dos.close();
			// 获得响应状态
			int resultCode = httpURLConnection.getResponseCode();
			if (HttpURLConnection.HTTP_OK == resultCode) {
				strBuffer = new StringBuffer();
				String readLine = new String();
				BufferedReader responseReader = new BufferedReader(
						new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
				while ((readLine = responseReader.readLine()) != null) {
					strBuffer.append(readLine);
				}
				responseReader.close();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return JSONObject.fromObject(strBuffer.toString());
	}

	/***
	 * 模拟上传文档请求 ，请求方式：Put
	 *
	 * @return
	 */
	public static boolean updateFileRequestByPost(String updateUrl,String filePath) {
		StringBuffer strBuffer = null;
		try {
			// 建立连接
			URL url = new URL(updateUrl);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setDoOutput(true); // 需要输出
			httpURLConnection.setDoInput(true); // 需要输入
			httpURLConnection.setUseCaches(false); // 不允许缓存

			httpURLConnection.setRequestMethod("PUT");
			httpURLConnection.setRequestProperty("Content-MD5", AlgorithmHelper.getContentMD5(filePath));
			httpURLConnection.setRequestProperty("Content-Type", "application/octet-stream");
			httpURLConnection.setRequestProperty("Charset", encoding);
			// 连接会话
			httpURLConnection.connect();
			// 建立输入流，向指向的URL传入参数
			DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());

			// 设置请求参数
			dos.write(FileHelper.getBytes(filePath));
			dos.flush();
			dos.close();
			// 获得响应状态
			int resultCode = httpURLConnection.getResponseCode();
			if (HttpURLConnection.HTTP_OK == resultCode) {
				System.out.println("上传成功！Http-Status = " + resultCode + " " + httpURLConnection.getResponseMessage());
				strBuffer = new StringBuffer();
				String readLine = new String();
				BufferedReader responseReader = new BufferedReader(
						new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
				while ((readLine = responseReader.readLine()) != null) {
					strBuffer.append(readLine);
				}
				responseReader.close();
				return true;
			}else{
				System.out.println("上传失败！Http-Status = " + resultCode + " " + httpURLConnection.getResponseMessage());
				strBuffer = new StringBuffer();
				String readLine = new String();
				BufferedReader responseReader = new BufferedReader(
						new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
				while ((readLine = responseReader.readLine()) != null) {
					strBuffer.append(readLine);
				}
				responseReader.close();
				return false;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/***
	 * 设置请求报头 HTTP Headers
	 *
	 * @param personalInfo
	 * @param httpURLConnection
	 * @return
	 */
	public static void initPostHeaders(HttpURLConnection httpURLConnection, ApplicantContractSignDto personalInfo, int type) {
		// 通过HmacSHA256算法获取签名信息，用以验签
		String signature = AlgorithmHelper.getXtimevaleSignature(setPostJSONStr(personalInfo, type), projectSecret, algorithm, encoding);
		// 设置Headers参数
		try {
			httpURLConnection.setDoOutput(true); // 需要输出
			httpURLConnection.setDoInput(true); // 需要输入
			httpURLConnection.setUseCaches(false); // 不允许缓存

			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Content-type", "application/json");
			httpURLConnection.setRequestProperty("X-timevale-project-id", projectId);
			httpURLConnection.setRequestProperty("X-timevale-signature", signature);
			httpURLConnection.setRequestProperty("signature-algorithm", algorithm);
			httpURLConnection.setRequestProperty("Content-Type", "application/json");
			httpURLConnection.setRequestProperty("Charset", encoding);

		} catch (IOException e) {
			e.printStackTrace();
			LOG.info("设置请求报头 HTTP Headers异常：" + e.getMessage());
		}
	}

	/***
	 * 设置POST请求参数
	 * @param type 0：存证确认函 1：存证合同 2：存证抵押 3：存证还款计划表 4：风险告知书
	 * @return
	 */
	public static String setPostJSONStr(ApplicantContractSignDto personalInfo, int type) {
		JSONObject eviObj = new JSONObject();
		String fileName;
		String serviceId;
		// 存证名称
//		String fileName = flag ? "_融资租赁合同" : "_确认函";
		switch (type) {
			case 1:
				fileName = "_融资租赁合同";
				serviceId = personalInfo.getSignServiceId();
				break;
			case 0:
				fileName = "_确认函";
				serviceId = personalInfo.getSignConfirmationServiceId();
				break;
			case 2:
				fileName = "_抵押合同";
				serviceId = personalInfo.getSignMortgageContractServiceId();
				break;
			case 3:
				fileName = "_还款计划表";
				serviceId = personalInfo.getSignRepaymentPlanServiceId();
				break;
			case 4:
				fileName = "_风险告知书";
				serviceId = personalInfo.getSignRiskNotificationServiceId();
				break;
			default:
				fileName = "_融资租赁合同";
				serviceId = personalInfo.getSignServiceId();
				break;
		}

		eviObj.put("eviName", personalInfo.getName() + "_" + personalInfo.getApplyNum() + fileName);
		eviObj.put("content", setContent(filePath));
		eviObj.element("eSignIds", setEsignIds(serviceId));
//		eviObj.element("bizIds", setBizIds());
		return eviObj.toString();
	}

	/***
	 * 设定内容
	 *
	 * @return
	 */
	public static String setContent(String filePath) {
		Map<String, String> fileInfos = FileHelper.getFileInfo(filePath);
		JSONObject contentObj = new JSONObject();
		// 内容描述，如文件名等
		contentObj.put("contentDescription", fileInfos.get("FileName"));
		// 内容数据长度，单位：字节，如文件大小
		contentObj.put("contentLength", fileInfos.get("FileLength"));
		// 内容字节流MD5的Base64编码值，如获取文件MD5后再Base64编码
		contentObj.put("contentBase64Md5", AlgorithmHelper.getContentMD5(filePath));
		return contentObj.toString();
	}

	/***
	 * eSignIds eSignIds 电子签名证据id,0-电子签名，1-时间戳；电子签名和时间戳Ids至少包含一个值
	 *
	 * @return
	 */
	public static String setEsignIds(String signServiceId) {

		IdsBean eSignIdsBean1 = new IdsBean();
		// 0 代表使用e签宝的电子签名服务
		eSignIdsBean1.setType("0");
		// value值就是 e签宝 SDK电子签名后返回的 signServiceId 签署记录id
		eSignIdsBean1.setValue(signServiceId);

//		IdsBean eSignIdsBean2 = new IdsBean();
//		// 1 代表使用e签宝的时间戳服务
//		eSignIdsBean2.setType("1");
//		// value值就是 e签宝时间戳服务返回的 timestampId 时间戳数据记录ID
//		eSignIdsBean2.setValue("456987-12f11230123-12ojawfowjfoj-afweafawfe");

		List<IdsBean> eSignIds = new ArrayList<IdsBean>();
		eSignIds.add(eSignIdsBean1);
//		eSignIds.add(eSignIdsBean2);
		return setIds(eSignIds);
	}

	/***
	 * bizIds不是必须的，允许保全未使用e签宝实名认证服务的文档； bizIds e签宝业务id列表中type = 0
	 * 的value就是调用e签宝实名认证成功返回的serviceId
	 *
	 * @return
	 */
	public static String setBizIds() {
		IdsBean bizIdsBean1 = new IdsBean();
		// 0 代表使用了e签宝的实名认证服务
		bizIdsBean1.setType("0");
		// value值就是调用e签宝实名认证服务返回的serviceId 实名认证请求ID
		bizIdsBean1.setValue("3684eb08-f089-49f9-b5e3-bfa251669fe6");
		List<IdsBean> bizIds = new ArrayList<IdsBean>();
		bizIds.add(bizIdsBean1);
		return setIds(bizIds);
	}

	/***
	 * 设置Ids
	 *
	 * @return
	 */
	public static String setIds(List<IdsBean> list) {
		JSONArray idsArray = JSONArray.fromObject(list);
		return idsArray.toString();
	}

}
