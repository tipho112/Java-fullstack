package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class TCPServer {

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		
		try {
			// 1. 서버소켓 생성
			serverSocket = new ServerSocket();
			
			// 2. 바인딩(binding) : Socket Address(IP Address + Port)
			InetSocketAddress inetSocketAddress = new InetSocketAddress("192.168.0.8", 6000);
			serverSocket.bind(inetSocketAddress);
			
			// 3. accept
			//serverSocket.accept();		// blocking
			//System.out.println("end blocking");
			
			Socket socket = serverSocket.accept();						
			InetSocketAddress remoteInetSocketAddress = (InetSocketAddress)socket.getRemoteSocketAddress();			
			InetAddress remoteInetAddress = remoteInetSocketAddress.getAddress();
			String  remoteHostAddress = remoteInetAddress.getHostAddress();
			int remotePort = remoteInetSocketAddress.getPort();			
			System.out.println("[server] connected by client[" + remoteHostAddress + ":" + remotePort + "]");
			
			try {
				// 4. IOStream 받아오기
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();
				// 5. 데이터 읽기
				while(true) {
					byte[] buffer = new byte[256];
					int readByteCount = is.read(buffer);	// blocking 상태
					
					if(readByteCount == -1) {
						// client가 소켓을 정상적으로 종료
						System.out.println("[server] closed by client");
						break;
					}
					
					String data = new String(buffer, 0, readByteCount, "UTF-8");
					System.out.println("[server] received : " + data);
					
					// 6. 데이터쓰기
					os.write(data.getBytes("UTF-8"));
				}
			} catch(SocketException e) {
				System.out.println("[server] suddenly closed by client");
			} catch(IOException e) {
				System.out.println("[server] error : " + e);
			}
			finally {
				try {
					if(socket != null && !socket.isClosed()) {
						socket.close();
					}
				} catch(IOException e) {
					e.printStackTrace();
				}
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