/**
 * Copyright (c) 2019,2020 honintech
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */
package cn.weforward.util.docker.ext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * 侦读取
 * 
 * @author daibo
 *
 */
public class FrameReader implements AutoCloseable {

	private static final int HEADER_SIZE = 8;

	private final InputStream m_InputStream;

	private final byte[] m_RawBuffer = new byte[1000];

	private Boolean m_RawStreamDetected = false;

	public FrameReader(InputStream inputStream) {
		m_InputStream = inputStream;
	}

	private static StreamType streamType(byte streamType) {
		switch (streamType) {
		case 0:
			return StreamType.STDIN;
		case 1:
			return StreamType.STDOUT;
		case 2:
			return StreamType.STDERR;
		default:
			return StreamType.RAW;
		}
	}

	/**
	 * 读取
	 * 
	 * @return
	 * @throws IOException
	 */
	public Frame readFrame() throws IOException {
		if (m_RawStreamDetected) {
			int read = m_InputStream.read(m_RawBuffer);
			if (read == -1) {
				return null;
			}
			return new Frame(StreamType.RAW, Arrays.copyOf(m_RawBuffer, read));
		}
		byte[] header = new byte[HEADER_SIZE];
		int actualHeaderSize = 0;
		do {
			int headerCount = m_InputStream.read(header, actualHeaderSize, HEADER_SIZE - actualHeaderSize);
			if (headerCount == -1) {
				return null;
			}
			actualHeaderSize += headerCount;
		} while (actualHeaderSize < HEADER_SIZE);

		StreamType streamType = streamType(header[0]);
		if (streamType.equals(StreamType.RAW)) {
			m_RawStreamDetected = true;
			return new Frame(StreamType.RAW, Arrays.copyOf(header, HEADER_SIZE));
		}
		int payloadSize = ((header[4] & 0xff) << 24) + ((header[5] & 0xff) << 16) + ((header[6] & 0xff) << 8)
				+ (header[7] & 0xff);
		byte[] payload = new byte[payloadSize];
		int actualPayloadSize = 0;
		do {
			int count = m_InputStream.read(payload, actualPayloadSize, payloadSize - actualPayloadSize);
			if (count == -1) {
				if (actualPayloadSize != payloadSize) {
					throw new IOException(
							String.format("payload must be %d bytes long, but was %d", payloadSize, actualPayloadSize));
				}
				break;
			}
			actualPayloadSize += count;
		} while (actualPayloadSize < payloadSize);
		return new Frame(streamType, payload);
	}

	@Override
	public void close() throws IOException {
		m_InputStream.close();
	}

}
