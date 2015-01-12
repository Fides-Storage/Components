package org.fides.tools;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utils for client
 * 
 */
public class HashUtils {
	/**
	 * Log for this class
	 */
	private static final Logger LOG = LogManager.getLogger(HashUtils.class);

	/**
	 * hash function to hash password
	 */
	private static final String HASH_ALGORITHM = "SHA-256";

	/**
	 * Hash data
	 * 
	 * @param data
	 *            to hash
	 * @return hash of data
	 */
	public static String hash(String data) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(HASH_ALGORITHM);
			messageDigest.update(data.getBytes());
			return toHex(messageDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			LOG.error(e);
		}
		return null;

	}

	/**
	 * Converts a byte array into a hexadecimal string.
	 * 
	 * @param array
	 *            the byte array to convert
	 * @return a length*2 character string encoding the byte array
	 */
	public static String toHex(byte[] array) {
		BigInteger bi = new BigInteger(1, array);
		String hex = bi.toString(16);
		int paddingLength = (array.length * 2) - hex.length();
		if (paddingLength > 0) {
			return String.format("%0" + paddingLength + "d", 0) + hex;
		} else {
			return hex;
		}
	}

	/**
	 * Converts a string of hexadecimal characters into a byte array.
	 * 
	 * @param hex
	 *            the hex string
	 * @return the hex string decoded into a byte array
	 */
	public static byte[] fromHex(String hex) {
		byte[] binary = new byte[hex.length() / 2];
		for (int i = 0; i < binary.length; i++) {
			binary[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return binary;
	}

}
