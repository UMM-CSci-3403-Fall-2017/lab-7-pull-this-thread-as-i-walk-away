package echoserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer {
	public static final int PORT_NUMBER = 6013;
	
	private static class ClientWriter extends Thread{
		private InputStream in;
		private OutputStream out;
		
		public ClientWriter(Socket socket) throws IOException{
			this.in = socket.getInputStream();
			this.out = socket.getOutputStream();
		}
		
		public void run(){
			byte[] output = new byte[1];
			try {
				while(in.read(output) != -1){
					out.write(output);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		EchoServer server = new EchoServer();
		server.start();
	}

	private void start() throws IOException, InterruptedException {
		ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
		ExecutorService executor = Executors.newCachedThreadPool();
		while (true) {
			ClientWriter writer = new ClientWriter(serverSocket.accept());
			executor.execute(writer);
		}
	}
	
}