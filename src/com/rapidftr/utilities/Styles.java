package com.rapidftr.utilities;

import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;

public class Styles {

	public static Font getDefaultFont() {
		int familyIndex = 6;
		int fontType = FontFamily.SCALABLE_FONT;
		int fontSize = 12;
		
		return getFont(familyIndex, fontType, fontSize);
	}

	public static Font getAuxFont() {
		int familyIndex = 2;
		int fontType = FontFamily.CBTF_FONT;
		int fontSize = 12;
		
		return getFont(familyIndex, fontType, fontSize);		
	}
	
	public static Font getSecondaryFont() {
		int familyIndex = 6;
		int fontType = FontFamily.CBTF_FONT;
		int fontSize = 12;
		
		return getFont(familyIndex, fontType, fontSize);
	}
	
	public static Font getTitleFont() {
		int familyIndex = 0;
		int fontType = FontFamily.TRUE_TYPE_FONT;
		int fontSize = 14;
		
		return getFont(familyIndex, fontType, fontSize);
	}

	public static Font getHeaderFont() {
		int familyIndex = 1;
		int fontType = FontFamily.TRUE_TYPE_FONT;
		int fontSize = 14;
		
		return getFont(familyIndex, fontType, fontSize);
	}
	
	private static Font getFont(int familyIndex, int fontType,int fontSize) {
		final FontFamily fontFamily[] = FontFamily.getFontFamilies();
		
		return fontFamily[familyIndex].getFont(fontType, fontSize);
	}
}
