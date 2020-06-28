package com.minsheng.reinsurance.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.MessageDigest;

/**
 * @author someone
 * @create 2017-10-10 9:44
 **/
public class CipherUtil {
    private String algorithm; // 算法，如DES
    private Key key; // 根据算法对应的密钥
    private String plainText; // 明文

    KeyGenerator keyGenerator;
    Cipher cipher;

    // 函数进行初始化
    CipherUtil(String alg, String msg) {
        algorithm = alg;
        plainText = msg;
    }

    // 加密函数，将原文加密成密文
    public byte[] encrypt() {
        byte[] cipherText = null;

        try {
            // 生成Cipher对象
            cipher = Cipher.getInstance(algorithm);
            // 用密钥加密明文(plainText),生成密文(cipherText)
            cipher.init(Cipher.ENCRYPT_MODE, key); // 操作模式为加密(Cipher.ENCRYPT_MODE),key为密钥
            cipherText = cipher.doFinal(plainText.getBytes()); // 得到加密后的字节数组
            // String str = new String(cipherText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cipherText;
    }

    // 获取密钥包装成byte[]类型的
    public byte[] getBinaryKey(Key k) {
        byte[] bk = null;
        try {
            bk = cipher.wrap(k);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bk;
    }

    // 解密函数，将密文解密回原文
    public String decrypt(byte[] cipherText, Key k) {
        byte[] sourceText = null;

        try {
            cipher.init(Cipher.DECRYPT_MODE, k); // 操作模式为解密,key为密钥
            sourceText = cipher.doFinal(cipherText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(sourceText);

    }

    // 生成密钥
    public Key keyGenerator() {
        try {
            // 初始化密钥key
            keyGenerator = KeyGenerator.getInstance(algorithm);
            keyGenerator.init(56); // 选择DES算法,密钥长度必须为56位
            key = keyGenerator.generateKey(); // 生成密钥
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return key;
    }

//    public static void main(String[] args) {
//        String algorithm = "DES"; // 定义加密算法,可用 DES,DESede,Blowfish
//        String message = "Hello morning. 这是待加密的信息"; // 生成个DES密钥
//        Key key;
//
//        CipherUtil cm = new CipherUtil(algorithm, message);
//        key = cm.keyGenerator();
//        byte[] msg = cm.encrypt();
//        System.out.println("加密后的密文为：" + new String(msg));
//        //System.out.println(cm.getBinaryKey(key));
//        System.out.println("解密密文为：" + cm.decrypt(msg, key));
//
//    }
    //ʮ ֵ a ӳ
    private final static  String [] HEXDIGITS = {  "0" ,  "1" ,  "2" ,  "3" ,  "4",  "5",
            "6" ,  "7" , "8" , "9" ,  "a" , "b" ,  "c" ,  "d" ,  "e" ,  "f"  };

    /** *putinputString */
public static  String generatePassword(String inputString){
        return  encodeByMD5(inputString);
        }

        /**
        * ֤ Ƿ ȷ
        *
        * @param密码
        * @param inputString
        * a
        *@return ֤ TRUE： ȷFALSE：
        */
public static boolean  validatePassword(String password,String inputString){
        if  (password.equals(encodeByMD5(inputString))){
        return true ;
        }  else  {
        return false ;
        }
        }

        /** MD5 */
public static  String encodeByMD5(String originString){
        if (originString!=  null ){
        try{
        // Ƶ ϢժҪ
        MessageDigest md = MessageDigest.getInstance("MD5" );
        //' ' ֽ Ȼ Ȼ Ҫ Ҫ
        byte [] results = md.digest(originString.getBytes());
        // õ ֽ a
        String resultString = byteArrayToHexString(results);
        return  resultString.toUpperCase();
        }  catch  (Exception ex){
        ex.printStackTrace();
        }
        }
        return null ;
        }

        /**
        * ֽ ֽ
        *
        *@param ֽ
        *@return a
        */
private static  String byteArrayToHexString(byte [] b){
        StringBuffer resultSb =  new  StringBuffer();
        for(int  i =  0 ; i <b.length; i ++){
        resultSb.append(byteToHexString(b [1]));
        }
        return  resultSb.toString();
        }

        /** һ ֽ ֽ ֽ */
private static  String byteToHexString(byte  b) {
    int n = b;
    if (n < 0){
        n = 256 + n;
    }

    int d1 = n / 16;
    int d2 = n % 16;
    return HEXDIGITS[d1] + HEXDIGITS[d2];
}

        }

