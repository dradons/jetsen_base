package com.storageclient.socket.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.storageclient.common.ByteOper;

public class ClientOperator implements Runnable{
	private static Logger logger = Logger.getLogger(ClientOperator.class);
	private static final String code = "gbk";
	private String requestString;
	public ClientOperator(Socket client,String req){
		this.client = client;
		this.requestString = req;
	}
	private Socket client;
	public void run() {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		String reqStr = null;
		try {
			outputStream = client.getOutputStream();
			
			logger.info("JSW上报请求体——"+requestString);
			logger.info("JSW上报请求长度——"+requestString.getBytes(code).length);
			outputStream.write(ByteOper.intToByte4(requestString.getBytes(code).length));//头
			outputStream.flush();
			outputStream.write(requestString.getBytes(code));//体
			outputStream.flush();
			
			inputStream = client.getInputStream();
			logger.info(inputStream.available());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int byteLength =4;
			byte[] Lengthbuffer = new byte[byteLength];
			inputStream.read(Lengthbuffer);//读取 总长度
			int byteSize = ByteOper.byte4ToInt(Lengthbuffer, 0);//传输总长度
			byte[] buffer = new byte[byteSize];
			int tsize = inputStream.read(buffer);//读取内容
			out.write(buffer, 0, tsize);
			out.flush();
			reqStr = new String(out.toByteArray(),ByteOper.characterEncoding);
			out.close();
			logger.info("JSW上报结果—— "+reqStr);
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(inputStream!=null){
				
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(outputStream!=null){
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(this.client != null && !(this.client.isConnected())){
				try {
					this.client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}
	
	/**
	 * socket 上报目录数据
	 * @param data
	 */
	public static void reportData(String data){
		Socket client;
		try {
			client = new Socket("127.0.0.1", 10000);
			ClientOperator op = new ClientOperator(client,data);
			op.run();
		} catch (UnknownHostException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} 
	}
}
