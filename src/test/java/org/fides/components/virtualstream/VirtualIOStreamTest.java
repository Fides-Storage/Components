package org.fides.components.virtualstream;

import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

public class VirtualIOStreamTest {

	static final byte[] testBytes = "This is a sentence for testing the sending and receiving of the Virtual Input- and OutputStream".getBytes();

	static final byte[] testBytes2 = "This is another sentence used for testing the sending and receiving of our VirtualIOStream".getBytes();

	static final short testBufferSize = 5;

	@Test
	public void testByteArraySendReceive() throws IOException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		OutputStream virtualOut = new VirtualOutputStream(byteOut, testBufferSize);

		virtualOut.write(testBytes);
		virtualOut.flush();
		virtualOut.close();
		InputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
		InputStream virtualIn = new VirtualInputStream(byteIn);

		ByteArrayOutputStream sentBytes = new ByteArrayOutputStream();
		IOUtils.copy(virtualIn, sentBytes);
		virtualIn.close();

		assertArrayEquals(testBytes, sentBytes.toByteArray());
	}

	/**
	 * Tests if sending two streams in a row and then receiving two streams, the streams are read correctly
	 * 
	 * @throws IOException
	 */
	@Test
	public void testSendReceiveTwice() throws IOException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

		OutputStream virtualOut1 = new VirtualOutputStream(byteOut, testBufferSize);
		virtualOut1.write(testBytes);
		virtualOut1.flush();
		virtualOut1.close();

		OutputStream virtualOut2 = new VirtualOutputStream(byteOut, testBufferSize);
		virtualOut2.write(testBytes2);
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

		assertArrayEquals(testBytes, sentBytes.toByteArray());
		assertArrayEquals(testBytes2, sentBytes2.toByteArray());
	}

	@Test
	public void testSendReceiveMultipleFlushes() throws IOException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		OutputStream virtualOut = new VirtualOutputStream(byteOut, testBufferSize);

		virtualOut.flush();
		virtualOut.write(testBytes);
		virtualOut.flush();
		virtualOut.flush();
		virtualOut.write(testBytes2);
		virtualOut.flush();
		virtualOut.close();
		InputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
		InputStream virtualIn = new VirtualInputStream(byteIn);

		ByteArrayOutputStream sentBytes = new ByteArrayOutputStream();
		IOUtils.copy(virtualIn, sentBytes);
		virtualIn.close();

		byte[] expectedResult = ArrayUtils.addAll(testBytes, testBytes2);
		assertArrayEquals(expectedResult, sentBytes.toByteArray());
	}

	@Test
	public void testSendReceiveSmallBuffer() throws IOException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		OutputStream virtualOut = new VirtualOutputStream(byteOut, (short) 1);

		virtualOut.write(testBytes);
		virtualOut.flush();
		virtualOut.close();
		InputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
		InputStream virtualIn = new VirtualInputStream(byteIn);

		ByteArrayOutputStream sentBytes = new ByteArrayOutputStream();
		IOUtils.copy(virtualIn, sentBytes);
		virtualIn.close();

		assertArrayEquals(testBytes, sentBytes.toByteArray());
	}
}
