package com.vonallin.lib.base.security;

import com.vonallin.lib.base.util.Base64Util;
import com.vonallin.lib.base.util.DateUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Enumeration;

public class LXRSAKeyUtil {

    public static String KEY_ALGORITHM = "RSA";

    public  static RSAKeyStore readKeyPair(String keyStorePath,String password,String keyName) throws Exception {
        //KeyStore存储对象，可以用jks文件，也适用于keystore格式私钥文件
        KeyStore keyStore = KeyStore.getInstance("jks");
        //读取文件
        FileInputStream fis = new FileInputStream(keyStorePath);
        keyStore.load(fis, password.toCharArray());
        showJksInfo(keyStore);
        return RSAKeyStore.builder()
                .publicKey((RSAPublicKey)keyStore.getCertificate(keyName).getPublicKey())
                .privateKey((RSAPrivateKey) keyStore.getKey(keyName, password.toCharArray()))
                .build();
    }

    private static void showJksInfo(KeyStore keyStore) throws Exception {
        Enumeration<String> enums = keyStore.aliases();
        while (enums.hasMoreElements()) {
            String o = enums.nextElement();
            System.out.println("-->" + o);
            if (keyStore.getCertificate(o).getType().equals("X.509")) {
                X509Certificate certificate = (X509Certificate) keyStore.getCertificate(o);
                System.out.println("  BasicConstraints=" + certificate.getBasicConstraints());
                System.out.println("  IssuerDN=" + certificate.getIssuerDN());
                System.out.println("  IssuerX500Principal=" + certificate.getIssuerX500Principal());
                System.out.println("  SerialNumber=" + certificate.getSerialNumber());
                System.out.println("  SigAlgName=" + certificate.getSigAlgName());
                System.out.println("  SigAlgOID=" + certificate.getSigAlgOID());
                System.out.println("  SubjectDN=" + certificate.getSubjectDN());
                System.out.println("  SubjectX500Principal=" + certificate.getSubjectX500Principal());
                System.out.println("  Type=" + certificate.getType());
                System.out.println("  Version=" + certificate.getVersion());
//                System.out.println("  Signature="+ new BigInteger(certificate.getSignature()).toString(16).toUpperCase());
//                System.out.println("  Encoded="+ new BigInteger(certificate.getEncoded()).toString(16).toUpperCase());
//                System.out.println("  SigAlgParams="+ certificate.getSigAlgParams());
//                System.out.println("  TBSCertificate="+ new BigInteger(certificate.getTBSCertificate()).toString(16).toUpperCase());
                System.out.println("  startAt=" + DateUtil.formatDateTime(certificate.getNotBefore()));
                System.out.println("  endAt=" + DateUtil.formatDateTime(certificate.getNotAfter()));
                String encoded = Base64Util.encode(certificate.getEncoded());
                System.out.println("-----BEGIN CERTIFICATE-----\r\n");    //非必须
                System.out.println(encoded);
                System.out.println("\r\n-----END CERTIFICATE-----");  //非必须
            }
        }
    }


    /*自己指定module和exponent*/
    public static RSAPublicKey generateRSAPublicKey(String hexModulus) throws Exception {
        KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM,new BouncyCastleProvider());
        BigInteger n = new BigInteger(hexModulus, 16);
        BigInteger e = new BigInteger("10001", 16);
        RSAPublicKeySpec spec = new RSAPublicKeySpec(n, e);
        return (RSAPublicKey)factory.generatePublic(spec);
    }

    public static RSAPrivateKey generateRSAPrivateKey(BigInteger modulus,
                                                      BigInteger privateExponent) throws Exception {
        KeyFactory keyFac = null;
        try {
            keyFac = KeyFactory.getInstance(KEY_ALGORITHM,new BouncyCastleProvider());
        } catch (NoSuchAlgorithmException ex) {
            throw new Exception(ex.getMessage());
        }
        RSAPrivateKeySpec priKeySpec = new RSAPrivateKeySpec(
                modulus, privateExponent);

        try {
            return (RSAPrivateKey) keyFac.generatePrivate(priKeySpec);
        } catch (InvalidKeySpecException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public static RSAPrivateKey generateRSAPrivateKey(String hexModulus,
                                                      String hexPrivateExponent) throws Exception {
        return generateRSAPrivateKey(new BigInteger(
                hexModulus,16),new BigInteger(
                hexPrivateExponent,16));
    }

}



