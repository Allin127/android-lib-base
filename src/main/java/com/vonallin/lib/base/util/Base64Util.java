package com.vonallin.lib.base.util;


import android.util.Base64;

public class Base64Util {
    public static String encode(byte[] datas) throws Exception{
            return new String(Base64.encode(datas,Base64.NO_WRAP), "UTF-8");
    }
    public static byte[] decode(byte[] datas) throws Exception{
        return Base64.decode(datas, Base64.NO_WRAP);
    }
    public static byte[] decode(String source) throws Exception{
        return Base64.decode(source,Base64.NO_WRAP);
    }
}
