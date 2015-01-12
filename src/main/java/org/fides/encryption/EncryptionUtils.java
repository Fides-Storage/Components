package org.fides.encryption;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.Security;

import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.CamelliaEngine;
import org.bouncycastle.crypto.io.CipherInputStream;
import org.bouncycastle.crypto.io.CipherOutputStream;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

/**
 * This EncryptionUtil can return Decryption and Encryption Streams
 */
public class EncryptionUtils {

	/**
	 * The algorithm used for encryption and decryption, when changing it dont forgot to update the
	 * {@link org.fides.encryption.EncryptionUtils#createCipher()}
	 */
	public static final String ALGORITHM = "Camellia";

	/** The algorithm used for encryption and decryption */
	public static final int KEY_SIZE = 32; // 256 bit

	/** The IV used to initiate the cipher */
	private static final byte[] IV = { 0x46, 0x69, 0x64, 0x65, 0x73, 0x2, 0x69, 0x73, 0x20, 0x53, 0x65, 0x63, 0x75, 0x72, 0x65, 0x21 };

	static {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}

	/**
	 * Create a n {@link java.io.InputStream} that decrypts and encrypted {@link java.io.InputStream}
	 *
	 * @param in
	 *            The stream to decrypt
	 * @param key
	 *            The {@link java.security.Key} to use
	 * @return An decrypting {@link java.io.InputStream}
	 */
	public static InputStream getDecryptionStream(InputStream in, Key key) {
		BufferedBlockCipher cipher = createCipher();

		KeyParameter keyParam = new KeyParameter(key.getEncoded());
		CipherParameters params = new ParametersWithIV(keyParam, IV);
		cipher.reset();
		// false because are decrypting
		cipher.init(false, params);

		return new CipherInputStream(in, cipher);
	}

	/**
	 * Create a n {@link java.io.OutputStream} that encrypts and encrypted {@link java.io.OutputStream}
	 *
	 * @param out
	 *            The stream to encrypt
	 * @param key
	 *            The {@link Key} to use
	 * @return An encrypting {@link java.io.OutputStream}
	 */
	public static OutputStream getEncryptionStream(OutputStream out, Key key) {
		BufferedBlockCipher cipher = createCipher();

		KeyParameter keyParam = new KeyParameter(key.getEncoded());
		CipherParameters params = new ParametersWithIV(keyParam, IV);
		cipher.reset();
		// true because are encrypting
		cipher.init(true, params);

		return new CipherOutputStream(out, cipher);
	}

	/** Create the cipher to use */
	private static BufferedBlockCipher createCipher() {
		return new PaddedBufferedBlockCipher(new CBCBlockCipher(new CamelliaEngine()), new PKCS7Padding());
	}
}
