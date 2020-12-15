package echo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
	public static final int PORT = 7000;
	
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		
		try {
			// 1. 서버소켓 생성
			serverSocket = new ServerSocket();
			
			// 2. 바인딩(binding) : Socket Address(IP Address + Port)
			serverSocket.bind(new InetSocketAddress("127.0.0.1", PORT));
			
			// 3. accept
			while(true) {
				Socket socket = serverSocket.accept();									
				new EchoServerReceiveThread(socket).start();
			}
			
		} catch (IOException e) {
			System.out.println("[server] error : " + e);
		} finally {
			try {
				if(serverSocket != null && !serverSocket.isClosed()) {
					serverSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}