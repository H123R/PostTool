package com.jax.PostTool.core.util;

/**
 * @author jax
 * @date 3/22/2023 2:06 PM
 */
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class AESUtils {

    private static final String KEY_ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final String DEFAULT_KEY256 = "7hlRNK4z4TClegkkHa05C/pI2MmHkBd6uVHr6gzLSlM=";
    private static final String RANDOM_TYPE = "SHA1PRNG";

    /**
     * 默认密钥大小<br/>
     */
    private static final int DEFAULT_KEY_SIZE = 128;

    /**
     * 256位密钥大小<br/>
     */
    private static final int KEY_SIZE_256 = 256;

    public static String genKey() throws NoSuchAlgorithmException {
        return genKey(DEFAULT_KEY_SIZE);
    }

    public static String genKey256() throws NoSuchAlgorithmException {
        return genKey(KEY_SIZE_256);
    }

    private static String genKey(int keySize) throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(KEY_ALGORITHM);
        SecureRandom random = new SecureRandom();
        keyGen.init(keySize, random);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyGen.generateKey().getEncoded(), KEY_ALGORITHM);
        byte[] keyBytes = secretKeySpec.getEncoded();
        return Base64.getEncoder().encodeToString(keyBytes);
    }

    /**
     * 根据key 解密<br/>
     *
     * @param encryptedString 加密后base64数据
     * @param keyBase64       base64后的key
     * @return 解密后数据
     * @throws NoSuchAlgorithmException  exception
     * @throws InvalidKeyException       exception
     * @throws NoSuchPaddingException    exception
     * @throws IllegalBlockSizeException exception
     * @throws BadPaddingException       exception
     */
    public static String decrypt(String encryptedString, String keyBase64) throws NoSuchAlgorithmException,
            InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedString);
        byte[] keyBytes = Base64.getDecoder().decode(keyBase64);
        return new String(doDecrypt(encryptedBytes, new SecretKeySpec(keyBytes, KEY_ALGORITHM)), StandardCharsets.UTF_8);
    }

    private static byte[] doDecrypt(byte[] encryptedBytes, Key key) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(encryptedBytes);
    }

    public static String decryptDefault(String encryptedString) throws NoSuchAlgorithmException,
            InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        return decrypt(encryptedString, DEFAULT_KEY256);
    }

    public static String encryptDefault(String content) throws InvalidKeyException,
            NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        return encrypt(content, DEFAULT_KEY256);
    }

    /**
     * 根据key加密数据<br/>
     *
     * @param content 数据
     * @param key     key
     * @return 加密后base64数据
     * @throws InvalidKeyException       exception
     * @throws NoSuchAlgorithmException  exception
     * @throws NoSuchPaddingException    exception
     * @throws IllegalBlockSizeException exception
     * @throws BadPaddingException       exception
     */
    public static String encrypt(String content, String key) throws InvalidKeyException,
            NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        byte[] keyBytes = Base64.getDecoder().decode(key);
        byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(doEncrypt(contentBytes, new SecretKeySpec(keyBytes, KEY_ALGORITHM)));
    }

    private static byte[] doEncrypt(byte[] content, Key key) throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(content);
    }

    /**
     * create AES Cipher<br/>
     */
    public static Cipher getAesCipher(String sKey, int cipherMode) {
        Cipher cipher;
        try {
            SecretKeySpec key = new SecretKeySpec(sKey.getBytes(), KEY_ALGORITHM);
            cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            // 初始化
            cipher.init(cipherMode, key);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return cipher;
    }
}

