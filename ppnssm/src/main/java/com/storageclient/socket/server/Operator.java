package com.storageclient.socket.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.storageclient.common.ByteOper;
import com.storageclient.common.CalcDirSize;
public class Operator implements Runnable{
	private static Logger logger = Logger.getLogger(Operator.class);
	private static final String code = "gbk";
	public Operator(Socket client){
		this.client = client;
	}
	private Socket client;
	@Override
	public void run() {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		String reqStr = null;
		try {
			inputStream = client.getInputStream();
			outputStream = client.getOutputStream();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int byteLength =4;
			byte[] Lengthbuffer = new byte[byteLength];
			inputStream.read(Lengthbuffer);//读取 总长度
			int byteSize = ByteOper.byte4ToInt(Lengthbuffer, 0);//传输总长度
			if(byteSize==0){
				logger.warn(client.getInetAddress());
				logger.warn("请求长度为0");
				throw new IOException();
			}
			byte[] buffer = new byte[byteSize];
			
			int tsize = inputStream.read(buffer);//读取内容
			out.write(buffer, 0, tsize);
			out.flush();
			reqStr = new String(out.toByteArray(),ByteOper.characterEncoding);
			out.close();
			logger.info("JSW接收请求体--:"+reqStr);
			// 接收数据后业务处理
			String resultString = CalcDirSize.recursiveSearch();
			
			
			logger.info("JSW回复报文体--:"+resultString);
			logger.info("JSW回复报文长度--:"+resultString.getBytes(code).length);
			outputStream.write(ByteOper.intToByte4(resultString.getBytes(code).length));
			outputStream.flush();
			outputStream.write(resultString.getBytes(code));
			outputStream.flush();	
		} catch (IOException e) {
			logger.error(e.getMessage());
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

}
