package org.fides.components.virtualstream;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * A virtual {@link OutputStream} that simulates an {@link OutputStream} on top of an existing {@link OutputStream}.
 * This is done using a buffer and a custom protocol for writing data. This stream does not close the underlying
 * {@link OutputStream}.
 */
public class VirtualOutputStream extends FilterOutputStream {

	private static final short DEFAULT_BUFFER_SIZE = 1024;

	private final byte[] buffer;

	private short count = 0;

	/**
	 * Constructor
	 * 
	 * @param out
	 *            The {@link OutputStream} to build the virtual {@link OutputStream} upon
	 * @param bufferSize
	 *            The size of the buffer
	 */
	public VirtualOutputStream(OutputStream out, short bufferSize) {
		super(out);
		buffer = new byte[bufferSize];
	}

	/**
	 * Constructor with the default buffer size
	 * 
	 * @param out
	 *            The {@link OutputStream} to build the virtual {@link OutputStream} upon
	 */
	public VirtualOutputStream(OutputStream out) {
		this(out, DEFAULT_BUFFER_SIZE);
	}

	@Override
	public void write(int b) throws IOException {
		if (count >= buffer.length) {
			flushBuffer();
		}
		buffer[count++] = (byte) b;
	}

	/**
	 * Writes the buffer to the underlying {@link OutputStream} using the virtual stream protocol
	 * 
	 * @throws IOException
	 */
	private void flushBuffer() throws IOException {
		byte[] prefix = ByteBuffer.allocate(2).putShort(count).array();
		out.write(prefix);
		out.write(buffer, 0, count);
		count = 0;
	}

	@Override
	public void flush() throws IOException {
		flushBuffer();
		out.flush();
	}

	@Override
	public void close() throws IOException {
		flushBuffer();
		byte[] postfix = ByteBuffer.allocate(2).putShort((short) -1).array();
		out.write(postfix);
		out.flush();
	}

}
