package jp.recruit.misc;

import java.io.UnsupportedEncodingException;

public class StringValidator {
	private static boolean checkCharacterCode(String str, String encoding) {
		if (str == null) {
			return true;
		}
		try {
			byte[] bytes = str.getBytes(encoding);
			return str.equals(new String(bytes, encoding));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("エンコード名称が正しくありません。", e);
		}
	}

	public static boolean isWindows31j(String str) {
		return checkCharacterCode(str, "Windows-31j");
	}

	public static boolean isSJIS(String str) {
		return checkCharacterCode(str, "SJIS");
	}

	public static boolean isEUC(String str) {
		return checkCharacterCode(str, "euc-jp");
	}

	public static boolean isUTF8(String str) {
		return checkCharacterCode(str, "UTF-8");
	}
}
