package com.vonallin.lib.base.security;

import java.security.MessageDigest;

public class MD5Util {
    public static byte[] encrypt(String source) throws Exception{
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(source.getBytes());
        return md5.digest();
    }
}
