package echoserver;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EchoClient {
	public static final int PORT_NUMBER = 6013;
	
	public static class ServerWriter extends Thread{
		OutputStream out;
		Socket socket;
		
		public ServerWriter(Socket socket) throws IOException{
			this.socket = socket;
			out = socket.getOutputStream();
		}
		
		public void run(){
			InputStream stdIn = new DataInputStream(System.in);
			byte[] input = new byte[1];
			try {
				while(stdIn.read(input) != -1){
					out.write(input);
				}
				this.socket.shutdownOutput();
				out.close();
				stdIn.close();
			} catch (IOException e) {
				System.out.println("Ayy lmao how did I end up here. :(");
				e.printStackTrace();
			}
		}
	}
	
	public static class ServerReader extends Thread{
		InputStream in;
		Socket socket;
		
		public ServerReader(Socket socket) throws IOException{
			this.socket = socket;
			in = socket.getInputStream();
		}
		
		public void run(){
			byte[] output = new byte[1];
			try {
				while(in.read(output) != -1){
					System.out.write(output, 0, 1);
				}
				System.out.flush();
				socket.close();
				in.close();
			} catch (IOException e) {
				System.out.println("Ayy lmao how did I end up here. :(");
				e.printStackTrace();
			}
			
		}
	}

	public static void main(String[] args) throws IOException {
		EchoClient client = new EchoClient();
		client.start();
	}

	private void start() throws IOException {
		Socket socket = new Socket("localhost", PORT_NUMBER);
		ServerReader reader = new ServerReader(socket);
		ServerWriter writer = new ServerWriter(socket);
		reader.start();
		writer.start();
		System.out.flush();
	}
	
}