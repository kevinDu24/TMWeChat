package com.tm.wechat.utils.commons;
		import javax.crypto.Cipher;
		import javax.crypto.SecretKey;
		import javax.crypto.SecretKeyFactory;
		import javax.crypto.spec.DESKeySpec;
		import javax.crypto.spec.IvParameterSpec;
        import org.apache.commons.codec.binary.Base64;
		import jodd.util.StringUtil;


public class TaiMengDES {
	private static boolean checkKey(String key) {
		if (!StringUtil.isBlank(key) && key.length() == 8) {
			return true;
		}
		return false;
	}

	// 解密数据
	public static String decrypt(byte[] message, byte[] key, byte[] iv)
			throws Exception {
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		// iv 向量
		IvParameterSpec ivParam = new IvParameterSpec(iv);
		cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParam);
		byte[] retByte = cipher.doFinal(message);
		return new String(retByte, "UTF-8");
	}

	// 解密数据
	public static String decrypt(String message, byte[] key, byte[] iv)
			throws Exception {
		byte[] bytesrc = message.getBytes();
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		// iv 向量
		IvParameterSpec ivParam = new IvParameterSpec(iv);
		cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParam);
		byte[] retByte = cipher.doFinal(bytesrc);
		return new String(retByte, "UTF-8");
	}

	public static String encryptString(String message, byte[] key, byte[] iv)
			throws Exception {
		byte[] ret = encrypt(message, key, iv);
		return new String(ret, "UTF-8");
	}

	public static byte[] encrypt(String message, byte[] key, byte[] iv)
			throws Exception {
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec ivParam = new IvParameterSpec(iv);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParam);
		return cipher.doFinal(message.getBytes("UTF-8"));
	}

	// 解密数据
	public static String decrypt(String message, String key, String iv)
			throws Exception {
		// 密钥只能是8位
		if (!checkKey(key)) {
			return null;
		}
		byte[] bytesrc = message.getBytes();
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

		// iv 向量
		IvParameterSpec ivParam = new IvParameterSpec(iv.getBytes("UTF-8"));

		cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParam);

		byte[] retByte = cipher.doFinal(bytesrc);
		return new String(retByte, "UTF-8");
	}

	public static byte[] encrypt(String message, String key, String iv)
			throws Exception {
		// 密钥只能是8位
		if (!checkKey(key)) {
			return null;
		}
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

		DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));

		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec ivParam = new IvParameterSpec(iv.getBytes("UTF-8"));
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParam);

		return cipher.doFinal(message.getBytes("UTF-8"));
	}

	// 解密数据
	public static String decryptBase64(String message, String key)
			throws Exception {
		// 密钥只能是8位
		if (!checkKey(key)) {
			return null;
		}
		byte[] bytesrc = decodeBase64(message);
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));

		cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

		byte[] retByte = cipher.doFinal(bytesrc);
		return new String(retByte, "UTF-8");
	}

	public static String encryptBase64(String message, String key)
			throws Exception {
		// 密钥只能是8位
		if (!checkKey(key)) {
			return null;
		}
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

		DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));

		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

		return encodeBase64(cipher.doFinal(message.getBytes("UTF-8")));
	}

	public static byte[] decodeBase64(String ss) throws Exception {
		// BASE64Decoder base64Decoder = new BASE64Decoder();
		// byte[] pasByte = base64Decoder.decodeBuffer(ss);
		// return pasByte;
		return Base64.decodeBase64(ss.getBytes("UTF-8"));
	}

	public static String encodeBase64(byte b[]) {
		// BASE64Encoder base64Encoder = new BASE64Encoder();
		// return base64Encoder.encode(b);
		return Base64.encodeBase64URLSafeString(b);
	}

//	public static void main(String[] args) throws Exception {
//		//加密
//		String a = encryptBase64("{  \"carBillId\":\"191\",\"price\": \"342000\",\"years\":\"24\",\"dateOfProduction\":\"20160209\",\"resultReason\":\"\"}", CommonUtils.encryptKey);
//		System.out.println(a);
//		getXFWS(80000,3);
//		double d = Math.round(2.45);
//		System.out.println(d);
//		System.out.println(new BigDecimal(Math.round(getXFWS(80000,3))));
//	}
}

