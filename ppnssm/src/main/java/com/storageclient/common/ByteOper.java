package com.storageclient.common;

public class ByteOper {
	public static final String characterEncoding = "gbk";
	public static byte[] intToByte4(int i) {
		byte[] targets = new byte[4];
		targets[0] = (byte) (i & 0xFF);
		targets[1] = (byte) (i >> 8 & 0xFF);
		targets[2] = (byte) (i >> 16 & 0xFF);
		targets[3] = (byte) (i >> 24 & 0xFF);
		return targets;
	}

	public static int byte4ToInt(byte[] bytes, int off) {
		int b0 = bytes[off] & 0xFF;
		int b1 = bytes[off + 1] & 0xFF;
		int b2 = bytes[off + 2] & 0xFF;
		int b3 = bytes[off + 3] & 0xFF;
		return (b3 << 24) | (b2 << 16) | (b1 << 8) | b0;
	}
	
	public static void main(String[] args) {
		 byte[] ints = ByteOper.intToByte4(10);
		 byte[] by = new byte[4];
		 by[0] = -64;
		 by[1] = 0;
		 by[2] = 0;
		 by[3] = 0;
		System.out.println(ByteOper.byte4ToInt(by, 0)); ;
		 
		 
	}

}
