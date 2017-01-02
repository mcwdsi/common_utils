package edu.uams.dbmi.util;

import java.util.Locale;

import junit.framework.TestCase;

import org.junit.Test;

public class ImmutableLocalizedStringTest extends TestCase {
	
	@Test public void testStringEquals() {
		String s = "colour";
		Locale l = Locale.UK;
		ImmutableLocalizedString ils = new ImmutableLocalizedString(s, l);
		
		assertTrue(ils.getString().equals(s));
	}

}
