package com.vonallin.lib.base.security;

import org.bouncycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;


//某厂特殊的RSA加密
public class LXRSAUtil {
    /**
     * 加密规则
     * 1. 将密码的长度进行16进制转换放在密码最前端
     * 2. 将源密码的每个字节进行转换
     * 3. 然后调用随即数，补充满密码128长度
     * 根据规则，将转换后的源密码进行截取操作，16进制下前四位是表示用户密码长度，
     * 2位截取一次头两位是十位单位，后2位是个位单位，且都是在数字48上相加所得结果 换算得到用户密码长度后，进行密码长度的截取，最后对密码进行转换操作
     */

    public static final int MAX_ENCRYPT_BLOCK = 117;

    private static byte[] convertPass(String pass) {
        byte[] passBytes = pass.getBytes();
//        String s = new BigInteger(passBytes).toString(16);
        String s = new String(Hex.encode(passBytes));
        String tenLength = new BigInteger(String.valueOf(passBytes.length / 10 + 48)).toString(16);
        String digLength = new BigInteger(String.valueOf(passBytes.length % 10 + 48)).toString(16);
        String targetEncode = new String(tenLength + digLength + s);
        byte[] encodeData = new BigInteger(targetEncode, 16).toByteArray();
        if (encodeData.length > MAX_ENCRYPT_BLOCK) {
            throw new RuntimeException("加密内容太长！");
        }
        byte[] res = new byte[MAX_ENCRYPT_BLOCK];
        for (int i = 0; i < encodeData.length; i++) {
            res[i] = encodeData[i];
        }
        //增加随机码
        for (int i = encodeData.length; i < MAX_ENCRYPT_BLOCK; i++) {
            res[i] = (byte) (Math.random() * 10 % 10);
        }
        return res;
    }

    // converPass的逆操作
    public static String revertPass(byte[] pass) {
        String strResult = new String(Hex.encode(pass));
//        String strResult = new BigInteger(pass).toString(16);
        // 获取用户密码长度的十位单位，并进行
        String len1 = strResult.substring(0, 2);
        // 对十位单位进行16进制转换
        int tenth = Integer.parseInt(len1, 16);
        // 对十位进行减去48基数操作
        int leni1 = tenth - 48;
        // 获取用户密码长度的个位数
        int leni2 = Integer.parseInt(strResult.substring(2, 4), 16) - 48;
        // 换算用户密码长度 (十位*10+个位)
        int pwdLen = leni1 * 10 + leni2;
        // 截取用户密码
        String passInitial = strResult.substring(4, pwdLen * 2 + 4);
        // 对用户密码进行转换处理
        passInitial = new String(Hex.decode(passInitial.getBytes()));
//        BigInteger bgint = new BigInteger(passInitial, 16);
//        return new String(bgint.toByteArray());
        return passInitial;
    }

    //目前实际使用的加密组合
    public static String decryptTransferData(PrivateKey privateKey, String encryptedText) {
        //这里的Hex工具使用加密套件的，不要自己写，会有异常
        return revertPass(RSAUtil.decrypt(privateKey, Hex.decode(encryptedText)));
    }

    public static String decryptTransferData(PrivateKey privateKey, String encryptedText,String alg) {
        //这里的Hex工具使用加密套件的，不要自己写，会有异常
        return revertPass(RSAUtil.decrypt(privateKey, Hex.decode(encryptedText),alg));
    }

    public static String encryptTransferData(PublicKey publicKey, String origText)  throws Exception {
        //这里的Hex工具使用加密套件的，不要自己写，会有异常
        return new String(Hex.encode(RSAUtil.encrypt(publicKey,convertPass(origText)))).toString();
    }


}
