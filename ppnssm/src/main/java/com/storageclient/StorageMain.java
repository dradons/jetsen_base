package com.storageclient;


import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.storageclient.common.CalcDirSize;
import com.storageclient.common.DateUtil;
import com.storageclient.common.PropertiesUtil;
import com.storageclient.socket.client.ClientOperator;
import com.storageclient.socket.server.SocketServer;

public class StorageMain {
	private static Logger logger = Logger.getLogger(StorageMain.class);
	private static  int serverPort = 20000;//客户端提供的服务端口
	private static  int clientPort = 10000;//客户端调用服务上报数据端口
	private static  int  interval = 24;//在线超过多久自动上报数据，单位小时
	private static  String  clientIp = "127.0.0.1";
	private static  String  remoteBusinessIp = "127.0.0.1";
	private static  String  fileDir = "E:\\logs";
	private static  Properties properties ;
	static{
		try {
			properties = PropertiesUtil.getProperties(StorageMain.class, "/config.properties");
			clientIp = properties.getProperty("clientIp");
			remoteBusinessIp= properties.getProperty("remoteBusinessIp");
			fileDir = properties.getProperty("fileDir");
			serverPort = Integer.parseInt(properties.getProperty("serverPort"));
			clientPort = Integer.parseInt(properties.getProperty("clientPort"));
			interval = Integer.parseInt(properties.getProperty("interval"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void test() throws UnknownHostException, IOException{
		
		logger.info("开机启动自动上报");
		String data = CalcDirSize.recursiveSearch(fileDir);
		String reportdata =generateDateXml(data);
		logger.info("将要上报的信息:"+reportdata);
		ClientOperator.reportData(reportdata,remoteBusinessIp,10000);
		
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Long start = System.currentTimeMillis();
		logger.info("程序启动"+new Date());
		new SocketServer(serverPort,fileDir).init();
		
		
		logger.info("开机启动自动上报");
		String data = CalcDirSize.recursiveSearch(fileDir);
		String reportdata =generateDateXml(data);
		logger.info("将要上报的信息:"+reportdata);
		ClientOperator.reportData(reportdata,remoteBusinessIp,clientPort);
		//超过24小时自动重新上报数据
		while (true){
			Long end = System.currentTimeMillis();
			if((end-start)>(interval*60*60*1000)){
				logger.info("超过24小时启动自动上报");
				reportdata =generateDateXml(data);
				logger.info("将要上报的信息:"+reportdata);
				ClientOperator.reportData(reportdata,remoteBusinessIp,clientPort);
			}else{
				logger.info("未超过24小时等待上报");
			}			
			try {
				TimeUnit.HOURS.sleep(1);
			} catch (InterruptedException e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	public static String generateDateXml(String data){
		StringBuilder sb = new StringBuilder();
		sb.append("<root>");
		sb.append("<ip>").append(clientIp).append("</ip>");
		sb.append("<datetime>").append(DateUtil.date2Str(new Date(), null)).append("</datetime>");
		sb.append("<dirs>");
		sb.append(data);
		sb.append("</dirs>");
		sb.append("</root>");
		return sb.toString();
		
	}

}
