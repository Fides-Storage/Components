package org.fides.components.virtualstream;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * A virtual {@link InputStream} that simulates an {@link InputStream} on top of an existing {@link InputStream}. This
 * stream does not close the underlying {@link InputStream}.
 */
public class VirtualInputStream extends InputStream {

	private short bytesLeft = 0;

	private InputStream in;

	/**
	 * Constructor for VirtualInputStream
	 * 
	 * @param in
	 *            The inputstream to virtualize
	 */
	public VirtualInputStream(InputStream in) {
		this.in = in;
	}

	@Override
	public int read() throws IOException {
		while (bytesLeft == 0) {
			readPrefix();
		}
		if (bytesLeft < 0) {
			return -1;
		}
		--bytesLeft;
		return in.read();

	}

	private void readPrefix() throws IOException {
		// Read the prefix
		byte[] prefix = new byte[2];
		int first = in.read();
		int second = in.read();
		if (first < 0 || second < 0) {
			throw new IOException("Could not read prefix properly");
		}

		prefix[0] = (byte) first;
		prefix[1] = (byte) second;
		bytesLeft = ByteBuffer.wrap(prefix).getShort();
	}

	@Override
	public void close() throws IOException {
		// We do not want to close the underlying InputStream
		// We do want to read everything the virtualoutputstream wanted to give us
		while (read() != -1) {
			// We just want to read until empty
		}
	}

	@Override
	public long skip(long n) throws IOException {
		throw new IOException("skip not supported");
	}

}
