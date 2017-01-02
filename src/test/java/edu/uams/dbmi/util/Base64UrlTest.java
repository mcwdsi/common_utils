package edu.uams.dbmi.util;

import org.junit.Test;


public class Base64UrlTest {
	@Test
	public void testEncode() {
		byte[] testBytes = { 33, 74, -100, 127 };
		assert(Base64Url.encodeToString(testBytes).equals("IUqcfw"));
	}
	
	@Test
	public void testDecode() {
		String encoded = "IUqcfw==";
		byte[] decoded = Base64.decode(encoded);
		assert(decoded[0] == 33 && decoded[1] == 74 && decoded[2] == -100 && decoded[3] == 127);
	}
	
	@Test
	public void testEncodeZero() {
		byte[] testBytes = { 0, 0, 0, 0, 0, 0 };
		assert(Base64Url.encodeToString(testBytes).equals("AAAAAAAA"));
	}
	
	@Test
	public void testDecodeAs() {
		String encoded = "AAAAAAAA";
		byte[] decoded = Base64Url.decode(encoded);
		boolean ok = (decoded.length==6);
		for (byte b : decoded) {
			ok = ok && b == 0;
		}
		assert(ok);
	}
	
	@Test
	public void testEncodeMinusOne() {
		byte[] testBytes = { -1, -1, -1, -1, -1, -1 };
		assert(Base64Url.encodeToString(testBytes).equals("________"));
	}
	
	@Test
	public void testDecodeSlashes() {
		String encoded = "________";
		byte[] decoded = Base64Url.decode(encoded);
		boolean ok = (decoded.length==6);
		for (byte b : decoded) {
			ok = ok && b == -1;
		}
		assert(ok);
	}
}
