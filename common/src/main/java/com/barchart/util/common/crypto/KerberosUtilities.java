package com.barchart.util.common.crypto;

import java.net.URI;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kerberos utilities.
 */
public class KerberosUtilities {

	/**
	 * Hex character dictionary.
	 */
	private static final char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * Use kerberos KDF.
	 * 
	 * @see http://www.ietf.org/rfc/rfc3962.txt
	 */
	static final String KEY_FACTORY = "PBKDF2WithHmacSHA1";

	/**
	 * Number of iterations during key generation.
	 */
	static final int KEY_LOOP = 128 * 1024;

	/**
	 * Minimum AES key size supported by all versions of JDK 6,7.
	 */
	static final int KEY_SIZE = 128;

	/**
	 * Use AES key specification.
	 * 
	 * @see http://www.ietf.org/rfc/rfc3962.txt
	 */
	static final String KEY_SPEC = "AES";

	/**
	 * Use AES cipher block transform.
	 * 
	 * @see http://www.ietf.org/rfc/rfc3962.txt
	 */
	static final String KEY_XFORM = "AES/CBC/PKCS5Padding";

	static final Logger log = LoggerFactory.getLogger(KerberosUtilities.class);

	private static final SecureRandom SECURE_RANDOM = new SecureRandom();

	/**
	 * Use HMAC-256 signature.
	 */
	static final String SIGNATURE = "HmacSHA256";

	/**
	 * Use only UTF-8 strings.
	 */
	static final Charset UTF_8 = Charset.forName("UTF-8");

	/**
	 * Concatenate 2 arrays.
	 */
	public static byte[] arrayCon(final byte[] one, final byte[] two) {
		final byte[] con = new byte[one.length + two.length];
		System.arraycopy(one, 0, con, 0, one.length);
		System.arraycopy(two, 0, con, one.length, two.length);
		return con;
	}

	/**
	 * Select first part of concatenated array.
	 */
	public static byte[] arrayOne(final int position, final byte[] con) {
		final int size = position;
		assert con.length >= size;
		final byte[] one = new byte[size];
		System.arraycopy(con, 0, one, 0, size);
		return one;
	}

	/**
	 * Select second part of concatenated array.
	 */
	public static byte[] arrayTwo(final int position, final byte[] con) {
		final int size = con.length - position;
		assert size >= 0;
		final byte[] one = new byte[size];
		System.arraycopy(con, position, one, 0, size);
		return one;
	}

	/**
	 * Convert into Base-64.
	 */
	public static byte[] base64decode(final String text) {
		return DatatypeConverter.parseBase64Binary(text);
	}

	/**
	 * Convert from Base-64.
	 */
	public static String base64encode(final byte[] array) {
		return DatatypeConverter.printBase64Binary(array);
	}

	/**
	 * Convert UTF-8 string into bytes.
	 */
	public static byte[] byteArray(final String text) {
		return text.getBytes(UTF_8);
	}

	/**
	 * Convert bytes to hex string.
	 */
	public static String bytesToHex(final byte[] array) {
		final char[] alphaList = new char[array.length * 2];
		int alpha;
		for (int j = 0; j < array.length; j++) {
			alpha = array[j] & 0xFF;
			alphaList[j * 2] = HEX[alpha >>> 4];
			alphaList[j * 2 + 1] = HEX[alpha & 0x0F];
		}
		return new String(alphaList);
	}

	/**
	 * Decrypt from AES.
	 */
	public static byte[] defaultDecrypt(final byte[] ciferText,
			final byte[] secretKey) {
		try {

			final SecretKey secret = new SecretKeySpec(secretKey, KEY_SPEC);

			final Cipher cipher = Cipher.getInstance(KEY_XFORM);

			final byte[] init = arrayOne(16, ciferText);
			final byte[] body = arrayTwo(16, ciferText);

			cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(init));

			return cipher.doFinal(body);

		} catch (final Throwable e) {
			throw new SecurityException("Decrypt failure.", e);
		}
	}

	/**
	 * Encrypt into AES.
	 */
	public static byte[] defaultEncrypt(final byte[] plainText,
			final byte[] secretKey) {
		try {

			final SecretKey secret = new SecretKeySpec(secretKey, KEY_SPEC);

			final Cipher cipher = Cipher.getInstance(KEY_XFORM);

			cipher.init(Cipher.ENCRYPT_MODE, secret);

			final byte[] init = cipher.getParameters()
					.getParameterSpec(IvParameterSpec.class).getIV();

			assert init.length == 16;

			final byte[] body = cipher.doFinal(plainText);

			return arrayCon(init, body);

		} catch (final Throwable e) {
			throw new SecurityException("Encrypt failure.", e);
		}
	}

	/**
	 * Generate secure random UUID.
	 */
	public static byte[] defaultRandomUUID() {
		final byte[] array = new byte[16];
		SECURE_RANDOM.nextBytes(array);
		return array;
	}

	/**
	 * Advanced Encryption Standard (AES) Encryption for Kerberos 5
	 * 
	 * @see http://www.ietf.org/rfc/rfc3962.txt
	 * 
	 * @param uri
	 *            - salt to AES
	 * @param secret
	 *            - client/user password
	 */
	public static byte[] defaultSecretKey(final String uri, final String secret) {
		try {

			final SecretKeyFactory factory = SecretKeyFactory
					.getInstance(KEY_FACTORY);

			final KeySpec spec = new PBEKeySpec(secret.toCharArray(),
					byteArray(uri), KEY_LOOP, KEY_SIZE);

			return factory.generateSecret(spec).getEncoded();

		} catch (final Throwable e) {
			throw new SecurityException("Secret key failure.", e);
		}
	}

	/**
	 * Generate HMAC-256 signature.
	 */
	public static byte[] defaultSignatureCreate(final byte[] plainText,
			final byte[] secretKey) {
		try {

			final SecretKey secret = new SecretKeySpec(secretKey, SIGNATURE);

			final Mac signature = Mac.getInstance(SIGNATURE);

			signature.init(secret);

			return signature.doFinal(plainText);

		} catch (final Throwable e) {
			throw new SecurityException("Signature failure.", e);
		}
	}

	/**
	 * Verify signature match;
	 */
	public static boolean defaultSignatureVerify(final byte[] plainText,
			final byte[] secretKey, final byte[] signature) {
		final byte[] verification = defaultSignatureCreate(plainText, secretKey);
		return Arrays.equals(signature, verification);
	}

	/**
	 * Decrypt UTF-8 string.
	 */
	public static String defaultStringDecrypt(final byte[] ciferText,
			final byte[] secretKey) {
		return new String(defaultDecrypt(ciferText, secretKey), UTF_8);
	}

	/**
	 * Encrypt UTF-8 string.
	 */
	public static byte[] defaultStringEncrypt(final String plainText,
			final byte[] secretKey) {
		return defaultEncrypt(plainText.getBytes(UTF_8), secretKey);
	}

	/**
	 * Verify if text has valid format URI.
	 */
	public static boolean isValidURI(final String textURI) {
		try {
			final URI uri = new URI(textURI);
			if (uri.getScheme() == null) {
				throw new IllegalArgumentException("Missing scheme.");
			}
			if (uri.getAuthority() == null) {
				throw new IllegalArgumentException("Missing authority.");
			}
			if (uri.getPath() == null) {
				throw new IllegalArgumentException("Missing uri path.");
			}
			return true;
		} catch (final Throwable e) {
			return false;
		}
	}

	private KerberosUtilities() {
	}

}
