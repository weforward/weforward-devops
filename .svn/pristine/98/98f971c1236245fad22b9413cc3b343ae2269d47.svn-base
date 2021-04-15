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

import java.io.Serializable;
import java.util.Arrays;

/**
 * 收到的数据侦
 * 
 * @author daibo
 *
 */
public class Frame implements Serializable {

	private static final long serialVersionUID = 1L;

	private final StreamType m_StreamType;

	private final byte[] m_Payload;

	public Frame(StreamType streamType, byte[] payload) {
		m_StreamType = streamType;
		m_Payload = payload;
	}

	public StreamType getStreamType() {
		return m_StreamType;
	}

	public byte[] getPayload() {
		return m_Payload;
	}

	@Override
	public String toString() {
		return String.format("%s: %s", m_StreamType, new String(m_Payload).trim());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o instanceof Frame) {
			Frame frame = (Frame) o;
			return m_StreamType == frame.m_StreamType && Arrays.equals(m_Payload, frame.m_Payload);
		}
		return false;

	}

	@Override
	public int hashCode() {
		int result = m_StreamType.hashCode();
		result = 31 * result + Arrays.hashCode(m_Payload);
		return result;
	}
}
