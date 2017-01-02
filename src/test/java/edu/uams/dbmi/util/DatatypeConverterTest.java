package edu.uams.dbmi.util;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.Test;

public class DatatypeConverterTest {
	@Test
	public void testConversion() {
		long msb = 2450948520845L;
		long lsb = 9797113401345L;
		
		UUID uuid = new UUID(msb, lsb);
		
		byte[] ba = new byte[16];
		
		DatatypeConverter.longToByteArray(ba, msb, 0);
		DatatypeConverter.longToByteArray(ba, lsb, 8);
		
		long msbR = DatatypeConverter.toLong(ba, 0);
		long lsbR = DatatypeConverter.toLong(ba, 8);
		
		System.out.println("msb == msbR? " + (msb==msbR));
		System.out.println("lsb == lsbR? " + (lsb==lsbR));
		
		assertEquals(msb, msbR);
		assertEquals(lsb, lsbR);
		
		UUID uuidR = new UUID(msbR, lsbR);
		
		assertEquals(uuidR,uuid);
	}
}
