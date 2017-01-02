package edu.uams.dbmi.util;

import java.util.UUID;

public class UuidUtil {
	
	public static String getNewUuidEncodedInBase64() {
		UUID uuid = UUID.randomUUID();
		long leastSig = uuid.getLeastSignificantBits();
		long mostSig = uuid.getMostSignificantBits();
		
		byte[] uuidAsBytes = new byte[16];
		DatatypeConverter.longToByteArray(uuidAsBytes, leastSig, 8);
		DatatypeConverter.longToByteArray(uuidAsBytes, mostSig, 0);
		
		return Base64.encodeToString(uuidAsBytes, false);
	}
	
	public static String getNewUuidEncodedInBase64URL() {
		UUID uuid = UUID.randomUUID();
		long leastSig = uuid.getLeastSignificantBits();
		long mostSig = uuid.getMostSignificantBits();
		
		byte[] uuidAsBytes = new byte[16];
		DatatypeConverter.longToByteArray(uuidAsBytes, leastSig, 8);
		DatatypeConverter.longToByteArray(uuidAsBytes, mostSig, 0);
		
		return Base64Url.encodeToString(uuidAsBytes);
	}
	
	public static byte[] convertUuidToByteArray(UUID uuid) {
		long msb = uuid.getMostSignificantBits();
		long lsb = uuid.getLeastSignificantBits();
		
		byte[] ba = new byte[16];
		
		DatatypeConverter.longToByteArray(ba, msb, 0);
		DatatypeConverter.longToByteArray(ba, lsb, 8);
		
		return ba;
	}
	
	public static UUID convertByteArrayToUuid(byte[] ba) {
		if (ba == null || ba.length != 16) {
			throw new IllegalArgumentException("byte array must be 16 bytes");
		}
		long msb = DatatypeConverter.toLong(ba, 0);
		long lsb = DatatypeConverter.toLong(ba, 8);
		return new UUID(msb, lsb);
	}
}
