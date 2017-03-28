package com.storageclient.socket.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

import org.apache.log4j.Logger;

/**
 * Servlet implementation class SocketServlet
 */
public class SocketServer {
	private static final long serialVersionUID = 1L;
	public  ExecutorService threadPools;
	public static int heartPort = 10000;//socket port
	public static int nThreads = 10;
	public static String fileDir = "E:\\logs";;
	
	/**
	 * 构造器
	 * @param port
	 */
	public SocketServer(int port,String filedir){
		this.heartPort = port;
		this.fileDir = filedir;
	}
    public void init() {
    	Thread t = new Thread(new Runnable() {
			public void run() {
				threadPools = java.util.concurrent.Executors.newFixedThreadPool(nThreads);//10
				ServerSocket server;
				try {
					server = new ServerSocket(heartPort);
					while(true){
						Socket client = server.accept();
						Operator op = new Operator(client,fileDir);
						threadPools.execute(op);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
    	t.start();
    
		}
}
