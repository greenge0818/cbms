package com.prcsteel.platform.acl.utils;

import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

/**
 * Created by rolyer on 15-1-15.
 */
public class EncryptUtil {
	
	 private EncryptUtil() {
	 }
	
	private static Logger logger = Logger.getLogger(EncryptUtil.class);

    public static String ldapMD5(String str) {
        return ldapEncrypt("MD5", str);
    }

    public static String ldapEncrypt(String algorithm, String str) {
        String sEncrypted = str;
        if ((str != null) && (str.length() > 0)) {
            boolean bMD5 = "MD5".equalsIgnoreCase(algorithm);
            boolean bSHA = "SHA".equalsIgnoreCase(algorithm) 
                    || "SHA1".equalsIgnoreCase(algorithm) 
                    || "SHA-1".equalsIgnoreCase(algorithm);
            if (bSHA || bMD5) {
                String sAlgorithm = "MD5";
                if (bSHA) {
                    sAlgorithm = "SHA";
                }
                try {
                    MessageDigest md = MessageDigest.getInstance(sAlgorithm);
                    md.update(str.getBytes("UTF-8"));
                    sEncrypted = "{" + sAlgorithm + "}" + (new BASE64Encoder()).encode(md.digest());
                } catch (Exception e) {
                    sEncrypted = null;
                    logger.error(e, e);
                }
            }
        }
        return sEncrypted;
    }


    /**
     * md5 encrypt
     * @param str
     * @return
     */
    public static String MD5(String str) {
        return encrypt("md5", str);
    }


    /**
     * sha encrypt
     * @param str
     * @return
     */
    public static String SHA(String str) {
        return encrypt("sha-1", str);
    }

    /**
     *
     * @param algorithm
     * @param str
     * @return
     */
    private static String encrypt(String algorithm, String str) {
        if (algorithm == null || "".equals(algorithm.trim())) {
            algorithm = "md5";
        }
        String encryptText = null;
        try {
            MessageDigest m = MessageDigest.getInstance(algorithm);
            m.update(str.getBytes("UTF8"));
            byte s[] = m.digest();
            return hex(s);
        } catch (NoSuchAlgorithmException e) {
        	logger.error(e, e);
        } catch (UnsupportedEncodingException e) {
        	logger.error(e, e);
        }

        return encryptText;
    }

    /**
     *
     * @param arr
     * @return
     */
    private static String hex(byte[] arr) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < arr.length; ++i) {
            sb.append(Integer.toHexString((arr[i] & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }
}
