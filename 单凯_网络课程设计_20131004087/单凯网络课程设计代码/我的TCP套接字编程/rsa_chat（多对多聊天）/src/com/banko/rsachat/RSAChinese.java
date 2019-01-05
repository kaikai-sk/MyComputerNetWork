package com.banko.rsachat;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RSAChinese {
	
	public static final String E = "65537";
	public static final String N = "6843313101291659592693028390370620217135751844805285054508177640036800872121365560329363814663088712048917867411880982302294734169414491505385852183253477";
	public static final String D = "1094625192499511230453042046725593355451639327236428326386762076392050041082846439448470498560895703615113748666115507592108308465632534638114424404205633";
	public static final String P = "100709084290345694865330116906846965717425950527844819314656941768325736751797";
	public static final String Q = "67951299026434323043402172046339884185876196849192897542415639826146862563441";
	public static String[] key = {E, N, D, P, Q};	
	
	/**
	 * ������Կ����������ָ�����ܺͽ����㷨ΪRSA
	 * @param keylen
	 * @return
	 */
	public static String[] createKey(int keylen) {// ������Կ����
		String[] output = new String[5]; // �����洢��Կ��e n d p q
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(keylen); // ָ����Կ�ĳ��ȣ���ʼ����Կ��������
			KeyPair kp = kpg.generateKeyPair(); // ������Կ��
			RSAPublicKey puk = (RSAPublicKey) kp.getPublic();
			RSAPrivateCrtKey prk = (RSAPrivateCrtKey) kp.getPrivate();
			BigInteger e = puk.getPublicExponent();
			BigInteger n = puk.getModulus();
			BigInteger d = prk.getPrivateExponent();
			BigInteger p = prk.getPrimeP();
			BigInteger q = prk.getPrimeQ();
			output[0] = e.toString();
			output[1] = n.toString();
			output[2] = d.toString();
			output[3] = p.toString();
			output[4] = q.toString();
		} catch (NoSuchAlgorithmException ex) {
			Logger.getLogger(RSAChinese.class.getName()).log(Level.SEVERE, null, ex);
		}
		return output;
	}

	/**
	 * ������RSA��Կ�а���������������Ϣ��e��n��������������m,�������ĵĹ�ʽ��m��e�η�����n��ģ��
	 * @param clearText ����
	 * @param eStr ��Կ
	 * @param nStr
	 * @return
	 */
	public static String encrypt(String clearText, String eStr, String nStr) {   //����
		String secretText = new String();
		
		try {
			clearText = URLEncoder.encode(clearText,"GB18030"); 	
			byte text[]=clearText.getBytes("GB18030");//���ַ���ת����byte�������飬ʵ���Ǹ����ַ��Ķ�������ʽ
			BigInteger mm=new BigInteger(text);//�����ƴ�ת��Ϊһ��������
			clearText = mm.toString();
			
			BigInteger e = new BigInteger(eStr);
			BigInteger n = new BigInteger(nStr);
			byte[] ptext = clearText.getBytes("GB18030"); // ��ȡ���ĵĴ�����
			BigInteger m = new BigInteger(ptext);
			BigInteger c = m.modPow(e, n);
			secretText = c.toString();
		} catch (UnsupportedEncodingException ex) {
			Logger.getLogger(RSAChinese.class.getName()).log(Level.SEVERE, null, ex);
		}
		return secretText;
	}

	/**
	 * ����
	 * @param secretText ����
	 * @param dStr ˽Կ
	 * @param nStr
	 * @return
	 */
	public static String decrypt(String secretText, String dStr, String nStr) {
		StringBuffer clearTextBuffer = new StringBuffer();
		BigInteger d = new BigInteger(dStr);// ��ȡ˽Կ�Ĳ���d,n
		BigInteger n = new BigInteger(nStr);
		BigInteger c = new BigInteger(secretText);
		BigInteger m = c.modPow(d, n);// ��������
		byte[] mt = m.toByteArray();// �������Ķ�Ӧ���ַ��������
		for (int i = 0; i < mt.length; i++) {
			clearTextBuffer.append((char) mt[i]);
		}
		String temp = clearTextBuffer.toString();//tempΪ���ĵ��ַ�����ʽ
		BigInteger b = new BigInteger(temp);//bΪ���ĵ�BigInteger����
		byte[] mt1=b.toByteArray();
		
		try {
			String clearText = (new String(mt1,"GB18030"));
			clearText=URLDecoder.decode(clearText,"GB18030"); 
			return clearText;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	public static void main(String[] args) {
		RSAChinese rsa = new RSAChinese();
		String[] str = rsa.createKey(512);
		String clearText = "123abc����";
		
		String secretText = RSAChinese.encrypt(clearText, key[0], key[1]);
		System.out.println("�����ǣ�" + clearText);
		System.out.println("�����ǣ�" + secretText);
		System.out.println("���ܺ�������ǣ�" + RSAChinese.decrypt(secretText, key[2], key[1]));
		
	}
}