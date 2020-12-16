package chatting;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class ChatClientApp {
	private static final String IP = "127.0.0.1";
	private static final int PORT = 7777;
	private static String nickName = null;
	public static Socket socket = null;
	
	public static void main(String[] args) {
		
		try {
			//1. socket 생성
			socket = new Socket();
			
			//2. connect to Server
			socket.connect(new InetSocketAddress(IP, PORT));
			
			//3. iostream 생성
			//BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
			
			//4. join 프로토콜 요청 및 처리
			joinProto();
			pw.println("Join : " + nickName);
			pw.flush();
			
			// chatclientReceiveThread 시작
			new ChatClientThread(socket).start();
			
		} catch (SocketException e) {
			System.out.println("[client] error : " + e);
		} catch (IOException e) {
			System.out.println("[client] error : " + e);
		} finally {
			try {
				if(socket != null && !socket.isClosed()) {
					socket.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
		//5. join 프로토콜 응답이 성공이면
		//   new ChatWindow(name, socket).show();
		new ChatWindow(nickName, socket).show();
	}
	
	private static void joinProto() {
		System.out.print("닉네임 >> ");
		Scanner scanner = new Scanner(System.in);
		nickName = scanner.nextLine();
		
		if(nickName != null) {
			scanner.close();
		} else {
			joinProto();
		}
	}
}