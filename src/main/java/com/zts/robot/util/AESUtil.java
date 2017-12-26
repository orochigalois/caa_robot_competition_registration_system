package com.zts.robot.util;

import java.io.IOException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * 3DES加密工具类
 * 
 * @author liufeng
 * @date 2012-10-11
 */
public class AESUtil {

	// 密钥
	// private final static String secretKey = "liuyunqiang@lx100$#365#$";
	// 向量
	private final static String iv = "01234567";
	// 加解密统一使用的编码方式
	private final static String encoding = "utf-8";

	/**
	 * 3DES加密
	 * 
	 * @param plainText
	 *            普通文本
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String plainText, String secretKey)
			throws Exception {
		Key deskey = null;
		DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		deskey = keyfactory.generateSecret(spec);

		Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
		byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));
		return Base64.encode(encryptData);
	}

	/**
	 * 3DES解密
	 * 
	 * @param encryptText
	 *            加密文本
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String encryptText, String secretKey)
			throws Exception {
		Key deskey = null;
		DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		deskey = keyfactory.generateSecret(spec);
		Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
		cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

		byte[] decryptData = cipher.doFinal(Base64.decode(encryptText));

		return new String(decryptData, encoding);
	}

	/**
	 * 生成密钥 自动生成AES128位密钥 传入保存密钥文件路径 filePath 表示文件存储路径加文件名；例如d:\aes.txt
	 * 
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public static String getAutoCreateAESKey() throws NoSuchAlgorithmException {
		KeyGenerator kg = KeyGenerator.getInstance("AES");
		kg.init(192);// 要生成多少位，只需要修改这里即可128, 192或256
		SecretKey sk = kg.generateKey();
		byte[] b = sk.getEncoded();
		return b.toString();
	}

	/**
	 * 生成随机密码
	 * 
	 * @param pwd_len
	 *            生成的密码的总长度
	 * @return 密码的字符串
	 */
	public static String genRandomNum(int pwd_len) {
		// 35是因为数组是从0开始的，26个字母+10个数字
		final int maxNum = 36;
		int i; // 生成的随机数
		int count = 0; // 生成的密码的长度
		char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
				'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
				'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		StringBuffer pwd = new StringBuffer("");
		Random r = new Random();
		while (count < pwd_len) {
			// 生成随机数，取绝对值，防止生成负数，
			i = Math.abs(r.nextInt(maxNum)); // 生成的数最大为36-1
			if (i >= 0 && i < str.length) {
				pwd.append(str[i]);
				count++;
			}
		}
		return pwd.toString();
	}

	public static void main(String[] args) throws Exception {
		// [B@59c9b9ca
		String content = "YK000000000002015-12-08 10:57:34";
		// // String password = getAutoCreateAESKey();
		String password = "v7l27ffb9xamti4igqa308ju";
		// //String password = genRandomNum(24);
		// System.out.println("Password：" + password);
		// // 加密
		// System.out.println("加密前：" + content);
		// // content = new String(content.getBytes("ISO-8859-1"), "UTF-8");
		String encryptResult1 = encrypt(content, password);
		// encryptResult =
		// "QI6AcBRSxeahkogeNROMBD2aHauc1lfNuXkDA45OXB6pdpdKAe3YwSrbPPrp/MZv7rbpeMDSnfD+C8QMF1IIOA==";
		System.out.println("加密后数据：" + encryptResult1);

		String encryptResult = "7liQ+keaYUgX13s5FSGC0+fgAyg0mlrsuwRVTDDThI5i0U9NmlPUXA==";
		String decryptResult = decrypt(encryptResult, password);

		// 解密
		System.out.println("解密后：" + decryptResult);
	}

}