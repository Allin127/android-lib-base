package com.vonallin.lib.base.security;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class HMACUtil {
    /**
     * HMAC加密
     * @return
     * @throws Exception
     */
    public static byte[] encryptionHMAC(String source,String key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA512");
        Mac mac = Mac.getInstance("HmacSHA512");
        mac.init(secretKey);
        mac.update(source.getBytes());
        return mac.doFinal();
    }

}
