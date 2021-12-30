package cn.weforward.devops;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.junit.Test;

public class NetTest {
	@Test
	public void test() throws SocketException {
		Enumeration<NetworkInterface> ifs;
		ifs = NetworkInterface.getNetworkInterfaces();
		while (ifs.hasMoreElements()) {
			NetworkInterface ni = ifs.nextElement();
			System.out.println(ni.getName());
			Enumeration<InetAddress> ias = ni.getInetAddresses();
			while (ias.hasMoreElements()) {
				InetAddress ip = ias.nextElement();
				System.out.println(ip.getCanonicalHostName());
			}
		}
	}
}
