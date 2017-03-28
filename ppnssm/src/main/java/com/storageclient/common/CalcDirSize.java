package com.storageclient.common;

import java.io.File;

public class CalcDirSize {
	private static final String fileDir ="E:\\logs";

	public static String recursiveSearch(String filedir) {
		File file = new File(filedir);
		StringBuilder sb = new StringBuilder();
		
		File[] filesList = file.listFiles();
		for (File f : filesList) {
			if (f.isDirectory() && !f.isHidden()) {
				sb.append("<dir>");
				sb.append("<dirname>").append(f.getName()).append("<dirname>");
				sb.append("<dirsize>").append(folderSize(f)).append("<dirsize>");
				sb.append("</dir>");
			}
		}
		return sb.toString();

	}

	public static long folderSize(File directory) {
		long length = 0;
		for (File file : directory.listFiles()) {
			if (file.isFile())
				length += file.length();
			else
				length += folderSize(file);
		}
		return length;
	}

}
