package de.questmaster.tudmensa.tokenreplacer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.io.Reader;
import java.nio.CharBuffer;

public class TokenReplacingInputStream extends InputStream {

	protected PushbackReader pushbackReader = null;
	protected ITokenResolver tokenResolver = null;
	protected StringBuilder tokenNameBuffer = new StringBuilder();
	protected String tokenValue = null;
	protected int tokenValueIndex = 0;

	public TokenReplacingInputStream(InputStream source, ITokenResolver resolver) {
		this(new InputStreamReader(source), resolver);
	}
	
	public TokenReplacingInputStream(Reader source, ITokenResolver resolver) {
		this.pushbackReader = new PushbackReader(source, 2);
		this.tokenResolver = resolver;
	}

	public int read(CharBuffer target) throws IOException {
		throw new RuntimeException("Operation Not Supported");
	}

	public int read() throws IOException {
		if (this.tokenValue != null) {
			if (this.tokenValueIndex < this.tokenValue.length()) {
				return this.tokenValue.charAt(this.tokenValueIndex++);
			}
			if (this.tokenValueIndex == this.tokenValue.length()) {
				this.tokenValue = null;
				this.tokenValueIndex = 0;
			}
		}

		int data = this.pushbackReader.read();
		if (data != '&')
			return data;

//		data = this.pushbackReader.read();
//		if (data != '{') {
//			this.pushbackReader.unread(data);
//			return '$';
//		}
		this.tokenNameBuffer.delete(0, this.tokenNameBuffer.length());

		data = this.pushbackReader.read();
		while (data != ';') {
			this.tokenNameBuffer.append((char) data);
			data = this.pushbackReader.read();
		}

		this.tokenValue = this.tokenResolver.resolveToken(this.tokenNameBuffer.toString());
		
		if (this.tokenValue == null) {
			this.tokenValue = this.tokenNameBuffer.toString();
		}
		return this.tokenValue.charAt(this.tokenValueIndex++);

	}

	public int read(byte cbuf[]) throws IOException {
		return read(cbuf, 0, cbuf.length);
	}

	public int read(byte cbuf[], int off, int len) throws IOException {
		int charsRead = 0;
		for (int i = 0; i < len; i++) {
			charsRead = i;
			int nextChar = read();
			if (nextChar == -1) {
				break;
			}
			cbuf[off + i] = (byte) nextChar;
		}
		return charsRead;
	}

	public void close() throws IOException {
		this.pushbackReader.close();
	}

	public long skip(long n) throws IOException {
		throw new RuntimeException("Operation Not Supported");
	}

	public boolean ready() throws IOException {
		return this.pushbackReader.ready();
	}

	public boolean markSupported() {
		return false;
	}

	public void mark(int readAheadLimit) {
		throw new RuntimeException("Operation Not Supported");
	}

	public void reset() throws IOException {
		throw new RuntimeException("Operation Not Supported");
	}
}