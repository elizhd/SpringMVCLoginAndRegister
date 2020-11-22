package edu.usts.eie.tools;


import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSATools {
    private static Map<Integer, String> keyMap = new HashMap<Integer, String>();


    public static void main(String[] args) throws Exception {
        genKeyPair();
        String message = "This is a test designed by zhaohaodong";
        System.out.println("The randomly generated public key is: " + keyMap.get(0));
        System.out.println("The randomly generated private key is: " + keyMap.get(1));
        String messageEn = encrypt(message, keyMap.get(0));
        System.out.println(message + "\tThe encrypted string is:" + messageEn);
        String messageDe = decrypt(messageEn, keyMap.get(1));
        System.out.println("The decrypted string is:" + messageDe);
    }


    public static Map<Integer, String> genKeyPair() throws NoSuchAlgorithmException {
        // The KeyPairGenerator class is used to generate public and private key pairs, and generate objects based on the RSA algorithm
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // Initialize the key pair generator, the key size is 96-1024 bits
        keyPairGen.initialize(1024, new SecureRandom());
        // Generate a key pair and save it in keyPair
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // Get private key
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // Get public key
        // Base64 encode, get public key string
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        // System.out.println("------------------- " + new String(publicKey.getEncoded()));
        // Base64 encode, get private key string
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
        keyMap.put(0, publicKeyString);  //0 represents public key
        keyMap.put(1, privateKeyString);  //1 represents private key
        Map<Integer, String> rKeyMap = keyMap;
        return rKeyMap;
    }


    public static String encrypt(String str, String publicKey) throws Exception {
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        // RSA encryption
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }


    public static String decrypt(String str, String privateKey) throws Exception {
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        // base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        // RSA decryption
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }

}

