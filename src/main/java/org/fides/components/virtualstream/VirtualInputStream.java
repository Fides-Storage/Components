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
	 * Constructor
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
		int read = in.read(prefix);
		if (read != 2) {
			throw new IOException("Prefix does not have the right size");
		}
		bytesLeft = ByteBuffer.wrap(prefix).getShort();
	}

	@Override
	public void close() throws IOException {
		// We do not want to close the underlying InputStream
	}

	@Override
	public long skip(long n) throws IOException {
		throw new UnsupportedOperationException("skip is not supported");
	}

	@Override
	public synchronized void mark(int readlimit) {
		throw new UnsupportedOperationException("mark is not supported");
	}

	@Override
	public synchronized void reset() throws IOException {
		throw new UnsupportedOperationException("reset is not supported");
	}

	@Override
	public boolean markSupported() {
		return false;
	}

}
