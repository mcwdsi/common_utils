package edu.uams.dbmi.util;

//import java.nio.charset.Charset;
import java.util.Locale;

public class ImmutableLocalizedString {
	
	String _s;
	Locale _l;
	//Charset _c;
	
	/**
	 * Create a localized string using the string s and set it in 
	 * 	the context of the language indicated by the locale l
	 * 
	 * If l is null, the constructor will assume the default locale
	 * 	automatically.
	 * 
	 * If locale is not relevant or the string is applicable in 
	 * 	multiple locales, then do not use this class.
	 * 
	 */
	public ImmutableLocalizedString(String s, Locale l) {
		setParams(s, (l==null) ? Locale.getDefault() : l);
	}
	
	public ImmutableLocalizedString(String s) {
		setParams(s, Locale.getDefault());
	}
	
	protected void setParams(String s, Locale l) {
		if (s==null || l==null) {
			throw new NullPointerException("ImmutableLocalizedString()." +
					" params may not be null");
		}
		_s = s;
		_l = l;
		//_c = c;
	}

	public String getString() {
		return _s;
	}
	
	public Locale getLocale() {
		return _l;
	}
}
