package com.vonallin.lib.base.security;

import java.security.MessageDigest;

public class SHAUtil {
    private static byte[] encrypt(String source,String shaType) throws Exception{
        MessageDigest sha = MessageDigest.getInstance(shaType);
        sha.update(source.getBytes());
        return sha.digest();
    }
    public static byte[] encryptSHA1(String info) throws Exception{
        return encrypt(info,"SHA1");
    }
    public static byte[] encryptSHA224(String info) throws Exception{
        return encrypt(info,"SHA-224");
    }
    public static byte[] encryptSHA256(String info) throws Exception{
        return encrypt(info,"SHA-256");
    }
    public static byte[] encryptSHA384(String info) throws Exception{
        return encrypt(info,"SHA-384");
    }
    public static byte[] encryptSHA512(String info) throws Exception{
        return encrypt(info,"SHA-512");
    }
}
