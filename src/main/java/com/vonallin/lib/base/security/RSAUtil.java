package com.vonallin.lib.base.security;

import com.vonallin.lib.base.util.Base64Util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class RSAUtil {

    //判断是否支持BC
//    static {
//        boolean bcExist = false;
//        for (Provider provider : Security.getProviders()) {
//            if (provider.getName().equals(BouncyCastleProvider.PROVIDER_NAME)) {
//                bcExist = true;
//                break;
//            }
//        }
//        if (!bcExist) {
//            Security.addProvider(new BouncyCastleProvider());
//        }
//    }

    public static String decrypt(PrivateKey privateKey, String base64Msg) throws Exception {
        byte[] raw = Base64Util.decode(base64Msg.getBytes());
        return new String(decrypt(privateKey, raw));
    }

    public static byte[] decrypt(PrivateKey privateKey, byte[] raw) {
        return decrypt(privateKey,raw,"RSA/None/PKCS1Padding");
    }

    public static byte[] decrypt(PrivateKey privateKey, byte[] raw,String alg) {
        InputStream in = null;
        byte[] result;
        try {
            Cipher cipher = Cipher.getInstance(alg, new BouncyCastleProvider());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            result =cipher.doFinal(raw);
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        } finally {
            try {
                if (in != null) in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    /**
     * 加密
     * 返回Base64
     */
    public static String encrypt(PublicKey publicKey, String strData) throws Exception {
        byte[] b = encrypt(publicKey, strData.getBytes());
        return Base64Util.encode(b);
    }

    /**
     * 根据公私钥加密
     */
    public static byte[] encrypt(PublicKey publicKey, byte[] data) throws Exception {
            Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding", new BouncyCastleProvider());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(data);
    }

    /*导入PKCS1的RSA密钥转成PKCS8的，H5前端默认实现*/
    public static PrivateKey generatePKCS1PrivateKey(String pkcs1Base64Key) throws Exception {
        // 对密钥解密
        byte[] keyBytes = Base64Util.decode(pkcs1Base64Key);
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA",new BouncyCastleProvider());
        return keyFactory.generatePrivate(pkcs8KeySpec);
    }

    /*导入PKCS1的RSA密钥转成PKCS8的，H5前端默认实现*/
    public static PublicKey generatePKCS1PublicKey(String pkcs1Base64Key) throws Exception {
        // 对密钥解密
        byte[] keyBytes = Base64Util.decode(pkcs1Base64Key);
        KeyFactory factory = KeyFactory.getInstance("RSA",new BouncyCastleProvider());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        return factory.generatePublic(keySpec);
    }

}
