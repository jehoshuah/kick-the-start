package com.bizcards.webservices.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptDecrypt {

	public static String encrypt(String input, String keyString)
			throws Exception {

		// setup AES cipher in CBC mode with PKCS #5 padding
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

		// setup an IV (initialization vector) that should be
		// randomly generated for each input that's encrypted
		byte[] iv = new byte[cipher.getBlockSize()];
		new SecureRandom().nextBytes(iv);
		IvParameterSpec ivSpec = new IvParameterSpec(iv);

		// hash keyString with SHA-256 and crop the output to 128-bit for key
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		digest.update(keyString.getBytes());
		byte[] key = new byte[16];
		System.arraycopy(digest.digest(), 0, key, 0, key.length);
		SecretKeySpec keySpec = new SecretKeySpec(key, "AES");

		// encrypt
		cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
		byte[] encrypted = cipher.doFinal(input.getBytes("UTF-8"));
		String encryptedString = new String(encrypted);
		System.out.println("encrypted: " + new String(encrypted));

		return encryptedString;
		// include the IV with the encrypted bytes for transport, you'll
		// need the same IV when decrypting (it's safe to send unencrypted)

	}

	public static String decrypt(String input, String keyString)
			throws Exception {

		// setup AES cipher in CBC mode with PKCS #5 padding
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

		// setup an IV (initialization vector) that should be
		// randomly generated for each input that's encrypted
		byte[] iv = new byte[cipher.getBlockSize()];
		new SecureRandom().nextBytes(iv);
		IvParameterSpec ivSpec = new IvParameterSpec(iv);

		// hash keyString with SHA-256 and crop the output to 128-bit for key
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		digest.update(keyString.getBytes());
		byte[] key = new byte[16];
		System.arraycopy(digest.digest(), 0, key, 0, key.length);
		SecretKeySpec keySpec = new SecretKeySpec(key, "AES");

		// decrypt
		cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
		byte[] decrypted = cipher.doFinal(input.getBytes());
		String decryptedString = new String(decrypted, "UTF-8");
		System.out.println("decrypted: " + decryptedString);

		return decryptedString;
	}

	public static String getSha2Hash(String stringToHash) {

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return String.format("%s", stringToHash.hashCode());
		}
		md.update(stringToHash.getBytes());

		byte byteData[] = md.digest();

		// convert the byte to hex format method 1
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
					.substring(1));
		}
		return sb.toString();
	}
}
