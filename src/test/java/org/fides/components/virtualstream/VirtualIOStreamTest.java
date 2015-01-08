package org.fides.components.virtualstream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

/**
 * Tests for the {@link VirtualInputStream} and the {@link VirtualOutputStream}
 * 
 */
public class VirtualIOStreamTest {

	private static final byte[] TEST_BYTES = "This is a sentence for testing the sending and receiving of the Virtual Input- and OutputStream".getBytes();

	private static final byte[] TEST_BYTES_2 = "This is another sentence used for testing the sending and receiving of our VirtualIOStream".getBytes();

	private static final short TEST_BUFFER_SIZE = 5;

	/**
	 * 
	 * @throws IOException
	 */
	@Test
	public void testByteArraySendReceive() throws IOException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		OutputStream virtualOut = new VirtualOutputStream(byteOut, TEST_BUFFER_SIZE);

		virtualOut.write(TEST_BYTES);
		virtualOut.flush();
		virtualOut.close();
		InputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
		InputStream virtualIn = new VirtualInputStream(byteIn);

		ByteArrayOutputStream sentBytes = new ByteArrayOutputStream();
		IOUtils.copy(virtualIn, sentBytes);
		virtualIn.close();

		assertArrayEquals(TEST_BYTES, sentBytes.toByteArray());
	}

	/**
	 * Tests if sending two streams in a row and then receiving two streams, the streams are read correctly
	 * 
	 * @throws IOException
	 */
	@Test
	public void testSendReceiveTwice() throws IOException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

		OutputStream virtualOut1 = new VirtualOutputStream(byteOut, TEST_BUFFER_SIZE);
		virtualOut1.write(TEST_BYTES);
		virtualOut1.flush();
		virtualOut1.close();

		OutputStream virtualOut2 = new VirtualOutputStream(byteOut, TEST_BUFFER_SIZE);
		virtualOut2.write(TEST_BYTES_2);
		virtualOut2.flush();
		virtualOut2.close();

		InputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());

		InputStream virtualIn1 = new VirtualInputStream(byteIn);
		ByteArrayOutputStream sentBytes = new ByteArrayOutputStream();
		IOUtils.copy(virtualIn1, sentBytes);
		virtualIn1.close();

		InputStream virtualIn2 = new VirtualInputStream(byteIn);
		ByteArrayOutputStream sentBytes2 = new ByteArrayOutputStream();
		IOUtils.copy(virtualIn2, sentBytes2);
		virtualIn2.close();

		assertArrayEquals(TEST_BYTES, sentBytes.toByteArray());
		assertArrayEquals(TEST_BYTES_2, sentBytes2.toByteArray());
	}

	/**
	 * Test with doing multiple flushes
	 * 
	 * @throws IOException
	 */
	@Test
	public void testSendReceiveMultipleFlushes() throws IOException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		OutputStream virtualOut = new VirtualOutputStream(byteOut, TEST_BUFFER_SIZE);

		virtualOut.flush();
		virtualOut.write(TEST_BYTES);
		virtualOut.flush();
		virtualOut.flush();
		virtualOut.write(TEST_BYTES_2);
		virtualOut.flush();
		virtualOut.close();
		InputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
		InputStream virtualIn = new VirtualInputStream(byteIn);

		ByteArrayOutputStream sentBytes = new ByteArrayOutputStream();
		IOUtils.copy(virtualIn, sentBytes);
		virtualIn.close();

		byte[] expectedResult = ArrayUtils.addAll(TEST_BYTES, TEST_BYTES_2);
		assertArrayEquals(expectedResult, sentBytes.toByteArray());
	}

	/**
	 * Test with an small buffer
	 * 
	 * @throws IOException
	 */
	@Test
	public void testSendReceiveSmallBuffer() throws IOException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		OutputStream virtualOut = new VirtualOutputStream(byteOut, (short) 1);

		virtualOut.write(TEST_BYTES);
		virtualOut.flush();
		virtualOut.close();
		InputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
		InputStream virtualIn = new VirtualInputStream(byteIn);

		ByteArrayOutputStream sentBytes = new ByteArrayOutputStream();
		IOUtils.copy(virtualIn, sentBytes);
		virtualIn.close();

		assertArrayEquals(TEST_BYTES, sentBytes.toByteArray());
	}

	/**
	 * Tests if when an {@link VirtualInputStream} is closed, it will read until the end of the
	 * {@link VirtualOutputStream}. This so check if it does not break the data that comes after
	 * 
	 * @throws IOException
	 */
	@Test
	public void testSendCloseIn() throws IOException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

		OutputStream virtualOut1 = new VirtualOutputStream(byteOut, TEST_BUFFER_SIZE);
		virtualOut1.write(TEST_BYTES);
		virtualOut1.flush();
		virtualOut1.close();

		OutputStream virtualOut2 = new VirtualOutputStream(byteOut, TEST_BUFFER_SIZE);
		virtualOut2.write(TEST_BYTES_2);
		virtualOut2.flush();
		virtualOut2.close();

		InputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());

		InputStream virtualIn1 = new VirtualInputStream(byteIn);
		ByteArrayOutputStream sentBytes = new ByteArrayOutputStream();
		byte[] bytesRead = new byte[10];
		virtualIn1.read(bytesRead);
		sentBytes.write(bytesRead);
		virtualIn1.close();

		InputStream virtualIn2 = new VirtualInputStream(byteIn);
		ByteArrayOutputStream sentBytes2 = new ByteArrayOutputStream();
		IOUtils.copy(virtualIn2, sentBytes2);
		virtualIn2.close();

		assertEquals(10, sentBytes.toByteArray().length);
		assertArrayEquals(TEST_BYTES_2, sentBytes2.toByteArray());
	}

	/**
	 * Tests if the correct exceptions are thrown when trying to call methods on a closed VirtualOutputStream
	 * 
	 * @throws IOException
	 */
	@Test
	public void testOutputStreamClosed() throws IOException {
		VirtualOutputStream virtualOut = new VirtualOutputStream(new ByteArrayOutputStream());
		virtualOut.close();

		// No exception should be thrown, the stream has already been closed and there should be nothing left to flush
		virtualOut.close();
		virtualOut.flush();

		boolean successfulCatch = false;
		try {
			virtualOut.write(TEST_BYTES);
		} catch (IOException e) {
			successfulCatch = true;
		}
		assertTrue(successfulCatch);
	}

	/**
	 * A badweather test for testing if trying to create an outputstream without a buffer throws the correct exception.
	 */
	@Test
	public void testIllegalOutputStream() {
		boolean successfulCatch = false;
		try {
			new VirtualOutputStream(new ByteArrayOutputStream(), (short) 0);
		} catch (IllegalArgumentException e) {
			successfulCatch = true;
		}
		assertTrue(successfulCatch);

		successfulCatch = false;
		try {
			new VirtualOutputStream(new ByteArrayOutputStream(), (short) -1);
		} catch (IllegalArgumentException e) {
			successfulCatch = true;
		}
		assertTrue(successfulCatch);
	}

	/**
	 * Tests if all unsupported functions of VirtualInputStream throw the correct exceptions.
	 */
	@Test
	public void testUnsupportedInputFunctions() {
		InputStream virtualIn = new VirtualInputStream(new ByteArrayInputStream(new byte[0]));

		assertFalse(virtualIn.markSupported());

		boolean successfulCatch = false;
		try {
			virtualIn.reset();
		} catch (IOException e) {
			successfulCatch = true;
		}
		assertTrue(successfulCatch);

		successfulCatch = false;
		try {
			virtualIn.skip(2);
		} catch (IOException e) {
			successfulCatch = true;
		}
		assertTrue(successfulCatch);

		IOUtils.closeQuietly(virtualIn);
	}
}
