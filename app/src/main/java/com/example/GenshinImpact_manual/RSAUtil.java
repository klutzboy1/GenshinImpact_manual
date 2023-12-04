package com.example.GenshinImpact_manual;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import javax.crypto.Cipher;

public class RSAUtil {
    // 生成RSA密钥对
    public static String publicKeyString = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAur8cdnFz/M1vsHXQujin/7Mwwd0+d5k3lfUvrWs2LIGNy+sJuWT9PAdh0/o+XA5FcacDSGFiAr/KxpDqUUxvPRwFd2L6Lwpr+KeeKjhJ5oZy9veu/9bGUwA7iJQzc35CXmLTlsJk0XWADOOVb2bCfWDjBW//aotqQ6u7T8F67M+bGJtNWCVEWR3Wbtw90K7D1Z+GxWRyYJFmAnI/a/zaYPi7HYZpcbFngKBbZ/1CL6HTa7AXOW/VkWFMRpwi/uDN4Vyok0USiKNOK54s1O2wJZsJtkVQa4nf7BpMDAfa84eF5HPryyHnibgop73hXS1wmRjaZbcTqWiTbWnttEecDwIDAQAB";

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // 选择密钥大小，一般2048位足够安全
        return keyPairGenerator.generateKeyPair();
    }

    // 将生成的KeyPair转化为HashMap
    public static HashMap<String, String> getKeyPairHashMapEdition(KeyPair keyPair){
        HashMap<String, String> map = new HashMap<>();
        map.put("public", keyToString(keyPair.getPublic()));
        map.put("private", keyToString(keyPair.getPrivate()));
        return map;
    }

    // 使用公钥加密字符串
    public static String encrypt(String originalText, String publicKeyString) throws Exception {
        PublicKey publicKey = stringToPublicKey(publicKeyString);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(originalText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // 使用私钥解密字符串
    public static String decrypt(String encryptedText, String privateKeyString) throws Exception {
        PrivateKey privateKey = stringToPrivateKey(privateKeyString);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }

    // 将Key对象转换为字符串表示
    private static String keyToString(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    // 将字符串表示的公钥转换为PublicKey对象
    private static PublicKey stringToPublicKey(String publicKeyString) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    // 将字符串表示的私钥转换为PrivateKey对象
    private static PrivateKey stringToPrivateKey(String privateKeyString) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyString);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }
}