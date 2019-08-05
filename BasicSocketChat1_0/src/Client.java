import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	//initialize socket and io streams
	private Socket socket = null;
	private BufferedReader input = null;
	private DataOutputStream out = null;
	
	//constructor to initialize IP and port
	public Client(String ip, int port)
	{
		//establish a connection
		System.out.println("establish a connection");
		try {
			socket = new Socket(ip,port);
			System.out.println("Client Initialized Successfully");
			
			//Take input from terminal
			input = new BufferedReader(new InputStreamReader(System.in));
			
			// Sends output to the socket
			out = new DataOutputStream(socket.getOutputStream());
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to initialize client : Unknown Host Exception");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to initialize client : IO Exception");
			e.printStackTrace();
		}
		
		//String to read message from input
		String line = "";
		
		try {
			//Limitation introduced : read message just till 1st \r, \n or EOF 
			System.out.println("Wait for client input and write to socket");
			line = input.readLine();
			out.writeUTF(line);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//Close the connection
		try {
			System.out.println("Close the io streams and socket  at client");
			input.close();
			out.close();
			socket.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		
		
		
		
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Client c = new Client("127.0.0.1",5000);

	}

}
