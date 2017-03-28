package com.storageclient.common;

import java.io.File;

public class CalcDirSize {
	private static final String fileDir ="E:\\logs";

	public static String recursiveSearch() {
		File file = new File(fileDir);
		StringBuilder sb = new StringBuilder();
		
		File[] filesList = file.listFiles();
		for (File f : filesList) {
			if (f.isDirectory() && !f.isHidden()) {
				sb.append("<dir>").append(f.getName()).append("<dir>");
				sb.append("<dirsize>").append(folderSize(f)).append("<dirsize>");
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
