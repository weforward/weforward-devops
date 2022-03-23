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

import java.util.ArrayList;
import java.util.List;

import cn.weforward.common.util.StringUtil;
import cn.weforward.util.docker.DockerPullProgesser;

public class CollectionPullProgesser implements DockerPullProgesser {

	protected ArrayList<PullStatus> m_Status;

	public CollectionPullProgesser() {
		m_Status = new ArrayList<>();
	}

	@Override
	public void progesser(PullStatus status) {
		ArrayList<PullStatus> newlist = new ArrayList<>();
		boolean replace = false;
		for (PullStatus s : m_Status) {
			if (!StringUtil.isEmpty(status.getId()) && StringUtil.eq(status.getId(), s.getId())) {
				newlist.add(status);
				replace = true;
			} else {
				newlist.add(s);
			}
		}
		if (!replace) {
			newlist.add(status);
		}
		m_Status = newlist;
	}

	public List<PullStatus> getStatus() {
		return m_Status;
	}

}
