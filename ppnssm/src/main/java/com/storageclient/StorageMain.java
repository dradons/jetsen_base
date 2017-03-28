package com.storageclient;


import org.apache.log4j.Logger;

import com.storageclient.common.CalcDirSize;
import com.storageclient.socket.client.ClientOperator;
import com.storageclient.socket.server.SocketServer;

public class StorageMain {
	private static Logger logger = Logger.getLogger(StorageMain.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		logger.info("程序启动"+System.currentTimeMillis());
		new SocketServer().init();
		String data = CalcDirSize.recursiveSearch();
		ClientOperator.reportData(data);

	}

}
