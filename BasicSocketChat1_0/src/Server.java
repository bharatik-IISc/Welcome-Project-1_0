import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	//initialize socket and input stream
	 
	private ServerSocket server = null;
	
	
	
	//Constructor with port
	public Server(int port)
	{
		//Starts the server 
		try {
			server = new ServerSocket(port);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("Unable to start the server");
			e1.printStackTrace();
			return;
		}
		System.out.println("Server Started");
			
		// Infinite loop for client requests
		while(true)
		{
			Socket socket = null;
			try {
					
				System.out.println("Server is accepting new Clients");
				socket = server.accept();
				System.out.println("Client accepted");
				
				//Takes input and output streams from the client socket
				DataInputStream in = new DataInputStream(socket.getInputStream());
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				
				//Assigning a new thread for this client
				Thread t = new ClientHandler(socket,in,out);
				t.start();
				
				String line = "";
					try {
						line = in.readUTF();
						System.out.println(line);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.println("Unable to read input");
						e.printStackTrace();
					}
					

				
					
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Socket Exception (create/accept)");
				e.printStackTrace();
			}
			finally {
				//Closing the connection
				System.out.println("Closing the connection");
				
				try {
					socket.close();
					in.close();
					
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("Unable to close socket/input stream");
					e.printStackTrace();
				}
			}
				
		}
			

		
		
	
			
		
	}
	
	public static void main(String args[])
	{
		Server server = new Server(5000);
	}

}
