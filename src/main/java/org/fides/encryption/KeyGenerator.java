package org.fides.encryption;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The KeyGenerator is a helper class for generating hashes from passwords These hashes will be generated with PBKDF2
 */
public final class KeyGenerator {
	/**
	 * Log for this class
	 */
	private static final Logger LOG = LogManager.getLogger(KeyGenerator.class);

	private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";

	private static int pbkdf2Iterations = 1000;

	private static final SecureRandom RANDOM = new SecureRandom();

	static {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}

	private KeyGenerator() {
	}

	/**
	 * Returns a random generated salt
	 * 
	 * @param saltByteSize
	 *            is the size of the salt
	 * @return a salt as byte array
	 */
	public static byte[] getSalt(int saltByteSize) {
		// Generate a random salt
		byte[] salt = new byte[saltByteSize];
		RANDOM.nextBytes(salt);
		return salt;
	}

	/**
	 * Returns the default number of rounds used by PBKDF2
	 * 
	 * @return the default number of rounds
	 */
	public static int getRounds() {
		return pbkdf2Iterations;
	}

	/**
	 * Returns a Key which was generated with the given password, salt en rounds
	 * 
	 * @param password
	 *            the password to hash
	 * @param salt
	 *            the salt for PBKDF2
	 * @param rounds
	 *            the amount of rounds PBKDF2 should use
	 * @param keyByteSize
	 *            is the size of the generated hash in bytes.
	 * @return a Key which was generated with PBKDF2
	 */
	public static Key generateKey(String password, byte[] salt, int rounds, int keyByteSize) {
		pbkdf2Iterations = rounds;
		return pbkdf2(password.toCharArray(), salt, pbkdf2Iterations, keyByteSize);
	}

	/**
	 * Generates a random key with the given algorithm and given size.
	 * 
	 * @param keyByteSize
	 *            the size of the generated key in bytes.
	 * @return a random generated Key
	 * @throws NoSuchAlgorithmException
	 */
	public static Key generateRandomKey(String algorithm, int keyByteSize) throws NoSuchAlgorithmException {
		javax.crypto.KeyGenerator generator = javax.crypto.KeyGenerator.getInstance(algorithm);
		generator.init(keyByteSize * 8);
		return generator.generateKey();
	}

	/**
	 * Computes the PBKDF2 hash of a password.
	 * 
	 * @param password
	 *            the password to hash.
	 * @param salt
	 *            the salt
	 * @param iterations
	 *            the iteration count (slowness factor)
	 * @param bytes
	 *            the length of the hash to compute in bytes
	 * @return the PBDKF2 hash of the password
	 */
	private static Key pbkdf2(char[] password, byte[] salt, int iterations, int bytes) {
		PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
		try {
			// Get al SecretKeyFactory instance based on the given algorithm and generate the SecretKey based on the
			// PBEKeySpec
			return SecretKeyFactory.getInstance(PBKDF2_ALGORITHM).generateSecret(spec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			LOG.error(e);
		}
		return null;
	}

}
